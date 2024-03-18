package com.example.oblig3_dat153;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
    private final PhotoDAO dao = db.photoDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleGetAll();
        populateDb();

    }

    protected void handleGetAll() {
        Button btn = (Button) findViewById(R.id.test_button);

        btn.setOnClickListener(v -> {
            List<PhotoEntry> list = dao.getAll();
            list.forEach(System.out::println);
        });
    }

    private void populateDb() {
        PhotoEntry entry1 = new PhotoEntry("Entry1", "url1");
        PhotoEntry entry2 = new PhotoEntry("Entry2", "url2");
        PhotoEntry entry3 = new PhotoEntry("Entry3", "url3");

        dao.insert(entry1);
        dao.insert(entry2);
        dao.insert(entry3);
    }



}