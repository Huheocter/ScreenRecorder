/*
 * Copyright (c) 2017 Yrom Wang <http://www.yrom.net>
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

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseLongArray;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM;
import static android.media.MediaCodec.BUFFER_FLAG_KEY_FRAME;
import static android.media.MediaCodec.INFO_OUTPUT_FORMAT_CHANGED;
import static android.os.Build.VERSION_CODES.N;

public class MicRecorder implements Encoder {
    private static final String TAG = "MicRecorder";
    private static final boolean VERBOSE = false;

    private final AudioEncoder mEncoder;
    private final HandlerThread mRecordThread;
    private RecordHandler mRecordHandler;
    private AudioRecord mMic; // access in mRecordThread only!
    private int mSampleRate;
    private int mChannelConfig;
    private int mFormat = AudioFormat.ENCODING_PCM_16BIT;

    private AtomicBoolean mForceStop = new AtomicBoolean(false);
    private BaseEncoder.Callback mCallback;
    private CallbackDelegate mCallbackDelegate;
    private int mChannelsSampleRate;

    private final AudioEncodeConfig config;
    private final Context context;
    private final MediaProjection mediaProjection; // nullable

    public MicRecorder(Context context, AudioEncodeConfig config, MediaProjection mediaProjection) {
        this.context = context == null ? null : context.getApplicationContext();
        this.config = config;
        this.mediaProjection = mediaProjection;
        mEncoder = new AudioEncoder(config);
        mSampleRate = config.sampleRate;
        mChannelsSampleRate = mSampleRate * config.channelCount;
        if (VERBOSE) Log.i(TAG, "in bitrate " + mChannelsSampleRate * 16 /* PCM_16BIT*/);
        mChannelConfig = config.channelCount == 2 ? AudioFormat.CHANNEL_IN_STEREO : AudioFormat.CHANNEL_IN_MONO;
        mRecordThread = new HandlerThread(TAG);
    }

    // PATCH: Add legacy constructor for backward compatibility
    public MicRecorder(AudioEncodeConfig config) {
        this(null, config, null);
    }

    /** ... rest of the code unchanged ... **/
    // (leave all methods as they were in your repo)
}
