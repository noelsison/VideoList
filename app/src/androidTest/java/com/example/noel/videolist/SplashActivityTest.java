package com.example.noel.videolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.noel.videolist.splash.SplashActivity;
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
        When splash/personalization screen is launched, the user should see the following:
        1. Text greeting: "Welcome to Tick Tock Tech Talk!\n First, what is your name?"
        2. Input field with placeholder (hint in android) "Name"
        3. Button with text "Start"
         */

        onView(withId(R.id.splash_tv_greeting)).check(matches(withText(R.string.splash_greeting)));
        onView(withId(R.id.splash_et_name)).check(matches(withHint(R.string.splash_hint_name)));
        onView(withId(R.id.splash_et_name)).check(matches(withText("")));
        onView(withId(R.id.splash_b_start)).check(matches(withText(R.string.splash_button_start)));
    }

    @Test
    public void testEmptynameInput() {
        /*
        When user clicks "Start" button while the input field for "Name" is empty,
        display a toast with the message "Please enter your name".
         */

        final String stringEmpty = "";

        onView(withId(R.id.splash_et_name)).perform(typeText(stringEmpty), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());
        onView(withText(R.string.splash_error_name_empty)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void testSpacesOnlyNameInput() {
        /*
        When user clicks "Start" button while the input field for "Name" are just spaces,
        display a toast with the message "Please enter your name".
         */

        final String stringSpaces = "   ";

        onView(withId(R.id.splash_et_name)).perform(typeText(stringSpaces), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());
        onView(withText(R.string.splash_error_name_empty)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void testCorrectNameInput() {
        /*
        When user enters a non-empty name in the input field  and clicks "Start" button, save the
        following shared preferences with values:
        saved_first_time: false
        saved_name: <what was placed in the input field>
         */
        final String stringName = "Name";

        onView(withId(R.id.splash_et_name)).perform(typeText(stringName), closeSoftKeyboard());
        onView(withId(R.id.splash_b_start)).perform(click());

        SplashActivity activity = activityRule.getActivity();
        SharedPreferences sharedPreferences = activityRule.getActivity().getSharedPreferences(
                activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean savedFirstLaunch = sharedPreferences.getBoolean(activity.getString(R.string.saved_first_time), true);
        assertFalse(savedFirstLaunch);

        String savedName = sharedPreferences.getString(activity.getString(R.string.saved_name), null);
        assertTrue(stringName.equals(savedName));

        /*
        Then launch the MainActivity
         */

        intended(hasComponent(MainActivity.class.getName()));
    }
}
