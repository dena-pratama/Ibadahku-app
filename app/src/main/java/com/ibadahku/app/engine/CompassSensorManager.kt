package com.ibadahku.app.engine

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.ibadahku.app.domain.QiblaCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

data class CompassState(
    val azimuthDegrees: Float = 0f,
    val qiblaBearingDegrees: Float = 295f,
    val isAligned: Boolean = false,
    val accuracy: Int = SensorManager.SENSOR_STATUS_ACCURACY_HIGH,
    val distanceKm: Double = 7915.0,
    val cardinalDirection: String = "Arah ke Barat Daya",
    val hasCompassSensor: Boolean = true
)

class CompassSensorManager(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private var rotationVectorSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val _compassState = MutableStateFlow(CompassState())
    val compassState: StateFlow<CompassState> = _compassState.asStateFlow()

    private val lastAccelerometer = FloatArray(3)
    private val lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    private val rMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var smoothedAzimuth = 0f
    private val smoothingFactor = 0.15f // Low pass filter factor
    private var wasAlignedBefore = false
    private var targetQiblaBearing = 295.0

    init {
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (rotationVectorSensor == null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        }
        val hasSensor = (rotationVectorSensor != null) || (magnetometer != null)
        _compassState.value = _compassState.value.copy(hasCompassSensor = hasSensor)
    }

    fun updateCoordinates(lat: Double, lng: Double) {
        val bearing = QiblaCalculator.calculateBearing(lat, lng)
        val dist = QiblaCalculator.calculateDistanceKm(lat, lng)
        val cardinal = QiblaCalculator.getCardinalDirection(bearing)
        targetQiblaBearing = bearing
        _compassState.value = _compassState.value.copy(
            qiblaBearingDegrees = bearing.toFloat(),
            distanceKm = dist,
            cardinalDirection = cardinal
        )
    }

    fun startListening() {
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
            magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    fun setSimulatedAzimuth(degrees: Float) {
        processAzimuth(degrees, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMatrix, event.values)
            SensorManager.getOrientation(rMatrix, orientation)
            var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            azimuth = (azimuth + 360f) % 360f
            processAzimuth(azimuth, event.accuracy)
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
            lastAccelerometerSet = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val success = SensorManager.getRotationMatrix(rMatrix, null, lastAccelerometer, lastMagnetometer)
            if (success) {
                SensorManager.getOrientation(rMatrix, orientation)
                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360f) % 360f
                processAzimuth(azimuth, event.accuracy)
            }
        }
    }

    private fun processAzimuth(rawAzimuth: Float, accuracy: Int) {
        // Low pass filter
        smoothedAzimuth += smoothingFactor * (normalizeDegreeDiff(rawAzimuth - smoothedAzimuth))
        smoothedAzimuth = (smoothedAzimuth + 360f) % 360f

        val diff = abs(normalizeDegreeDiff(smoothedAzimuth - targetQiblaBearing.toFloat()))
        val isAligned = diff <= 3.0f // Within ±3 degrees as required in PRD KB-03

        if (isAligned && !wasAlignedBefore) {
            triggerAlignedHaptic()
        }
        wasAlignedBefore = isAligned

        _compassState.value = _compassState.value.copy(
            azimuthDegrees = smoothedAzimuth,
            isAligned = isAligned,
            accuracy = accuracy
        )
    }

    private fun normalizeDegreeDiff(diff: Float): Float {
        var d = diff
        while (d < -180f) d += 360f
        while (d > 180f) d -= 360f
        return d
    }

    private fun triggerAlignedHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        _compassState.value = _compassState.value.copy(accuracy = accuracy)
    }
}
