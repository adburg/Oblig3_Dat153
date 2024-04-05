package com.example.oblig3_dat153;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.oblig3_dat153.gallery.GalleryActivity;
import com.example.oblig3_dat153.quiz.QuizActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GalleryActivityTest {
    @Rule
    public ActivityScenarioRule<GalleryActivity> activityRule = new ActivityScenarioRule<>(GalleryActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @Test
    public void testAddImage(){

    }
}
