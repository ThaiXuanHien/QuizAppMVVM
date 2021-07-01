package com.hienthai.quizappmvvm;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference quizRef = firebaseFirestore.collection("QuizList");

    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getQuizData() {
        quizRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(Quiz.class));
            } else {
                onFirestoreTaskComplete.onError(task.getException());
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(List<Quiz> quizListModelsList);

        void onError(Exception e);
    }
}
