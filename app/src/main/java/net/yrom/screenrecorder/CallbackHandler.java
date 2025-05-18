package net.yrom.screenrecorder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CallbackHandler extends Handler {
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_ERROR = 1;
    private static final int MSG_RECORDING = 2;

    private final Encoder.Callback mCallback;

    public CallbackHandler(Encoder.Callback callback) {
        super(Looper.getMainLooper());
        mCallback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SUCCESS:
                mCallback.onSuccess();
                break;
            case MSG_ERROR:
                mCallback.onFailed((Throwable) msg.obj);
                break;
            case MSG_RECORDING:
                mCallback.onRecording((long) msg.obj);
                break;
        }
    }

    public void notifySuccess() {
        obtainMessage(MSG_SUCCESS).sendToTarget();
    }

    public void notifyError(Throwable error) {
        obtainMessage(MSG_ERROR, error).sendToTarget();
    }

    public void notifyRecording(long presentationTimeUs) {
        obtainMessage(MSG_RECORDING, presentationTimeUs).sendToTarget();
    }
}
