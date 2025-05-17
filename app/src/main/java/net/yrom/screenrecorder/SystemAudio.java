if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    AudioPlaybackCaptureConfiguration config = new AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
        .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
        .build();

    AudioRecord record = new AudioRecord.Builder()
        .setAudioPlaybackCaptureConfig(config)
        .setAudioFormat(new AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(44100)
            .setChannelMask(AudioFormat.CHANNEL_IN_STEREO)
            .build())
        .setBufferSizeInBytes(bufferSize)
        .build();
    // Use 'record' for recording system audio
}
