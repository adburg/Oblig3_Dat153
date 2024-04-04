package com.example.oblig3_dat153;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oblig3_dat153.gallery.GalleryActivity;
import com.example.oblig3_dat153.quiz.QuizActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button galleryActivityBtn = findViewById(R.id.galleryactivity);
        Button quizActivityBtn = findViewById(R.id.quizactivity);

        galleryActivityBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GalleryActivity.class)));
        quizActivityBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizActivity.class)));
    }
}