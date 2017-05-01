package com.example.noel.videolist.activity.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Noel on 4/19/2017.
 */

public class AudioPlayer implements MediaPlayer.OnCompletionListener {
    private final static String TAG = AudioPlayer.class.getName();

    MediaPlayerListener mediaPlayerListener;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    public AudioPlayer(MediaPlayerListener mediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener;
    }

    public void startPlaying(AssetFileDescriptor assetFileDescriptor) {
        if (isPlaying) {
            stopPlaying();
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            Log.e(TAG, "Play failed");
        }
    }

    public void startPlaying(String filename) {
        if (isPlaying) {
            stopPlaying();
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            Log.e(TAG, "Play failed");
        }
    }

    public void stopPlaying() {
        if (!isPlaying) {
            return;
        }
        isPlaying = false;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayerListener != null) {
            mediaPlayerListener.onFinishPlaying();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    interface MediaPlayerListener {
        void onFinishPlaying();
    }
}
