package com.appnotresponding.rumbo.ui.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

data class CompassState(
    val degrees: Float,
    val direction: String
)

// grados a cardinalidad
private fun getCardinalDirection(degrees: Float): String {
    return when {
        degrees < 22.5f || degrees >= 337.5f -> "Norte ↑"
        degrees < 67.5f                      -> "Noreste ↗"
        degrees < 112.5f                     -> "Este →"
        degrees < 157.5f                     -> "Sureste ↘"
        degrees < 202.5f                     -> "Sur ↓"
        degrees < 247.5f                     -> "Suroeste ↙"
        degrees < 292.5f                     -> "Oeste ←"
        else                                 -> "Noroeste ↖"
    }
}

@Composable
fun rememberCompassManager(context: Context = LocalContext.current): CompassState {
    var degrees by remember { mutableFloatStateOf(0f) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val magnetometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    val accelerometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(Unit) {
        // El magnetometro necesita el acelerometro para calcular la orientacion correctamente
        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        gravity[0] = event.values[0]
                        gravity[1] = event.values[1]
                        gravity[2] = event.values[2]
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        geomagnetic[0] = event.values[0]
                        geomagnetic[1] = event.values[1]
                        geomagnetic[2] = event.values[2]
                    }
                }

                val rotationMatrix = FloatArray(9)
                val inclinationMatrix = FloatArray(9)
                val success = SensorManager.getRotationMatrix(
                    rotationMatrix,
                    inclinationMatrix,
                    gravity,
                    geomagnetic
                )

                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    // radianes a grados y se da el valor entre 0 y 360
                    val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    degrees = (azimuth + 360f) % 360f
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            magnetometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            listener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return CompassState(
        degrees = degrees,
        direction = getCardinalDirection(degrees)
    )
}
