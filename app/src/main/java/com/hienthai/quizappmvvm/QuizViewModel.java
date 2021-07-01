package com.hienthai.quizappmvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class QuizViewModel extends ViewModel implements FirebaseRepository.OnFirestoreTaskComplete {

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);
    private MutableLiveData<List<Quiz>> quizListLiveData = new MutableLiveData<>();

    public LiveData<List<Quiz>> getQuizListLiveData() {
        return quizListLiveData;
    }


    public QuizViewModel() {
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(List<Quiz> quizListModelsList) {
        quizListLiveData.setValue(quizListModelsList);
    }

    @Override
    public void onError(Exception e) {

    }
}
