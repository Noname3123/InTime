package com.jakupovic.intime.fragments;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;



import com.jakupovic.intime.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * auto generated code which tests the stopwatch userflow, made using the record espresso test functionality
 * */
public class StopwatchUserFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    //vals to compare
    String currentStopwatchValue="";
    String currentLapValues="";

    @Test
    public void stopwatchUserFlowTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html




        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction tabView = onView(
                allOf(withContentDescription("Stopwatch"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tabs),
                                        0),
                                2),
                        isDisplayed()));
        tabView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.buttonStart), withText("Start"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                4),
                        isDisplayed()));
        materialButton.perform(click()); //press start

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonLap), withText("Lap"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                5),
                        isDisplayed()));
        //take the stopwatch time
        UpdateStopwatchTimeRef();
        materialButton2.perform(click()); //press lap



        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.buttonLap), withText("Lap"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                5),
                        isDisplayed()));
        //take the stopwatch time
        UpdateStopwatchTimeRef();
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.buttonLap), withText("Lap"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                5),
                        isDisplayed()));
        //take the stopwatch time
        UpdateStopwatchTimeRef();

        materialButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));


        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.buttonLap), withText("Lap"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                5),
                        isDisplayed()));
        //take the stopwatch time
        UpdateStopwatchTimeRef();

        materialButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));


        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.buttonStop), withText("Stop"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());
        //update with stopwatch data after stop was pressed
        UpdateStopwatchTimeRef();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //does the taken stopwatch value match two seconds after stop was tapped?
        onView(withId(R.id.StopwatchTime)).check(matches(withText(currentStopwatchValue)));

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.buttonStart), withText("Start"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                4),
                        isDisplayed()));
        //update with stopwatch data after stop was pressed
        UpdateStopwatchTimeRef();

        materialButton7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.buttonLap), withText("Lap"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                5),
                        isDisplayed()));
        //update with stopwatch data after stop was pressed
        UpdateStopwatchTimeRef();

        materialButton8.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //take the lap time
        UpdateLapStopwatchRef();
        //does the lap data contain te stopwatch before lap button was pushed
        assertTrue(currentLapValues.contains(currentStopwatchValue));


        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.buttonStop), withText("Stop"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                6),
                        isDisplayed()));
        materialButton9.perform(click());
        //update with stopwatch data after stop was pressed
        UpdateStopwatchTimeRef();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //does the taken stopwatch value match one second after stop was tapped?
        onView(withId(R.id.StopwatchTime)).check(matches(withText(currentStopwatchValue)));


        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.buttonStop), withText("Reset"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout2),
                                        withParent(withId(R.id.view_pager))),
                                6),
                        isDisplayed()));
        materialButton10.perform(click());
        //does the current stopwatch value inside UI match the reset value
        onView(withId(R.id.StopwatchTime)).check(matches(withText("00:00:00")));
        //does the taken lap data value inside UI match the reset value
        onView(withId(R.id.lapsData)).check(matches(withText("")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
/**
 * method updates the string currentStopwatchValue
 * */
    public void UpdateStopwatchTimeRef(){
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            currentStopwatchValue= (String) ((TextView)activity.findViewById(R.id.StopwatchTime)).getText();
        });
    }
    /**
     * method updates the string currentLapValues
     * */
    public void UpdateLapStopwatchRef(){
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            currentLapValues= (String) ((TextView)activity.findViewById(R.id.lapsData)).getText();
        });
    }

}
