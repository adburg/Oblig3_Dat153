package com.example.oblig3_dat153;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private PhotoDAO dao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button galleryActivityBtn = findViewById(R.id.galleryactivity);
        Button quizActivityBtn = findViewById(R.id.quizactivity);

        galleryActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GalleryActivity.class));
            }
        });

        quizActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,QuizActivity.class));
            }
        });
    }

    /*
    protected void handleGetAll() {
        Button btn = (Button) findViewById(R.id.test_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<List<PhotoEntry>> liveDataList = dao.getAll();
                liveDataList.observe(MainActivity.this, new Observer<List<PhotoEntry>>() {
                    // OnChange lytter til endringer i dataen, slik at vi kan manuelt oppdatere
                    // UI med den nye dataen.
                    @Override
                    public void onChanged(@Nullable List<PhotoEntry> photoEntries) {
                        for (PhotoEntry photoEntry : photoEntries) {
                            System.out.println(photoEntry.toString());
                        }
                    }
                });
            }
        });
    }*/

    private void populateDb() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                PhotoEntry entry1 = new PhotoEntry("Entry1", "url1");
                PhotoEntry entry2 = new PhotoEntry("Entry2", "url2");
                PhotoEntry entry3 = new PhotoEntry("Entry3", "url3");

                dao.insert(entry1);
                dao.insert(entry2);
                dao.insert(entry3);
            }
        });
    }
}