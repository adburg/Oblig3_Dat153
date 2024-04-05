package com.example.oblig3_dat153.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.oblig3_dat153.AppDatabase;
import com.example.oblig3_dat153.MainActivity;
import com.example.oblig3_dat153.PhotoDAO;
import com.example.oblig3_dat153.PhotoEntry;
import com.example.oblig3_dat153.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GalleryActivity extends AppCompatActivity {

    private PhotoDAO dao;
    public GalleryAdapter adapter; // adapter for feeding data to our gallery_item
    private List<PhotoEntry> galleryItems; // List of all items in database
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // DB and dao instantiate
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "photo-entry").build();
        dao = db.photoDAO();

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
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    public void insertPhotoEntry(String name, String uri) {
        PhotoEntry entry = new PhotoEntry(name, uri);
        // Uses executor for handling the insert in another thread than the main
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            dao.insert(entry);
        });
    }

    // Creates a Alert programmatically without xml file, and displays it
    private void showNameInputDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image Name");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            String imageName = input.getText().toString();
            if (!imageName.isEmpty()) {
                insertPhotoEntry(imageName, imageUri.toString());
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }
    /* - - - - - - - - - - - - OBSERVER - - - - - - - - - - - - - - - - - - - */

    // Observes the database for updates, the onChange is triggered on updates,
    // and we pass the updated data to our adapter
    private void observeAndUpdatePhotoEntries() {
        dao.getAll().observe(this, new Observer<List<PhotoEntry>>() {
            @Override
            public void onChanged(List<PhotoEntry> photoEntries) {
                // Sets current changes to object
                galleryItems = photoEntries;

                // Update the adapter's dataset
                adapter.setGalleryItems(photoEntries);
            }
        });
    }

    /* - - - - - - - - - - - - SETUP - - - - - - - - - - - - - - - - - - - */

    // Sets up a handler for taking in images, check that the result is good, then it takes out image
    private void setupActivityResultLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Result from imagePicker
                        Uri selectedImageUri = result.getData().getData();

                        // Sends the URI to the input, so we can add a name
                        showNameInputDialog(selectedImageUri);
                    }
                }
        );
    }

    // Gets the Recycler view from the Gallery XML file, creates and sets the adapter
    private void setupRecyclerViewAndAdapter() {
        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        adapter = new GalleryAdapter(this, new ArrayList<>(), this::onDeleteItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Sets up the exit button
    public void setupExitButton() {
        Button exitButton = findViewById(R.id.button_save_exit);
        exitButton.setOnClickListener(v -> startActivity(new Intent(GalleryActivity.this, MainActivity.class)));
    }

    // Sets up the add button
    public void setupAddButton() {
        Button addButton = findViewById(R.id.button_add_entry);
        addButton.setOnClickListener(v -> openImagePicker());
    }

    // Sets up the A-Z sorting
    public void setupAzSort() {
        Button az = findViewById(R.id.button_sort_az);
        az.setOnClickListener(v -> {
            Collections.sort(galleryItems, Comparator.comparing(PhotoEntry::getName));
            adapter.setGalleryItems(galleryItems);
        });
    }

    // Sets up the Z-A sorting
    public void setupZaSort() {
        Button za = findViewById(R.id.button_sort_za);
        za.setOnClickListener(v -> {
            Collections.sort(galleryItems, (item1, item2) -> item2.getName().compareTo(item1.getName()));
            adapter.setGalleryItems(galleryItems);
        });
    }

    /* - - - - - - - - - - - - OnClick FUNCTIONS- - - - - - - - - - - - - - - - - - - */

    // Deletes a PhotoEntry from our database
    private void onDeleteItem(PhotoEntry image) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            dao.delete(image);
        });
    }
}
