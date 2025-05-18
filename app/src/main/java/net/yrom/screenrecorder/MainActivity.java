package net.yrom.screenrecorder;

import android.app.Activity;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private static final int REQUEST_POST_NOTIFICATIONS = 1001;
    public static final String ACTION_STOP = "net.yrom.screenrecorder.action.STOP";

    // Use Spinner for all fields formerly NamedSpinner
    private Spinner mVideoResolution;
    private Spinner mVideoFramerate;
    private Spinner mIFrameInterval;
    private Spinner mVideoBitrate;
    private Spinner mAudioBitrate;
    private Spinner mAudioSampleRate;
    private Spinner mAudioChannelCount;
    private Spinner mVideoCodec;
    private Spinner mAudioCodec;
    private Spinner mVideoProfileLevel;
    private Spinner mAudioProfile;
    private Spinner mOrientation;
    private Spinner mAudioSourceSpinner;
    private Button mButton;
    private Button mSaveButton;
    private ToggleButton mAudioToggle;
    private boolean isRecording = false;
    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        bindViews();
    }

    private void bindViews() {
        mButton = findViewById(R.id.record_button);
        mSaveButton = findViewById(R.id.save_button);
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

        mButton.setOnClickListener(v -> onButtonClick());
        mSaveButton.setOnClickListener(v -> onSaveButtonClick());
    }

    private void onButtonClick() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
        isRecording = !isRecording;
        updateRecordButton();
    }

    private void onSaveButtonClick() {
        saveConfiguration();
    }

    private void startRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions()) {
                requestPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.RECORD_AUDIO
                }, REQUEST_PERMISSIONS);
                return;
            }
        }
        // TODO: Start screen recording as before
    }

    private void stopRecording() {
        // TODO: Stop recording logic
        mButton.setText(R.string.start_recorder);
    }

    private void updateRecordButton() {
        mButton.setText(isRecording ? R.string.stop_recorder : R.string.start_recorder);
    }

    private void saveConfiguration() {
        // TODO: Save logic
        Toast.makeText(this, R.string.set_default, Toast.LENGTH_SHORT).show();
    }

    private boolean hasPermissions() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        return pm.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName) == PackageManager.PERMISSION_GRANTED
                && pm.checkPermission(android.Manifest.permission.RECORD_AUDIO, packageName) == PackageManager.PERMISSION_GRANTED;
    }
}
