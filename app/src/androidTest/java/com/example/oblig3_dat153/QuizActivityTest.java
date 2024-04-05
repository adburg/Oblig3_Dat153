package com.example.oblig3_dat153;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import androidx.room.Room;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.oblig3_dat153.quiz.QuizActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityTest {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityRule = new ActivityScenarioRule<>(QuizActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @Test
    public void testCorrectAnswer() {

        // Click on the option button for the correct answer
        onView(withId(R.id.button_option1)).perform(click());

        // Check if the score is updated correctly
        onView(withId(R.id.textView_score)).check(matches(withText("Score: 1 / 1")));

    }

    @Test
    public void testWrongAnswer() {

        // Click on the option button for the correct answer
        onView(withId(R.id.button_option2)).perform(click());

        // Check if the score is updated correctly
        onView(withId(R.id.textView_score)).check(matches(withText("Score: 0 / 1")));

    }

    @After
    public void tearDown() {
        Intents.release();
    }

}
