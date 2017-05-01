package com.example.noel.videolist.activity.interview.practice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.audio.AudioPlayer;
import com.example.noel.videolist.activity.audio.BaseInterviewActivity;
import com.example.noel.videolist.activity.audio.TTSPlayer;
import com.example.noel.videolist.data.VideoListContract.InterviewQuestion;

import java.util.Locale;

/**
 * Created by Noel on 4/19/2017.
 */

public class InterviewPracticeActivity extends BaseInterviewActivity implements InterviewButtonsFragment.InterviewButtonsListener {
    private static final String TAG = InterviewPracticeActivity.class.getName();

    public static final String INTENT_EXTRA_CONTENT_ID = "CONTENT_ID";
    public static final String INTENT_EXTRA_CONTENT_TITLE = "CONTENT_TITLE";

    private static final int MAX_AMPLITUDE = 30000;

    // TODO: Create a controller class that handles the interview practice gameplay

    TTSPlayer ttsPlayer;

    int contentId;
    String title;

    TextView textViewQuestion;
    ProgressBar progressBarRecordVolume;
    InterviewButtonsFragment buttonsFragment;

    Runnable runEnableButtons;

    InterviewPracticeController controller;

    AudioPlayer resultAudioPlayer;

    boolean isResultMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_practice);

        contentId = getIntent().getIntExtra(INTENT_EXTRA_CONTENT_ID, 0);
        title = getIntent().getStringExtra(INTENT_EXTRA_CONTENT_TITLE);
        getSupportActionBar().setTitle(title);

        // TODO: Check if device has TTS
        ttsPlayer = new TTSPlayer(getApplicationContext(), this);

        resultAudioPlayer = new AudioPlayer(null);

        // View variables
        textViewQuestion = (TextView) findViewById(R.id.interview_practice_tv_question);

        progressBarRecordVolume = (ProgressBar) findViewById(R.id.interview_practice_pb_recording_level);
        progressBarRecordVolume.setMax(MAX_AMPLITUDE);

        buttonsFragment = (InterviewButtonsFragment) getFragmentManager().findFragmentById(R.id.interview_practice_fragment_buttons_holder);

        initRunnables();

        controller = new InterviewPracticeController(this);
        controller.initQuestionsForActivity(contentId);
    }

    protected void initRunnables() {
        runEnableButtons = new Runnable() {
            @Override
            public void run() {
                buttonsFragment.setButtonsEnabled(true);
            }
        };
    }

    protected void playQuestion(InterviewQuestion interviewQuestion) {
        if (isQuestionPlaying()) {
            return;
        }
        buttonsFragment.setButtonsEnabled(false);
        // TODO: If there are errors playing the sound file, fallback to TTS
        if (interviewQuestion.getAudioFilePath() != null) {
            audioPlayer.startPlaying(interviewQuestion.getAudioFilePath());
        } else {
            ttsPlayer.speak(interviewQuestion.getText());
        }
    }

    private boolean isQuestionPlaying() {
        return audioPlayer.isPlaying() && ttsPlayer.isPlaying();
    }

    @Override
    public void onFinishPlaying() {
        runOnUiThread(runEnableButtons);
        if (!isResultMode) {
            audioRecorder.startRecording(controller.getCurrentTempRecordingFilePath());
        } else {
            resultAudioPlayer.startPlaying(controller.getCurrentTempRecordingFilePath());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsPlayer.dispose();
    }

    @Override
    public void updateAmplitude(int amplitude) {
        progressBarRecordVolume.setProgress(amplitude);
    }

    @Override
    public void replayButtonClicked() {
        audioPlayer.stopPlaying();
        resultAudioPlayer.stopPlaying();
        audioRecorder.stopRecording();
        playQuestion(controller.getCurrentQuestion());
    }

    @Override
    public void nextButtonClicked() {
        resultAudioPlayer.stopPlaying();
        audioRecorder.stopRecording();
        controller.setNextQuestion();
    }

    public void setQuestion(InterviewQuestion interviewQuestion) {
        if (controller.isOnLastQuestion()) {
            buttonsFragment.setNextToFinish();
        }
        textViewQuestion.setText(interviewQuestion.getText());
        playQuestion(interviewQuestion);
    }

    public void endActivityAction() {
        if (!isResultMode) {
            isResultMode = true;
            LinearLayout recordVisualizerHolder = (LinearLayout) findViewById(R.id.activity_interview_practice_ll_record_visualizer_holder);
            recordVisualizerHolder.setVisibility(View.GONE);
            controller.restart();
            controller.setNextQuestion();
        } else {
            finish();
        }
    }
}
