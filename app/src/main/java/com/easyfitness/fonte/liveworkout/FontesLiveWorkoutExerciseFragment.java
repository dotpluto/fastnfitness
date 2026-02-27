package com.easyfitness.fonte.liveworkout;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.DAO.record.Record;
import com.easyfitness.R;

public class FontesLiveWorkoutExerciseFragment extends Fragment {
    AppViMo appViewModel;

    TextView exerciseNameText;
    TextView exerciseTypeText;
    TextView weightInfoText;
    TextView repInfoText;
    TextView secondsInfoText;
    TextView distanceInfoText;
    TextView durationInfoText;

    TextView setInfoText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        exerciseNameText = view.findViewById(R.id.fontes_liveworkout_exercise_exercisenametext);
        exerciseTypeText = view.findViewById(R.id.fontes_liveworkout_exercise_typeText);
        weightInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_weightinfotext);
        repInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_repinfotext);
        secondsInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_secondsinfotext);
        distanceInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_distanceinfotext);
        durationInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_durationinfotext);
        setInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_setinfotext);

        //when a workout is started
        appViewModel.getCurrentExerciseInLiveWorkout().observe(getViewLifecycleOwner(), (Record exercise) -> {
            if(exercise != null) {
                showExercise(exercise);
            }
        });

        var button = (Button)view.findViewById(R.id.fontes_liveworkout_exercise_nextbutton);
        button.setOnClickListener((buttonView) -> {
            appViewModel.doNextSet();
        });

        var quitButton = (Button)view.findViewById(R.id.fontes_liveworkout_exercise_quitbutton);
        quitButton.setOnClickListener((buttonView) -> {
            appViewModel.quitLiveWorkout();
        });

        appViewModel.getCurrentSetInLiveWorkout().observe(getViewLifecycleOwner(), (Integer set) -> {
            showSetInfo(appViewModel.getCurrentExercise());
        });

        Button skipButton = view.findViewById(R.id.fontes_liveworkout_exercise_skipbutton);
        skipButton.setOnClickListener((buttonView) -> {
            appViewModel.skipExercise();
        });
    }

    private void showExercise(Record record) {
        exerciseNameText.setText(record.getExercise());
        exerciseTypeText.setText(record.getExerciseType().toString());
        switch(record.getExerciseType()) {
            case STRENGTH -> {
                weightInfoText.setVisibility(VISIBLE);
                weightInfoText.setText(record.getTemplateWeightInfoString());
                repInfoText.setVisibility(VISIBLE);
                repInfoText.setText(record.getTemplateRepsInfoString());
                secondsInfoText.setVisibility(GONE);
                distanceInfoText.setVisibility(GONE);
                durationInfoText.setVisibility(GONE);
            }
            case CARDIO -> {
                weightInfoText.setVisibility(GONE);
                repInfoText.setVisibility(GONE);
                secondsInfoText.setVisibility(GONE);
                distanceInfoText.setVisibility(VISIBLE);
                distanceInfoText.setText(record.getTemplateDistanceDisplayString());
                durationInfoText.setVisibility(VISIBLE);
                durationInfoText.setText(record.getTemplateDurationDisplayString());
            }
            case ISOMETRIC -> {
                weightInfoText.setVisibility(VISIBLE);
                weightInfoText.setText(record.getTemplateWeightInfoString());
                repInfoText.setVisibility(GONE);
                secondsInfoText.setVisibility(VISIBLE);
                secondsInfoText.setText(record.getTemplateSecondsDisplayString());
                distanceInfoText.setVisibility(GONE);
                durationInfoText.setVisibility(GONE);
            }
        }

        showSetInfo(record);
    }

    void showSetInfo(Record record) {
        if(record != null && record.getSets() > 1) {
            assert appViewModel.getCurrentSetInLiveWorkout().getValue() != null;
            setInfoText.setText(getString(R.string.fontes_liveworkout_rest_setinfotext, appViewModel.getCurrentSetInLiveWorkout().getValue() + 1, record.getSets()));
        } else {
            setInfoText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        appViewModel = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        exerciseNameText = null;
        exerciseTypeText = null;
        weightInfoText = null;
        repInfoText = null;
        secondsInfoText = null;
        distanceInfoText = null;
        durationInfoText = null;
        setInfoText = null;
    }
}
