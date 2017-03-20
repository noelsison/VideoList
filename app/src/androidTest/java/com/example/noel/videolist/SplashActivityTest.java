package com.example.noel.videolist;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.noel.videolist.R;
import com.example.noel.videolist.splash.SplashActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Noel on 3/20/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityRule = new ActivityTestRule<>(
            SplashActivity.class);

    @Test
    public void testSplashActivity() throws Exception {
        /*
        When splash/personalization screen is launched, the user should see the following:
        1. Text greeting: "Welcome to Tick Tock Tech Talk!\n First, what is your name?"
        2. Input field with placeholder (hint in android) "Name"
        3. Button with text "Start"
         */

        /*
        When user clicks "Start" button while the input field for "Name" is empty (no text or just
        spaces), display a toast with the message "Please enter your name".
         */

        /*
        When user enters a non-empty name in the input field  and clicks "Start" button, save the
        following shared preferences with values:
        saved_first_time: false
        saved_name: <what was placed in the input field>
         */

        /*
        Then launch the MainActivity
         */

    }
}
