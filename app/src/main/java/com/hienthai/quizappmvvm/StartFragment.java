package com.hienthai.quizappmvvm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private static final String START_TAG = "START_LOG";

    private NavController navController;

    private ProgressBar pgb_start;
    private TextView txt_start_feedback;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);
        txt_start_feedback = view.findViewById(R.id.txt_start_feedback);
        pgb_start = view.findViewById(R.id.pgb_start);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {

            txt_start_feedback.setText("Creating Account...");

            //Create a new account
            firebaseAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    txt_start_feedback.setText("Account Created...");
                    navController.navigate(R.id.action_startFragment_to_listFragment);
                } else {
                    Log.d(START_TAG, "Start Log : " + task.getException().getMessage());
                }
            });
        } else {
            //Navigate to Homepage
            txt_start_feedback.setText("Logged in...");
            navController.navigate(R.id.action_startFragment_to_listFragment);
        }
    }
}