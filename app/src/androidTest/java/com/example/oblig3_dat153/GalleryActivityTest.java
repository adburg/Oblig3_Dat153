package com.example.oblig3_dat153;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.oblig3_dat153.gallery.GalleryActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GalleryActivityTest {

    @Rule
    public IntentsTestRule<GalleryActivity> intentsTestRule = new IntentsTestRule<>(GalleryActivity.class);

    @Before
    public void stubImagePickerIntent() {
        // Stub the image picker Intent to simulate selecting an image
        Uri dummyImageUri = Uri.parse("android.resource://com.example.oblig3_dat153/" + R.drawable.elefant);
        Intent resultData = new Intent();
        resultData.setData(dummyImageUri);
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result);
    }

    @Test
    public void addAndDeleteImageFromUI() {
        intentsTestRule.getActivity().runOnUiThread(() ->
                intentsTestRule.getActivity().insertPhotoEntry("Test", "android.resource://com.example.oblig3_dat153/" + R.drawable.elefant)
        );

        // Wait a bit for the database operation to complete and UI to update.
        try {
            Thread.sleep(2000); // Not recommended for real tests
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Now perform the check
        int expectedPosition = 3; // Adjust based on your app's behavior
        onView(withId(R.id.gallery_recycler_view))
                .perform(scrollToPosition(expectedPosition))
                .check(matches(hasDescendant(withText("Test"))));

        // Hit delete button here:
        onView(withId(R.id.gallery_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, new ClickOnButtonViewAction(R.id.delete_button)));

        // Wait a bit for the deletion to be processed and for the UI to update.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify the entry "Dennis" has been deleted from the RecyclerView.
        onView(withId(R.id.gallery_recycler_view))
                .check(matches(not(hasDescendant(withText("Test")))));
    }

    // PAPPPPPPPAAAAAA
    public static class ClickOnButtonViewAction implements ViewAction {
        private final int buttonId;

        public ClickOnButtonViewAction(int buttonId) {
            this.buttonId = buttonId;
        }

        @Override
        public Matcher<View> getConstraints() {
            return null;
        }

        @Override
        public String getDescription() {
            return "Click on a child view with specified id.";
        }

        @Override
        public void perform(UiController uiController, View view) {
            View button = view.findViewById(buttonId);
            if (button != null) {
                button.performClick();
            }
        }
    }
}
