package com.example.oblig3_dat153;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.oblig3_dat153.ui.gallery.GalleryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GalleryActivity extends AppCompatActivity {

    private AppDatabase db;
    private PhotoDAO dao;
    private GalleryAdapter adapter;
    private List<PhotoEntry> galleryItems;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // DB and dao instantiate
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
        dao = db.photoDAO();

         // populateDb();

        setupActivityResultLauncher();
        setupRecyclerViewAndAdapter();
        setupExitButton();
        setupAddButton();
        setupAzSort();
        setupZaSort();

        observeAndUpdatePhotoEntries();
    }

    /* - - - - - - - - - - - - ImagePicker and saver - - - - - - - - - - - - - - - - - - - */
    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    public void insertPhotoEntry(String name) {
        PhotoEntry entry = new PhotoEntry(name, selectedImageUri.toString());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            dao.insert(entry);
            adapter.notifyDataSetChanged();
        });
    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image Name");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            String imageName = input.getText().toString();
            if (!imageName.isEmpty()) {
                insertPhotoEntry(imageName);
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    /* - - - - - - - - - - - - OBSERVER - - - - - - - - - - - - - - - - - - - */

    private void observeAndUpdatePhotoEntries() {
        dao.getAll().observe(this, new Observer<List<PhotoEntry>>() {
            @Override
            public void onChanged(List<PhotoEntry> photoEntries) {
                // Sets current changes to object variable, for sorting functionality
                galleryItems = photoEntries;

                // Update the adapter's dataset
                adapter.setGalleryItems(photoEntries);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /* - - - - - - - - - - - - SETUP - - - - - - - - - - - - - - - - - - - */

    private void setupActivityResultLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Resultatet fra imagePicker
                        selectedImageUri = result.getData().getData();

                        // Sender URI til input, for å hente navn til bilde
                        showNameInputDialog();
                    }
                }
        );
    }

    private void setupRecyclerViewAndAdapter() {
        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        adapter = new GalleryAdapter(this, new ArrayList<>(), this::onDeleteItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setupExitButton() {
        Button exitButton = findViewById(R.id.button_save_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, MainActivity.class));
            }
        });
    }

    public void setupAddButton() {
        Button addButton = findViewById(R.id.button_add_entry);
        addButton.setOnClickListener(v -> openImagePicker());
    }

    public void setupAzSort() {
        Button az = findViewById(R.id.button_sort_za);
        az.setOnClickListener(v -> {
            Collections.sort(galleryItems, new Comparator<PhotoEntry>() {
                @Override
                public int compare(PhotoEntry item1, PhotoEntry item2) {
                    // Compare the names of the gallery items alphabetically
                    return item1.getName().compareTo(item2.getName());
                }
            });

            adapter.setGalleryItems(galleryItems);
            adapter.notifyDataSetChanged();
        });
    }

    public void setupZaSort() {
        Button za = findViewById(R.id.button_sort_az);
        za.setOnClickListener(v -> {
            Collections.sort(galleryItems, new Comparator<PhotoEntry>() {
                @Override
                public int compare(PhotoEntry item1, PhotoEntry item2) {
                    // Compare the names of the gallery items alphabetically
                    return item2.getName().compareTo(item1.getName());
                }
            });

            adapter.setGalleryItems(galleryItems);
            adapter.notifyDataSetChanged();
        });
    }

    /* - - - - - - - - - - - - OnClick FUNCTIONS- - - - - - - - - - - - - - - - - - - */

    private void onDeleteItem(PhotoEntry image) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            dao.delete(image);
        });
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
