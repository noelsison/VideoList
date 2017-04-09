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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.noel.videolist.R;

import java.io.IOException;

/**
 * Created by Noel on 3/20/2017.
 */

public class AudioRecorderActivity extends AppCompatActivity {

    private static final String TAG = AudioRecorderActivity.class.getName();
    private static final int REQUEST_PERMISSIONS_CODE = 200;
    private static final int PERMISSION_RECORD_AUDIO = 0;
    private static String filename;

    Button buttonRecord;
    Button buttonPlay;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    boolean isPermissionToRecordAccepted = false;
    String[] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean isRecording = false;
    boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        filename = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                isPermissionToRecordAccepted = grantResults[PERMISSION_RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isPermissionToRecordAccepted) {
            finish();
        } else {
            initialize();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRecording) {
            isRecording = false;
            onRecord(isRecording);
        }
        if (isPlaying) {
            isPlaying = false;
            onPlay(false);
        }
    }

    protected void initialize() {
        buttonRecord = (Button) findViewById(R.id.audio_recorder_record);
        buttonRecord.setText("Recording: " + isRecording);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                buttonRecord.setText("Recording: " + isRecording);
                onRecord(isRecording);
            }
        });

        buttonPlay = (Button) findViewById(R.id.audio_recorder_play);
        buttonPlay.setText("Playing: " + isPlaying);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                buttonPlay.setText("Playing: " + isPlaying);
                onPlay(isPlaying);
            }
        });

        if (Build.MODEL.toUpperCase().contains("SDK")) {
            Toast.makeText(
                    getApplicationContext(),
                    "AUDIO RECORDING IS NOT SUPPORTED IN EMULATOR",
                    Toast.LENGTH_LONG
            ).show();
            buttonRecord.setEnabled(false);
        }
    }

    protected void onRecord(boolean isStarted) {
        if (isStarted) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    protected void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Record failed");
        }

        mediaRecorder.start();
    }

    protected void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    protected void onPlay(boolean isStarted) {
        if (isStarted) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    protected void startPlaying() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
            // TODO: Add completion listener to media player
            // TODO: Add media controls to media player
        } catch (IOException e) {
            Log.e(TAG, "Play failed");
        }
    }

    protected void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
