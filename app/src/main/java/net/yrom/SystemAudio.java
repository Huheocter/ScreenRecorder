// Add these imports
import android.media.projection.MediaProjection;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioAttributes;

// Add to your class fields
private boolean mRecordSystemAudio = false;
private MediaProjection mMediaProjection = null;

// Update the constructor to accept the new parameters
MicRecorder(AudioEncodeConfig config, boolean recordSystemAudio, MediaProjection mediaProjection) {
    // ... existing setup
    this.mRecordSystemAudio = recordSystemAudio;
    this.mMediaProjection = mediaProjection;
}

// Modify createAudioRecord()
private AudioRecord createAudioRecord(int sampleRateInHz, int channelConfig, int audioFormat) {
    int minBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    if (minBytes <= 0) {
        Log.e(TAG, "Bad arguments: getMinBufferSize");
        return null;
    }

    if (mRecordSystemAudio && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && mMediaProjection != null) {
        AudioPlaybackCaptureConfiguration config =
                new AudioPlaybackCaptureConfiguration.Builder(mMediaProjection)
                        .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                        .addMatchingUsage(AudioAttributes.USAGE_GAME)
                        .build();

        AudioFormat format = new AudioFormat.Builder()
                .setEncoding(audioFormat)
                .setSampleRate(sampleRateInHz)
                .setChannelMask(channelConfig)
                .build();

        return new AudioRecord.Builder()
                .setAudioFormat(format)
                .setBufferSizeInBytes(minBytes * 2)
                .setAudioPlaybackCaptureConfig(config)
                .build();
    } else {
        // Fallback to mic
        return new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                minBytes * 2);
    }
}
