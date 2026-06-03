package com.appnotresponding.rumbo.ui.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs
import kotlin.math.sqrt

data class AccelerometerState(
    val isMoving: Boolean
)

@Composable
fun rememberAccelerometerManager(context: Context = LocalContext.current): AccelerometerState {
    var isMoving by remember { mutableStateOf(false) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val accelerometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                // Calcul la magnitud del vector de aceleracion
                val magnitude = sqrt(x * x + y * y + z * z)
                // La gravedad es 9.8m/s2, si la diferencia supera el umbral el dispositivo se esta moviendo por lo que la gracvedad afecta al aceleromtro
                isMoving = abs(magnitude - SensorManager.GRAVITY_EARTH) > 1.5f
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return AccelerometerState(isMoving = isMoving)
}
