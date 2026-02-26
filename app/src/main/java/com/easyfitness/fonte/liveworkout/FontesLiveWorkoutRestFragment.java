package com.easyfitness.fonte.liveworkout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.R;

public class FontesLiveWorkoutRestFragment extends Fragment {
    AppViMo appViewModel;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBar;
    private TextView restTimeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout_rest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button skipButton = view.findViewById(R.id.fontes_liveworkout_rest_skipbutton);
        skipButton.setOnClickListener((buttonView) -> {
            stopTimer();
            appViewModel.goToNextExercise();
        });

        restTimeText = view.findViewById(R.id.fontes_liveworkout_rest_resttimetext);
        progressBar = view.findViewById(R.id.fontes_liveworkout_rest_progressbar);

        appViewModel.getCurrentExerciseInLiveWorkout().observe(getViewLifecycleOwner(), (record -> {
            if(record != null) {
                startTimer(record.getTemplateRestTime());
            }
        }));
    }

    private void startTimer(int restTimeSeconds) {
        stopTimer();
        if (restTimeSeconds <= 0) {
            appViewModel.goToNextExercise();
            return;
        }

        if (progressBar != null) {
            progressBar.setMax(restTimeSeconds * 1000);
            progressBar.setProgress(0);
        }

        countDownTimer = new CountDownTimer(restTimeSeconds * 1000L, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int millisElapsed = (int) (restTimeSeconds * 1000L - millisUntilFinished);
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                if (restTimeText != null) {
                    restTimeText.setText(getString(R.string.fontes_liveworkout_rest_seconds, secondsRemaining));
                }
                if (progressBar != null) {
                    progressBar.setProgress(millisElapsed);
                }
            }

            @Override
            public void onFinish() {
                if (progressBar != null) {
                    progressBar.setProgress(restTimeSeconds * 1000);
                }
                if (restTimeText != null) {
                    restTimeText.setText(getString(R.string.fontes_liveworkout_rest_seconds, 0));
                }
                appViewModel.goToNextExercise();
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
        progressBar = null;
        restTimeText = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appViewModel = null;
    }
}
