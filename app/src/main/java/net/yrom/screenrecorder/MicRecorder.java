package net.yrom.screenrecorder;

import android.content.Context;
import android.os.HandlerThread;

public class MicRecorder implements Encoder {
    private final HandlerThread mRecordThread;
    private RecordHandler mRecordHandler;
    private Encoder.Callback mCallback;
    private boolean mIsRecording;

    public MicRecorder(Context context, AudioEncodeConfig audioConfig, Encoder.Callback callback) {
        mRecordThread = new HandlerThread("RecordThread");
        mCallback = callback;
        // Use context or audioConfig as needed in your implementation
    }

    public MicRecorder() {
        mRecordThread = new HandlerThread("RecordThread");
    }

    @Override
    public void setCallback(Encoder.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void release() {
        if (mRecordThread.isAlive()) {
            mRecordThread.quitSafely();
        }
    }

    @Override
    public void prepare() {
        // Implementation goes here if needed
    }

    void handleStartRecord() {
        mIsRecording = true;
        // If you want to notify success, do it via onError with null or a custom Exception.
        // Example: mCallback.onError(this, null); // but usually only errors are reported
    }

    void handleStopRecord() {
        mIsRecording = false;
    }

    void handleError(Exception error) {
        if (mCallback != null) mCallback.onError(this, error);
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
