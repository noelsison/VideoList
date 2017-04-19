package com.example.noel.videolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.noel.videolist.activity.main.MainActivity;
import com.example.noel.videolist.activity.splash.SplashActivity;
import com.example.noel.videolist.utils.ToastMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Noel on 3/20/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public IntentsTestRule<SplashActivity> activityRule = new IntentsTestRule<>(
            SplashActivity.class);

    @Test
    public void testLaunchState() throws Exception {
        /*
        When user opens the splash/personalization activity
        Then the following is seen:
            1. Text greeting: "Welcome to Tick Tock Tech Talk!\n First, what is your name?"
            2. Input field with placeholder (hint in android) "Name" and empty text input
            3. Button with text "Start"
         */

        onView(withId(R.id.splash_tv_greeting)).check(matches(withText(R.string.splash_greeting)));
        onView(withId(R.id.splash_et_name)).check(matches(withHint(R.string.splash_hint_name)));
        onView(withId(R.id.splash_et_name)).check(matches(withText("")));
        onView(withId(R.id.splash_b_start)).check(matches(withText(R.string.splash_button_start)));

        /*
        When user leaves the name input field blank
        And user clicks "Start" button
        Then display a toast with the message "Please enter your name".
         */

        final String stringEmpty = "";

        onView(withId(R.id.splash_et_name)).perform(typeText(stringEmpty), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());
        onView(withText(R.string.splash_error_name_empty)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Hide the toast for the next test since we don't want to wait
        activityRule.getActivity().hideToast();
        boolean isToastHidden = false;
        try {
            onView(withText(R.string.splash_error_name_empty)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        } catch (NoMatchingRootException e) {
            isToastHidden = true;
        } finally {
            assertTrue("Toast is hidden?", isToastHidden);
        }

        /*
        When user types in spaces to the name input field
        And user clicks "Start" button
        Then display a toast with the message "Please enter your name".
         */

        final String stringSpaces = "   ";
        onView(withId(R.id.splash_et_name)).perform(typeText(stringSpaces), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());
        onView(withText(R.string.splash_error_name_empty)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        /*
        When user types in a non-empty name in the input field
        And user clicks "Start" button
        Then the following shared preferences with values should be saved:
            saved_first_time: false
            saved_name: <what was placed in the input field with leading/trailing spaces trimmed>
        And MainActivity should be launched
         */
        final String stringName = "   Name   ";
        final String expectedSavedName = stringName.trim();

        // Note that "user" did not clear previously input spaces and just appended a name
        onView(withId(R.id.splash_et_name)).perform(typeText(stringName), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());

        SplashActivity activity = activityRule.getActivity();
        SharedPreferences sharedPreferences = activityRule.getActivity().getSharedPreferences(
                activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean savedFirstLaunch = sharedPreferences.getBoolean(activity.getString(R.string.saved_first_time), true);
        assertFalse(savedFirstLaunch);

        String savedName = sharedPreferences.getString(activity.getString(R.string.saved_name), null);
        assertTrue(expectedSavedName + " =? " + savedName, savedName.equals(expectedSavedName));

        intended(hasComponent(MainActivity.class.getName()));
    }
}
