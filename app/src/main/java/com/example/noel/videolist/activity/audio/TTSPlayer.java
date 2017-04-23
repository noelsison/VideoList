package com.example.noel.videolist.activity.audio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Noel on 4/19/2017.
 */

public class TTSPlayer extends UtteranceProgressListener {

    private final static String TAG = TTSPlayer.class.getName();

    private AudioPlayer.MediaPlayerListener mediaPlayerListener;
    private TextToSpeech textToSpeech;
    private boolean isPlaying = false;

    public TTSPlayer(Context context, final AudioPlayer.MediaPlayerListener mediaPlayerListener) {
        final TTSPlayer ttsPlayer = this;
        this.mediaPlayerListener = mediaPlayerListener;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.US);
                mediaPlayerListener.onFinishLoading();

                textToSpeech.setOnUtteranceProgressListener(ttsPlayer);
            }
        });
    }

    public void speak(String text) {
        isPlaying = true;
        HashMap<String, String> params = new HashMap<String, String>();
        String utteranceId = String.valueOf(text.hashCode());
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
        }
    }

    public void dispose() {
        textToSpeech.shutdown();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onStart(String utteranceId) {
    }

    @Override
    public void onDone(String utteranceId) {
        isPlaying = false;
        mediaPlayerListener.onFinishPlaying();
    }

    @Override
    public void onError(String utteranceId) {
    }
}
