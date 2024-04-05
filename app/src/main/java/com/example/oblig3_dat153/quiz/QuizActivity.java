package com.example.oblig3_dat153.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.oblig3_dat153.AppDatabase;
import com.example.oblig3_dat153.MainActivity;
import com.example.oblig3_dat153.PhotoDAO;
import com.example.oblig3_dat153.PhotoEntry;
import com.example.oblig3_dat153.R;
import com.example.oblig3_dat153.gallery.GalleryActivity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    private QuizViewModel viewModel;
    private PhotoDAO dao;
    private Button [] optionButtons;
    private TextView scoreTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // DB and dao instantiate
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
        dao = db.photoDAO();

        checkAndPopulateDbIfNeeded();
    }
    public void populateQuizViewModel() {
        // Creates the ViewModel for handling state
        dao.getAll().observe(this, new Observer<List<PhotoEntry>>() {
            @Override
            public void onChanged(List<PhotoEntry> photoEntries) {
                //Collections.shuffle(photoEntries);
                viewModel = new QuizViewModel(new MutableLiveData<>(photoEntries));
                setupHomeButton();
                setupAlternativeButtons();
                setupScoreAndImageView();
                startQuiz();
            }
        });
    }

    public void setupHomeButton() {
        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        });
    }

    public void setupScoreAndImageView(){
        scoreTextView = findViewById(R.id.textView_score);
        imageView = findViewById(R.id.imageView_quiz);
    }

    public void setupAlternativeButtons() {
        optionButtons = new Button[3];

        optionButtons[0] = findViewById(R.id.button_option1);
        optionButtons[1] = findViewById(R.id.button_option2);
        optionButtons[2] = findViewById(R.id.button_option3);

        for (Button button : optionButtons) {
            button.setOnClickListener(this);
        }
    }

    public void startQuiz(){
        PhotoEntry entry = viewModel.getQuestion();
        //if quiz is finished
        if(entry == null){
            System.out.println("MORDI ER NULL");
            showFinishedDialog();
            return;
        }
        imageView.setImageURI(Uri.parse(entry.getUrl()));
        List<String> options = viewModel.getQuestionAlternatives();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
        }
    }

    private void updateScore() {
        String scoreText = "Score: " + viewModel.getScore() + " / " + viewModel.getTotalAttempts();
        scoreTextView.setText(scoreText);
    }

    // Triggered by this
    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        boolean correct = viewModel.takeUserAnswer(clickedButton.getText().toString());
        if(correct){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
        updateScore();
        startQuiz();
    }

    private void showFinishedDialog() {

        if(viewModel.getTotalAttempts() == 0)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations, you finished the quiz");

        builder.setMessage("You got: " + viewModel.getScore()+ " / " + viewModel.getTotalAttempts() + " correct answers.");
        builder.setNeutralButton("Go home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Handle neutral button click
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        });

        builder.show();
    }

    private void checkAndPopulateDbIfNeeded() {
        dao.getAll().observe(this, new Observer<List<PhotoEntry>>() {
            @Override
            public void onChanged(List<PhotoEntry> photoEntries) {
                if (photoEntries.size() < 3) {
                    populateDb(); // If less than three items, populate the database
                } else {
                    populateQuizViewModel(); // Otherwise, directly populate the quiz view model
                }
            }
        });
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

        try {
            latch.await(); // Wait for database operations to complete
            runOnUiThread(new Runnable() { // Ensure UI operations are on the main thread
                @Override
                public void run() {
                    populateQuizViewModel(); // Now safe to call
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}