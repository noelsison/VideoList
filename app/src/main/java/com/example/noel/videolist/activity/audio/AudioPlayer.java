package com.example.noel.videolist.activity.audio;

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

    protected void startPlaying(String filename) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Play failed");
        }
    }

    protected void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayerListener.onFinishPlaying();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    interface MediaPlayerListener {
        void onFinishPlaying();
    }
}
