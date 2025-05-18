/*
 * Copyright (c) 2014 Yrom Wang <http://www.yrom.net>
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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodecInfo;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.yrom.screenrecorder.view.NamedSpinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.M;
import static net.yrom.screenrecorder.ScreenRecorder.AUDIO_AAC;
import static net.yrom.screenrecorder.ScreenRecorder.VIDEO_AVC;

public class MainActivity extends Activity {
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private static final int REQUEST_POST_NOTIFICATIONS = 1001;
    public static final String ACTION_STOP = "net.yrom.screenrecorder.action.STOP";
    
    // members below will be initialized in onCreate()
    private MediaProjectionManager mMediaProjectionManager;
    private Button mButton;
    private Button mSaveButton;
    private ToggleButton mAudioToggle;
    private NamedSpinner mVideoResolution;
    private NamedSpinner mVideoFramerate;
    private NamedSpinner mIFrameInterval;
    private NamedSpinner mVideoBitrate;
    private NamedSpinner mAudioBitrate;
    private NamedSpinner mAudioSampleRate;
    private NamedSpinner mAudioChannelCount;
    private NamedSpinner mVideoCodec;
    private NamedSpinner mAudioCodec;
    private NamedSpinner mVideoProfileLevel;
    private NamedSpinner mAudioProfile;
    private NamedSpinner mOrientation;
    private Spinner mAudioSourceSpinner;
    private MediaCodecInfo[] mAvcCodecInfos; // avc codecs
    private MediaCodecInfo[] mAacCodecInfos; // aac codecs
    private Notifications mNotifications;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        mNotifications = new Notifications(getApplicationContext());
        bindViews();
    }

    private void bindViews() {
        mButton = findViewById(R.id.record_button);
        mButton.setOnClickListener(this::onButtonClick);
        mSaveButton = findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this::onSaveButtonClick);
        mVideoCodec = findViewById(R.id.video_codec);
        mVideoResolution = findViewById(R.id.resolution);
        mVideoFramerate = findViewById(R.id.framerate);
        mIFrameInterval = findViewById(R.id.iframe_interval);
        mVideoBitrate = findViewById(R.id.video_bitrate);
        mOrientation = findViewById(R.id.orientation);
        mAudioCodec = findViewById(R.id.audio_codec);
        mVideoProfileLevel = findViewById(R.id.avc_profile);
        mAudioBitrate = findViewById(R.id.audio_bitrate);
        mAudioSampleRate = findViewById(R.id.sample_rate);
        mAudioProfile = findViewById(R.id.aac_profile);
        mAudioChannelCount = findViewById(R.id.audio_channel_count);
        mAudioToggle = findViewById(R.id.with_audio);
        mAudioSourceSpinner = findViewById(R.id.audio_source_spinner);
    }

    private void onButtonClick(View v) {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
        isRecording = !isRecording;
        updateRecordButton();
    }

    private void onSaveButtonClick(View v) {
        saveConfiguration();
    }

    private void startRecording() {
        if (Build.VERSION.SDK_INT >= M) {
            if (!hasPermissions()) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUEST_PERMISSIONS);
                return;
            }
        }
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

    private void stopRecording() {
        // Implementation of stopping recording
        mButton.setText(R.string.start_recorder);
    }

    private void updateRecordButton() {
        mButton.setText(isRecording ? R.string.stop_recorder : R.string.start_recorder);
    }

    private void saveConfiguration() {
        // Implementation of saving configuration
        Toast.makeText(this, R.string.set_default, Toast.LENGTH_SHORT).show();
    }

    private boolean hasPermissions() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        return pm.checkPermission(WRITE_EXTERNAL_STORAGE, packageName) == PackageManager.PERMISSION_GRANTED
                && pm.checkPermission(RECORD_AUDIO, packageName) == PackageManager.PERMISSION_GRANTED;
    }

    // --- Notification Permission Handling for Android 13+ ---

    private static final int NOTIFICATION_ID = 2001;

    // Example method to show a notification safely
    private void showScreenRecorderNotification(Notification notification) {
        if (!mNotifications.canPostNotifications(this)) {
            Notifications.requestNotificationPermission(this, REQUEST_POST_NOTIFICATIONS);
        } else {
            mNotifications.notify(this, NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now show notifications
                // For example, show a notification or inform the user
                // mNotifications.notify(this, NOTIFICATION_ID, notificationObject);
            } else {
                // Permission denied, inform user
                Toast.makeText(this, "Notification permission denied. You may miss important alerts.", Toast.LENGTH_SHORT).show();
            }
        }
        // Handle your other permissions as needed...
    }
}
