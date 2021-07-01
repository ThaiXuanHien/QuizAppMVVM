package com.hienthai.quizappmvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;


public class DetailsFragment extends Fragment implements View.OnClickListener {

    private ImageView img_details;
    private TextView txt_title_details, txt_desc_details, txt_difficulty_details, txt_total_questions, txt_title_last_score_details;
    private Button btn_start_details;


    private NavController navController;
    private QuizViewModel quizViewModel;

    private int position;
    private long totalQuestions;

    private String quizId;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        anhXa(view);

        return view;
    }

    private void anhXa(View view) {
        img_details = view.findViewById(R.id.img_details);
        txt_title_details = view.findViewById(R.id.txt_title_details);
        txt_desc_details = view.findViewById(R.id.txt_desc_details);
        txt_difficulty_details = view.findViewById(R.id.txt_difficulty_details);
        txt_total_questions = view.findViewById(R.id.txt_total_questions);
        txt_title_last_score_details = view.findViewById(R.id.txt_title_last_score_details);
        btn_start_details = view.findViewById(R.id.btn_start_details);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        quizViewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), quizzes -> {

            Quiz quiz = quizzes.get(position);
            Glide.with(getContext()).load(quiz.getImage()).into(img_details);

            txt_title_details.setText(quiz.getName());
            txt_desc_details.setText(quiz.getDesc());
            txt_difficulty_details.setText(quiz.getLevel());
            txt_total_questions.setText(String.valueOf(quiz.getQuestions()));

            quizId = quiz.getQuiz_id();
            totalQuestions = quiz.getQuestions();

        });

        btn_start_details.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_details:

                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();

                action.setTotalQuestions(totalQuestions);
                action.setQuizid(quizId);
                navController.navigate(action);
                break;
        }
    }
}