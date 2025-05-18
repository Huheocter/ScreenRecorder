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

import android.media.MediaCodec;
import android.os.HandlerThread;

public class MicRecorder implements Encoder {
    private static final String TAG = "MicRecorder";
    private final HandlerThread mRecordThread;
    private RecordHandler mRecordHandler;
    private final CallbackDelegate mCallbackDelegate;
    private volatile boolean mIsRecording;

    public MicRecorder() {
        mRecordThread = new HandlerThread("RecordThread");
        mCallbackDelegate = new CallbackDelegate();
    }

    @Override
    public void setCallback(Callback callback) {
        mCallbackDelegate.setCallback(callback);
    }

    void handleStartRecord() {
        // Implementation of starting record
        mIsRecording = true;
    }

    void handleStopRecord() {
        // Implementation of stopping record
        mIsRecording = false;
    }

    void handleError(Throwable error) {
        mCallbackDelegate.onFailed(error);
    }

    public void start() {
        mRecordThread.start();
        mRecordHandler = new RecordHandler(this, mRecordThread.getLooper());
        mRecordHandler.sendStartMessage();
    }

    public void stop() {
        mRecordHandler.sendStopMessage();
        mRecordThread.quitSafely();
    }

    public boolean isRecording() {
        return mIsRecording;
    }
}
