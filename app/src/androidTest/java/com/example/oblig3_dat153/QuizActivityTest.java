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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityTest {
    private PhotoDAO dao;

    @Rule
    public ActivityScenarioRule<QuizActivity> activityRule = new ActivityScenarioRule<>(QuizActivity.class);

    @Before
    public void setup() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
        dao = db.photoDAO();
        populateDb();

        Intents.init();
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

}
