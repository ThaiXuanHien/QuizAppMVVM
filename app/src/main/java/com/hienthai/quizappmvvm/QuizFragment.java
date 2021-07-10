package com.hienthai.quizappmvvm;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QUIZ_FRAGMENT_LOG";
    private TextView txt_title_quiz, txt_quiz_question_number, txt_quiz_question_time, txt_quiz_question, txt_quiz_question_feedback;
    private ProgressBar pgb_quiz_question;
    private Button btn_quiz_option_one, btn_quiz_option_two, btn_quiz_option_three, btn_next_question;

    private FirebaseFirestore firebaseFirestore;
    private String quizId;

    private List<Question> questionList = new ArrayList<>();
    private List<Question> questionListToAnswer = new ArrayList<>();
    private long totalQuestions = 0L;

    private CountDownTimer countDownTimer;

    private boolean canAnswer = false;
    private int currentQuestion = 0;

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

        //Set Button Click Listeners
        btn_quiz_option_one.setOnClickListener(this);
        btn_quiz_option_two.setOnClickListener(this);
        btn_quiz_option_three.setOnClickListener(this);

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
        txt_quiz_question.setText(questionListToAnswer.get(questNum - 1).getQuestion());

        //Load Options
        btn_quiz_option_one.setText(questionListToAnswer.get(questNum - 1).getOptionA());
        btn_quiz_option_two.setText(questionListToAnswer.get(questNum - 1).getOptionB());
        btn_quiz_option_three.setText(questionListToAnswer.get(questNum - 1).getOptionC());
        canAnswer = true;
        currentQuestion = questNum;

        //Start Question Timer
        startTimer(questNum);
    }

    private void startTimer(int questionNumber) {

        //Set Timer Text
        final Long timeToAnswer = questionListToAnswer.get(questionNumber - 1).getTimer();
        txt_quiz_question_time.setText(timeToAnswer.toString());

        //Show Timer ProgressBar
        pgb_quiz_question.setVisibility(View.VISIBLE);

        //Start CountDown
        countDownTimer = new CountDownTimer(timeToAnswer * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Update Time
                txt_quiz_question_time.setText(millisUntilFinished / 1000 + "");

                //Progress in percent
                Long percent = millisUntilFinished / (timeToAnswer * 10);
                pgb_quiz_question.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                //Time Up, Cannot Answer Question Anymore
                canAnswer = false;
            }
        };

        countDownTimer.start();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_quiz_option_one:
                verifyAnswer(btn_quiz_option_one);
                break;
            case R.id.btn_quiz_option_two:
                verifyAnswer(btn_quiz_option_two);
                break;
            case R.id.btn_quiz_option_three:
                verifyAnswer(btn_quiz_option_three);
                break;
        }
    }

    private void verifyAnswer(Button selectedAnswer) {
        //Check Answer
        if (canAnswer) {
            if (questionListToAnswer.get(currentQuestion - 1).getAnswer().equals(selectedAnswer.getText())) {
                //Correct Answer
                Log.d(TAG, "Correct Answer");
            } else {
                //Wrong Answer
                Log.d(TAG, "Wrong Answer");
            }
            //Set Can answer to false
            canAnswer = false;
        }
    }
}