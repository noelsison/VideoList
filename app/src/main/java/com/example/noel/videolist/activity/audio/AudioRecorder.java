package com.example.noel.videolist.activity.audio;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Noel on 4/19/2017.
 */

public class AudioRecorder {
    private final static String TAG = AudioRecorder.class.getName();
    private final static int UPDATE_AMPLITUDE_INTERVAL = 150;

    private AudioRecorderListener audioRecorderListener;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    Handler handler;
    Runnable updateAmplitude;

    public AudioRecorder (final AudioRecorderListener audioRecorderListener) {
        this.audioRecorderListener = audioRecorderListener;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // TODO: Make this optional
        handler = new Handler();
        updateAmplitude = new Runnable() {
            @Override
            public void run() {
                audioRecorderListener.updateAmplitude(getMaxAmplitude());
                handler.postDelayed(this, UPDATE_AMPLITUDE_INTERVAL);
            }
        };
    }

    public void startRecording(String filename) {
        try {
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();

            handler.post(updateAmplitude);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Recording failed");
        }

        mediaRecorder.start();
        isRecording = true;
    }

    public void stopRecording() {
        isRecording = false;
        handler.removeCallbacks(updateAmplitude);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public int getMaxAmplitude() {
        return mediaRecorder.getMaxAmplitude();
    }

    interface AudioRecorderListener {
        void updateAmplitude(int amplitude);
    }
}
