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

public class CallbackDelegate implements Callback {
    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onSuccess() {
        if (mCallback != null) {
            mCallback.onSuccess();
        }
    }

    @Override
    public void onFailed(Throwable error) {
        if (mCallback != null) {
            mCallback.onFailed(error);
        }
    }

    @Override
    public void onRecording(long presentationTimeUs) {
        if (mCallback != null) {
            mCallback.onRecording(presentationTimeUs);
        }
    }
}
