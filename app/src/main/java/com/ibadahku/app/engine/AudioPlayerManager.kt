package com.ibadahku.app.engine

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.ibadahku.app.R
import com.ibadahku.app.data.model.PrayerSettings

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

    fun playAdzan(
        soundResId: Int = R.raw.adzan_nabawi,
        volumePercent: Int = 100,
        fadeInSeconds: Int = 3,
        onComplete: () -> Unit = {}
    ) {
        stop()

        try {
            mediaPlayer = MediaPlayer.create(context, soundResId)?.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )

                setupPlayback(volumePercent, fadeInSeconds, onComplete)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isPlaying = false
        }
    }

    fun playAdzanUri(
        uri: Uri,
        volumePercent: Int = 100,
        fadeInSeconds: Int = 3,
        onComplete: () -> Unit = {}
    ) {
        stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(context, uri)
                prepare()
                setupPlayback(volumePercent, fadeInSeconds, onComplete)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isPlaying = false
            // Fallback to default raw if custom file cannot be read
            playAdzan(R.raw.adzan_nabawi, volumePercent, fadeInSeconds, onComplete)
        }
    }

    private fun MediaPlayer.setupPlayback(
        volumePercent: Int,
        fadeInSeconds: Int,
        onComplete: () -> Unit
    ) {
        val targetVol = volumePercent / 100.0f

        if (fadeInSeconds > 0) {
            setVolume(0.1f, 0.1f)
            start()
            this@AudioPlayerManager.isPlaying = true

            // Fade-in volume gradually
            val steps = 10
            val stepDelay = (fadeInSeconds * 1000L) / steps
            for (step in 1..steps) {
                handler.postDelayed({
                    if (this@AudioPlayerManager.isPlaying && mediaPlayer != null) {
                        val currentVol = (step.toFloat() / steps) * targetVol
                        mediaPlayer?.setVolume(currentVol, currentVol)
                    }
                }, step * stepDelay)
            }
        } else {
            setVolume(targetVol, targetVol)
            start()
            this@AudioPlayerManager.isPlaying = true
        }

        setOnCompletionListener {
            this@AudioPlayerManager.isPlaying = false
            stop()
            onComplete()
        }

        // Safety limit: Stop automatically after 3 minutes (PRD CS-07)
        handler.postDelayed({
            if (this@AudioPlayerManager.isPlaying) {
                stop()
                onComplete()
            }
        }, 3 * 60 * 1000L)
    }

    fun playAdzanFromSettings(
        settings: PrayerSettings,
        prayerName: String = "",
        volumePercent: Int = settings.volumePercent,
        fadeInSeconds: Int = settings.fadeInSeconds,
        onComplete: () -> Unit = {}
    ) {
        if (settings.adzanSoundType == "CUSTOM_FILE" && settings.adzanCustomUri.isNotBlank()) {
            try {
                val uri = Uri.parse(settings.adzanCustomUri)
                playAdzanUri(uri, volumePercent, fadeInSeconds, onComplete)
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val soundRes = when {
            prayerName.equals("Subuh", ignoreCase = true) || settings.adzanSoundId == "adzan_subuh" -> R.raw.adzan_subuh
            settings.adzanSoundId == "adzan_ringtone" -> R.raw.adzan_ringtone
            else -> R.raw.adzan_nabawi
        }

        playAdzan(soundRes, volumePercent, fadeInSeconds, onComplete)
    }

    fun playPreview(
        uri: Uri? = null,
        soundResId: Int = R.raw.adzan_nabawi,
        seconds: Int = 6,
        onFinish: () -> Unit = {}
    ) {
        if (uri != null) {
            playAdzanUri(uri, volumePercent = 100, fadeInSeconds = 0)
        } else {
            playAdzan(soundResId, volumePercent = 100, fadeInSeconds = 0)
        }
        handler.postDelayed({
            stop()
            onFinish()
        }, seconds * 1000L)
    }

    fun playTestSound(seconds: Int = 5, onFinish: () -> Unit = {}) {
        playAdzan(R.raw.adzan_nabawi, volumePercent = 100, fadeInSeconds = 0)
        handler.postDelayed({
            stop()
            onFinish()
        }, seconds * 1000L)
    }

    fun stop() {
        try {
            handler.removeCallbacksAndMessages(null)
            if (mediaPlayer != null) {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                }
                mediaPlayer?.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isPlaying = false
        }
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying
}
