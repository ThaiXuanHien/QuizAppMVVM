package com.hienthai.quizappmvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListFragment extends Fragment implements CategoryQuizAdapter.OnCategoryClicked {

    private static final String TAG = "ListFragment";

    private RecyclerView rcv_category_quiz;
    private ProgressBar pgb_list_category;
    private NavController navController;

    private Animation fadeIn, fadeOut;

    private QuizViewModel quizViewModel;
    private CategoryQuizAdapter categoryQuizAdapter;


    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        rcv_category_quiz = view.findViewById(R.id.rcv_category_quiz);
        pgb_list_category = view.findViewById(R.id.pgb_list_category);


        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        categoryQuizAdapter = new CategoryQuizAdapter(this);
        rcv_category_quiz.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_category_quiz.setHasFixedSize(true);
        rcv_category_quiz.setAdapter(categoryQuizAdapter);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        quizViewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), quizzes -> {

            rcv_category_quiz.startAnimation(fadeIn);
            pgb_list_category.setAnimation(fadeOut);

            categoryQuizAdapter.setQuizList(quizzes);
            categoryQuizAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onItemClicked(int position) {
        ListFragmentDirections.ActionListFragmentToDetailsFragment actionListFragmentToDetailsFragment = ListFragmentDirections.actionListFragmentToDetailsFragment();

        actionListFragmentToDetailsFragment.setPosition(position);

        navController.navigate(actionListFragmentToDetailsFragment);
    }
}