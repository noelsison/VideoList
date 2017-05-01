package com.example.noel.videolist.activity.interview.practice;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.audio.TTSPlayer;

/**
 * Created by Noel on 5/1/2017.
 */

public class InterviewPracticeResultActivity extends InterviewPracticeActivity {
    private static final String TAG = InterviewPracticeActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "NOW ON RESULTS");

        getSupportActionBar().setTitle("Results for " + title);
    }

    @Override
    public void onFinishPlaying() {
        runOnUiThread(runEnableButtons);
        audioPlayer.startPlaying(controller.getCurrentTempRecordingFilePath());
    }

    @Override
    public void endActivityAction() {
        finish();
    }
}
