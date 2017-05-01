package com.example.noel.videolist.activity.interview.practice;

import android.content.AsyncQueryHandler;
import android.database.Cursor;
import android.net.Uri;

import com.example.noel.videolist.data.VideoListContentProvider;
import com.example.noel.videolist.data.VideoListContract.InterviewQuestion;

import java.util.ArrayList;

/**
 * Created by Noel on 5/1/2017.
 */

public class InterviewPracticeController {

    private AsyncQueryHandler asyncQueryHandler;
    private InterviewPracticeActivity activity;
    private ArrayList<InterviewQuestion> questionList;
    private ArrayList<String> tempRecordingFileList;
    private int questionIndex;

    private boolean isInitialized = false;

    InterviewPracticeController(InterviewPracticeActivity activity) {
        this.activity = activity;
        questionList = new ArrayList<>();
        tempRecordingFileList = new ArrayList<>();
        questionIndex = -1;
    }

    public void initQuestionsForActivity(int contentId) {
        asyncQueryHandler = new AsyncQueryHandler(activity.getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                while (cursor.moveToNext()) {
                    InterviewQuestion interviewQuestion = new InterviewQuestion(cursor);
                    questionList.add(interviewQuestion);
                    tempRecordingFileList.add(makeRecordingFilename(interviewQuestion));
                }
                isInitialized = true;
                setNextQuestion();
            }
        };
        asyncQueryHandler.startQuery(
                1,
                null,
                Uri.parse(VideoListContentProvider.INTERVIEW_QUESTION_URI + "/" + Integer.toString(contentId)),
                null,
                null,
                null,
                InterviewQuestion.COLUMN_SEQ_NUM
        );
    }

    private String makeRecordingFilename(InterviewQuestion interviewQuestion) {
        return activity.getExternalCacheDir().getAbsolutePath() + "/" + interviewQuestion.getContentId() + "-" + interviewQuestion.getId() + ".3gp";
    }

    public void setNextQuestion() {
        questionIndex += 1;
        if (questionIndex < questionList.size()) {
            activity.setQuestion(questionList.get(questionIndex));
        } else {
            endPractice();
        }
    }

    public InterviewQuestion getCurrentQuestion() {
        return questionList.get(questionIndex);
    }

    public String getCurrentTempRecordingFilePath() {
        return tempRecordingFileList.get(questionIndex);
    }

    public boolean isOnLastQuestion() {
        return questionIndex == questionList.size() - 1;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void endPractice() {
        activity.endActivityAction();
    }

    public void restart() {
        questionIndex = -1;
    }
}
