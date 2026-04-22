package com.appnotresponding.rumbo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.appnotresponding.rumbo.navigation.Navigation
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.google.firebase.auth.FirebaseAuth

lateinit var auth: FirebaseAuth
lateinit var sensorManager: SensorManager
var lightSensor: Sensor? = null
var isDarkTheme by mutableStateOf(false)

// https://developer.android.com/reference/androidx/fragment/app/FragmentActivity
class MainActivity : FragmentActivity(), SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Inicializar sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        enableEdgeToEdge()
        setContent {
            // Se usa la integración de coil para mejorar el rendimiento de carga de imágenes, especialmente para listas con muchas imágenes docs: https://coil-kt.github.io/coil/network/
            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context).components {
                    add(OkHttpNetworkFetcherFactory())
                }.build()
            }
            RumboTheme(darkTheme = isDarkTheme) {
                Navigation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            isDarkTheme = lux < 2000
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}