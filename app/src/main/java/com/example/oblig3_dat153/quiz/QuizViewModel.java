package com.example.oblig3_dat153.quiz;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.oblig3_dat153.AppDatabase;
import com.example.oblig3_dat153.PhotoDAO;
import com.example.oblig3_dat153.PhotoEntry;

import java.util.List;

public class QuizViewModel extends ViewModel {
    private MutableLiveData<List<PhotoEntry>> galleryItems;
    private MutableLiveData<Integer> score;
    private MutableLiveData<Integer> totalAttempts;
    private MutableLiveData<Integer> index;

    public QuizViewModel (List<PhotoEntry> galleryItems) {
        this.galleryItems = new MutableLiveData<>(galleryItems);
        this.score = new MutableLiveData<>(0);
        this.totalAttempts = new MutableLiveData<>(0);
        this.index = new MutableLiveData<>(0);
    }

    public void incrementScore(){
        this.score.setValue(score.getValue() + 1);
    }

    public void incrementTotalAttempts(){
        this.totalAttempts.setValue(totalAttempts.getValue() + 1);
    }

    public void incrementIndex(){
        this.index.setValue(index.getValue() + 1);
    }

    public PhotoEntry getQuestion(){
        if (index.getValue() < galleryItems.getValue().size()){
            PhotoEntry entry = galleryItems.getValue().get(index.getValue());
            incrementIndex();
            return entry;
        }
        return null;
    }

}
