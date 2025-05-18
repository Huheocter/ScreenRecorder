package net.yrom.screenrecorder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class RecordHandler extends Handler {
    private static final int MSG_START = 0;
    private static final int MSG_STOP = 1;
    private static final int MSG_ERROR = 2;

    private final MicRecorder mRecorder;

    public RecordHandler(MicRecorder recorder, Looper looper) {
        super(looper);
        mRecorder = recorder;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_START:
                mRecorder.handleStartRecord();
                break;
            case MSG_STOP:
                mRecorder.handleStopRecord();
                break;
            case MSG_ERROR:
                mRecorder.handleError((Throwable) msg.obj);
                break;
        }
    }

    public void sendStartMessage() {
        sendEmptyMessage(MSG_START);
    }

    public void sendStopMessage() {
        sendEmptyMessage(MSG_STOP);
    }

    public void sendErrorMessage(Throwable error) {
        obtainMessage(MSG_ERROR, error).sendToTarget();
    }
}
