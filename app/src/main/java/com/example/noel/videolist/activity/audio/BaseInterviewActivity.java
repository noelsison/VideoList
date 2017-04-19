package com.example.noel.videolist.activity.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Noel on 4/19/2017.
 */

public abstract class BaseInterviewActivity extends AppCompatActivity implements AudioPlayer.MediaPlayerListener {

    private static final String TAG = AudioRecorderActivity.class.getName();
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 200;
    private static final int PERMISSION_INDEX_RECORD_AUDIO = 0;
    private static final String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};

    final String PERMISSION_REQUIRED = "Permission to Record Audio is required to continue";

    protected AudioPlayer audioPlayer;
    protected AudioRecorder audioRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Remove development only code on production
        if (Build.MODEL.toUpperCase().contains("SDK")) {
            usingEmulatorAction();
        }

        requestForPermission();
        audioPlayer = new AudioPlayer(this);
        audioRecorder = new AudioRecorder();
    }

    private void requestForPermission() {
        int recordAudioPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (recordAudioPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissionToRecordAudioGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_RECORD_AUDIO:
                isPermissionToRecordAudioGranted = grantResults[PERMISSION_INDEX_RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isPermissionToRecordAudioGranted) {
            Toast.makeText(this, PERMISSION_REQUIRED, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioPlayer.isPlaying()) {
            audioPlayer.stopPlaying();
        }
        if (audioRecorder.isRecording()) {
            audioRecorder.stopRecording();
        }
    }

    protected void usingEmulatorAction() {
        Toast.makeText(
                getApplicationContext(),
                "AUDIO RECORDING IS NOT SUPPORTED IN EMULATOR",
                Toast.LENGTH_LONG
        ).show();
    }
}