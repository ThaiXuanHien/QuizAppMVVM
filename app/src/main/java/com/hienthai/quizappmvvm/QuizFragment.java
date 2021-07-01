package com.hienthai.quizappmvvm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QuizFragment extends Fragment {

    private TextView txt_title_quiz, txt_quiz_question_number, txt_quiz_question_time, txt_quiz_question, txt_quiz_question_feedback;
    private ProgressBar pgb_quiz_question;
    private Button btn_quiz_option_one, btn_quiz_option_two, btn_quiz_option_three, btn_next_question;

    private FirebaseFirestore firebaseFirestore;
    private String quizId;

    private List<Question> questionList = new ArrayList<>();
    private List<Question> questionListToAnswer = new ArrayList<>();
    private long totalQuestions = 0L;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        anhXa(view);
        return view;
    }

    private void anhXa(View view) {
        txt_title_quiz = view.findViewById(R.id.txt_title_quiz);
        txt_quiz_question_number = view.findViewById(R.id.txt_quiz_question_number);
        txt_quiz_question_time = view.findViewById(R.id.txt_quiz_question_time);
        txt_quiz_question = view.findViewById(R.id.txt_quiz_question);
        pgb_quiz_question = view.findViewById(R.id.pgb_quiz_question);
        btn_quiz_option_one = view.findViewById(R.id.btn_quiz_option_one);
        btn_quiz_option_two = view.findViewById(R.id.btn_quiz_option_two);
        btn_quiz_option_three = view.findViewById(R.id.btn_quiz_option_three);
        txt_quiz_question_feedback = view.findViewById(R.id.txt_quiz_question_feedback);
        btn_next_question = view.findViewById(R.id.btn_next_question);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize
        firebaseFirestore = FirebaseFirestore.getInstance();

        // get data between frm
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizid();
        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();

        // get all question from quiz list
        firebaseFirestore.collection("QuizList")
                .document(quizId)
                .collection("Questions")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                // add all questions to list
                questionList = task.getResult().toObjects(Question.class);

                pickQuestions();

                loadDataToUI();

            } else {
                txt_title_quiz.setText("Error : " + task.getException().getMessage());
            }
        });

    }

    private void loadDataToUI() {

        txt_title_quiz.setText("Quiz Data Loaded");
        txt_quiz_question.setText("Load first question");
        enableOption();

        loadQuestion(1);
    }

    private void loadQuestion(int questNum) {
        //Set Question Number
        txt_quiz_question_number.setText(questNum + "");

        //Load Question Text
        txt_quiz_question.setText(questionListToAnswer.get(questNum).getQuestion());

        //Load Options
        btn_quiz_option_one.setText(questionListToAnswer.get(questNum).getOptionA());
        btn_quiz_option_two.setText(questionListToAnswer.get(questNum).getOptionB());
        btn_quiz_option_three.setText(questionListToAnswer.get(questNum).getOptionC());


    }

    private void enableOption() {
        //Show All Option Buttons
        btn_quiz_option_one.setVisibility(View.VISIBLE);
        btn_quiz_option_two.setVisibility(View.VISIBLE);
        btn_quiz_option_three.setVisibility(View.VISIBLE);

        //Enable Option Buttons
        btn_quiz_option_one.setEnabled(true);
        btn_quiz_option_two.setEnabled(true);
        btn_quiz_option_three.setEnabled(true);

        //Hide Feedback and next Button
        txt_quiz_question_feedback.setVisibility(View.INVISIBLE);
        btn_next_question.setVisibility(View.INVISIBLE);
        btn_next_question.setEnabled(false);
    }

    private void pickQuestions() {
        for (int i = 0; i < totalQuestions; i++) {
            int random = getRandomInt(0, questionList.size());
            questionListToAnswer.add(questionList.get(random));
            //questionList.remove(random);

        }

    }

    private int getRandomInt(int min, int max) {
        return ((int) (Math.random() * (max - min))) + min;
    }
}