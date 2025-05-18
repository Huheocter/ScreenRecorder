package net.yrom.screenrecorder;

import android.content.Context;
import android.os.HandlerThread;

public class MicRecorder implements Encoder {
    private final HandlerThread mRecordThread;
    private RecordHandler mRecordHandler;
    private Encoder.Callback mCallback;
    private boolean mIsRecording;

    // Constructor matching the call in ScreenRecorder.java
    public MicRecorder(Context context, AudioEncodeConfig audioConfig, Encoder.Callback callback) {
        mRecordThread = new HandlerThread("RecordThread");
        mCallback = callback;
        // Use context or audioConfig as needed in your implementation
    }

    // No-arg constructor if still needed elsewhere
    public MicRecorder() {
        mRecordThread = new HandlerThread("RecordThread");
    }

    @Override
    public void setCallback(Encoder.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void release() {
        // Clean up resources here
        if (mRecordThread.isAlive()) {
            mRecordThread.quitSafely();
        }
    }

    void handleStartRecord() {
        mIsRecording = true;
        if (mCallback != null) mCallback.onSuccess();
    }

    void handleStopRecord() {
        mIsRecording = false;
    }

    void handleError(Throwable error) {
        if (mCallback != null) mCallback.onFailed(error);
    }

    public void start() {
        mRecordThread.start();
        mRecordHandler = new RecordHandler(this, mRecordThread.getLooper());
        mRecordHandler.sendStartMessage();
    }

    public void stop() {
        if (mRecordHandler != null) {
            mRecordHandler.sendStopMessage();
        }
        mRecordThread.quitSafely();
    }

    public boolean isRecording() {
        return mIsRecording;
    }
}
