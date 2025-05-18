/*
 * Created by Huheocter on 2025-05-18 01:47:29 UTC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.yrom.screenrecorder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CallbackHandler extends Handler {
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_ERROR = 1;
    private static final int MSG_RECORDING = 2;

    private final Callback mCallback;

    public CallbackHandler(Callback callback) {
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
                mCallback.onRecording(msg.arg1);
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
        obtainMessage(MSG_RECORDING, (int) presentationTimeUs).sendToTarget();
    }
}
