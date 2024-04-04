package com.example.oblig3_dat153.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.oblig3_dat153.AppDatabase;
import com.example.oblig3_dat153.MainActivity;
import com.example.oblig3_dat153.PhotoDAO;
import com.example.oblig3_dat153.PhotoEntry;
import com.example.oblig3_dat153.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel viewModel;
    private PhotoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // DB and dao instantiate
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
        dao = db.photoDAO();

        populateQuizViewModel();
        setupHomeButton();
        setupAlternativeButtons();
    }

    public void populateQuizViewModel() {
        // Creates the ViewModel for handling state
        dao.getAll().observe(this, new Observer<List<PhotoEntry>>() {
            @Override
            public void onChanged(List<PhotoEntry> photoEntries) {
                dao.getAll().removeObserver(this);
                viewModel = new QuizViewModel(new MutableLiveData<>(photoEntries));

                System.out.println(viewModel.getQuestion());
                System.out.println(viewModel.getQuestionAlternatives());
            }
        });
    }

    public void setupHomeButton() {
        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> startActivity(new Intent(QuizActivity.this, MainActivity.class)));
    }

    public void setupAlternativeButtons() {
        Button buttonOne = findViewById(R.id.button_option1);
        Button buttonTwo = findViewById(R.id.button_option1);
        Button buttonThree = findViewById(R.id.button_option1);

        
    }

    /* - - - - - - - - - - - - ONLY TEST - - - - - - - - - - - - - - - - - - - */

    private void populateDb() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                PhotoEntry entry1 = new PhotoEntry("Petter Pil", "https://plan-b.no/content/uploads/2021/06/Petter-P.jpg");
                PhotoEntry entry2 = new PhotoEntry("Vendela <3", "https://www.dagbladet.no/images/79604169.jpg?imageId=79604169&panow=99.893674641148&panoh=22.612709265176&panox=0&panoy=22.044728434505&heightw=51.301588571429&heighth=55.218856060606&heightx=21.142857142857&heighty=17.424242424242&width=1200&height=1200");
                PhotoEntry entry3 = new PhotoEntry("Osmaniiii", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTMxyu3_vCnw_7bTUn_Z0Ztdm7Mlg6fyeS_gyP4xM4Aw&s");

                dao.insert(entry1);
                dao.insert(entry2);
                dao.insert(entry3);
            }
        });
    }
}
