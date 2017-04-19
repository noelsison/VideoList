package com.example.noel.videolist.activity.audio;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Noel on 4/19/2017.
 */

public class AudioRecorder {
    private final static String TAG = AudioRecorder.class.getName();

    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    public AudioRecorder () {

    }

    public void startRecording(String filename) {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Recording failed");
        }

        mediaRecorder.start();
        isRecording = true;
    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
