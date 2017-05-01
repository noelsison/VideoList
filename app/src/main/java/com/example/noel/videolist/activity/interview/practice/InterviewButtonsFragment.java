package com.example.noel.videolist.activity.interview.practice;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noel.videolist.R;

/**
 * Created by Noel on 5/1/2017.
 */

public class InterviewButtonsFragment extends Fragment {
    private static final String TAG = InterviewButtonsFragment.class.getName();

    private InterviewButtonsListener interviewButtonsListener;
    private Button buttonReplay;
    private Button buttonNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interview_button_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButtons();
        try {
            interviewButtonsListener = (InterviewButtonsListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Instantiated in the wrong activity.");
        }
    }

    private void initButtons() {
        buttonReplay = (Button) getView().findViewById(R.id.interview_practice_b_replay);
        buttonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewButtonsListener.replayButtonClicked();
            }
        });
        buttonNext = (Button) getView().findViewById(R.id.interview_practice_b_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewButtonsListener.nextButtonClicked();
            }
        });

        setButtonsEnabled(false);
    }

    public void setButtonsEnabled(boolean isEnabled) {
        buttonReplay.setEnabled(isEnabled);
        buttonNext.setEnabled(isEnabled);
    }

    public void setNextToFinish() {
        buttonNext.setText(getActivity().getString(R.string.interview_practice_button_finish));
    }

    interface InterviewButtonsListener {
        void replayButtonClicked();
        void nextButtonClicked();
    }
}
