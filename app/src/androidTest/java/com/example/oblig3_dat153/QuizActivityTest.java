package com.example.oblig3_dat153;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.oblig3_dat153.quiz.QuizActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityTest {
    private PhotoDAO dao;
    private ActivityScenario<QuizActivity> scenario;

       // @Rule
        // public ActivityScenarioRule<QuizActivity> activityRule = new ActivityScenarioRule<>(QuizActivity.class);

        @Before
        public void setup() {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
            dao = db.photoDAO();
            populateDb();

            Intents.init();
            scenario = ActivityScenario.launch(QuizActivity.class);
        }

        private void populateDb() {
            final CountDownLatch latch = new CountDownLatch(1); // Initialize CountDownLatch

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Your database operations
                    PhotoEntry entry1 = new PhotoEntry("Dennis", "android.resource://com.example.oblig3_dat153/" + R.drawable.elefant);
                    PhotoEntry entry2 = new PhotoEntry("Car", "android.resource://com.example.oblig3_dat153/" + R.drawable.bil);
                    PhotoEntry entry3 = new PhotoEntry("Bedroom", "android.resource://com.example.oblig3_dat153/" + R.drawable.bedroom);

                    dao.insert(entry1);
                    dao.insert(entry2);
                    dao.insert(entry3);

                    latch.countDown(); // Signal that database operations are complete
                }
            });
        }
/*
        @Test
        public void testCorrectAnswer() {

            // Click on the option button for the correct answer
            onView(withId(R.id.button_option1)).perform(click());

            // Check if the score is updated correctly
            onView(withId(R.id.textView_score)).check(matches(withText("Score: 1 / 1")));

        }

        @Test
        public void testWrongAnswer() throws InterruptedException {

            // Click on the option button for the correct answer
            onView(withId(R.id.button_option2)).perform(click());
            try {
                // Check if the score is updated correctly
                onView(withId(R.id.textView_score)).check(matches(withText("Score: 0 / 1")));
            } catch(Exception e) {
                System.err.println("Finner ikke moren din");
            }
        }

        @After
        public void tearDown() {
            Intents.release();
        }
    */

    @Test
    public void testCorrectAnswer() {
        // Perform UI interactions with the activity through the scenario

        AtomicInteger correctIdx = new AtomicInteger(-1);
        scenario.onActivity(activity -> {

            // activity.populateQuizViewModel();
            List<String> alternatives = activity.getButtonValues();
            String correct = activity.getViewModel().getCorrectAnswer();


            if (correct.equals(alternatives.get(0))) {
                // Click on the option button for the correct answer
                correctIdx.set(0);

            } else if (correct.equals(alternatives.get(1))) {
                correctIdx.set(1);
            } else {
                correctIdx.set(2);
            }
        });

        if (correctIdx.intValue() == 0) {
            // Click on the option button for the correct answer
            onView(withId(R.id.button_option1)).perform(click());
        } else if (correctIdx.intValue() == 1) {
            onView(withId(R.id.button_option2)).perform(click());
        } else {
            onView(withId(R.id.button_option3)).perform(click());
        }
            // Check if the score is updated correctly
            onView(withId(R.id.textView_score)).check(matches(withText("Score: 1 / 1")));
    }

    @Test
    public void testWrongAnswer() {
        // Perform UI interactions with the activity through the scenario
        scenario.onActivity(activity -> {
            // Click on the option button for the incorrect answer
            activity.findViewById(R.id.button_option2).performClick();
            // Check if the score is updated correctly
            Espresso.onView(activity.findViewById(R.id.textView_score)).check(matches(withText("Score: 0 / 1")));
        });
    }

    @After
    public void tearDown() {
        // Close the activity scenario
        scenario.close();

        Intents.release();
    }
}

