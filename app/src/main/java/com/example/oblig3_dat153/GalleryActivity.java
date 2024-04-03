package com.example.oblig3_dat153;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oblig3_dat153.ui.gallery.GalleryAdapter;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private GalleryAdapter adapter;
    private List<PhotoEntry> galleryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Henter data fra Database

        // Sets up the recycler view and its adapter
        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        adapter = new GalleryAdapter(this, galleryItems, this::onDeleteItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup
        exitButtonSetup();
    }

    private void onDeleteItem(PhotoEntry image) {
        galleryItems.remove(image);
        adapter.notifyDataSetChanged();
    }

    public void exitButtonSetup() {
        Button exitButton = findViewById(R.id.button_save_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, MainActivity.class));
            }
        });
    }
}
