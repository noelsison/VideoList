package com.example.noel.videolist.utils;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Noel on 3/20/2017.
 */

public class ToastMatcher extends TypeSafeMatcher<Root> {
    private static String stringDescription = "is toast";

    @Override
    protected boolean matchesSafely(Root item) {
        int type = item.getWindowLayoutParams().get().type;
        if(type == WindowManager.LayoutParams.TYPE_TOAST) {
            IBinder windowToken = item.getDecorView().getWindowToken();
            IBinder appToken = item.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(stringDescription);
    }
}
