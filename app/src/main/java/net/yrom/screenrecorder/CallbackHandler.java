package net.yrom.screenrecorder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CallbackHandler extends Handler {
    private static final int MSG_ERROR = 1;

    private final Encoder.Callback mCallback;
    private final Encoder mEncoder;

    public CallbackHandler(Encoder encoder, Encoder.Callback callback) {
        super(Looper.getMainLooper());
        mCallback = callback;
        mEncoder = encoder;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_ERROR) {
            mCallback.onError(mEncoder, (Exception) msg.obj);
        }
    }

    public void notifyError(Exception error) {
        obtainMessage(MSG_ERROR, error).sendToTarget();
    }
}
