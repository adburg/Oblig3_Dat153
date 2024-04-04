package com.example.oblig3_dat153.quiz;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.oblig3_dat153.AppDatabase;
import com.example.oblig3_dat153.PhotoDAO;
import com.example.oblig3_dat153.PhotoEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            return entry;
        }
        return null;
    }

    public List<String> getQuestionAlternatives() {
        List<PhotoEntry> questions = this.galleryItems.getValue();
        int correctAnswerIndex = this.index.getValue() - 1;

        Set<String> alternatives = new HashSet<>();
        alternatives.add(questions.get(correctAnswerIndex).getName());

        while(alternatives.size() < 3) {
            int randomIndex = (int) Math.floor(Math.random() * questions.size());
            alternatives.add(questions.get(randomIndex).getName());
        }

        List<String> returnValues = new ArrayList<>(alternatives);
        Collections.shuffle(returnValues);

        return returnValues;
    }

    public void takeUserAnswer(String answer) {
        int correctAnswerIndex = this.index.getValue() - 1;

        if(answer.equals(galleryItems.getValue().get(correctAnswerIndex).getName())) {
            incrementScore();
        }

        incrementTotalAttempts();
    }
}
