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
    val degrees: Float
)
@Composable
fun rememberCompassManager(context: Context = LocalContext.current): CompassState {
    var degrees by remember { mutableFloatStateOf(0f) }
    val smoothingAlpha = 0.15f

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val rotationVectorSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    val magnetometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    val accelerometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(Unit) {
        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val remappedMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        SensorManager.remapCoordinateSystem(
                            rotationMatrix,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Z,
                            remappedMatrix
                        )
                        SensorManager.getOrientation(remappedMatrix, orientation)

                        val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        degrees = smoothAngle(degrees, (azimuth + 360f) % 360f, smoothingAlpha)
                    }

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

                if (event.sensor.type != Sensor.TYPE_ROTATION_VECTOR) {
                    val success = SensorManager.getRotationMatrix(
                        rotationMatrix,
                        remappedMatrix,
                        gravity,
                        geomagnetic
                    )

                    if (success) {
                        SensorManager.getOrientation(rotationMatrix, orientation)
                        val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        degrees = smoothAngle(degrees, (azimuth + 360f) % 360f, smoothingAlpha)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        rotationVectorSensor?.let {
            sensorManager.registerListener(
                listener, it, SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        sensorManager.registerListener(
            listener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return CompassState(
        degrees = degrees
    )
}

private fun smoothAngle(current: Float, target: Float, alpha: Float): Float {
    val delta = ((target - current + 540f) % 360f) - 180f
    return (current + alpha * delta + 360f) % 360f
}
