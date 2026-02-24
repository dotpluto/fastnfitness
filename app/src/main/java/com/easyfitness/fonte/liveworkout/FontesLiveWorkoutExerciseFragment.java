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

import java.util.List;

public class FontesLiveWorkoutExerciseFragment extends Fragment {
    AppViMo appViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView exerciseNameText = view.findViewById(R.id.fontes_liveworkout_exercise_exercisenametext);
        TextView exerciseTypeText = view.findViewById(R.id.fontes_liveworkout_exercise_typeText);
        TextView weightInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_weightinfotext);
        TextView repInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_repinfotext);
        TextView secondsInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_secondsinfotext);
        TextView distanceInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_distanceinfotext);
        TextView durationInfoText = view.findViewById(R.id.fontes_liveworkout_exercise_durationinfotext);

        //when a workout is started
        appViewModel.getActiveWorkoutData().observe(getViewLifecycleOwner(), (List<Record> workoutData) -> {
            var nextExercise  = appViewModel.nextExercise();
            if(nextExercise != null) {
                showExercise(nextExercise, exerciseNameText, exerciseTypeText, weightInfoText, repInfoText, secondsInfoText, distanceInfoText, durationInfoText);
            }
        });

        var button = (Button)view.findViewById(R.id.fontes_liveworkout_exercise_nextbutton);
        button.setOnClickListener((buttonView) -> {
            if(appViewModel.getActiveWorkoutData().getValue() != null) {
                var nextRecord = appViewModel.nextExercise();
                if(nextRecord != null) {
                    showExercise(nextRecord, exerciseNameText, exerciseTypeText, weightInfoText, repInfoText, secondsInfoText, distanceInfoText, durationInfoText);
                } else {
                    appViewModel.stopProgramInLiveView();
                }

            }
        });

        var quitButton = (Button)view.findViewById(R.id.fontes_liveworkout_exercise_quitbutton);
        quitButton.setOnClickListener((buttonView) -> {
            appViewModel.stopProgramInLiveView();
        });
    }

    private void showExercise(Record record, TextView exerciseTitle, TextView exerciseType, TextView weightInfo, TextView repInfo, TextView secondsInfo, TextView distanceInfo, TextView durationInfo) {
        exerciseTitle.setText(record.getExercise());
        exerciseType.setText(record.getExerciseType().toString());
        switch(record.getExerciseType()) {
            case STRENGTH -> {
                weightInfo.setVisibility(VISIBLE);
                weightInfo.setText(record.getTemplateWeightInfoString());
                repInfo.setVisibility(VISIBLE);
                repInfo.setText(record.getTemplateRepsInfoString());
                secondsInfo.setVisibility(GONE);
                distanceInfo.setVisibility(GONE);
                durationInfo.setVisibility(GONE);
            }
            case CARDIO -> {
                weightInfo.setVisibility(GONE);
                repInfo.setVisibility(GONE);
                secondsInfo.setVisibility(GONE);
                distanceInfo.setVisibility(VISIBLE);
                distanceInfo.setText(record.getTemplateDistanceDisplayString());
                durationInfo.setVisibility(VISIBLE);
                durationInfo.setText(record.getTemplateDurationDisplayString());
            }
            case ISOMETRIC -> {
                weightInfo.setVisibility(VISIBLE);
                weightInfo.setText(record.getTemplateWeightInfoString());
                repInfo.setVisibility(GONE);
                secondsInfo.setVisibility(VISIBLE);
                secondsInfo.setText(record.getTemplateSecondsDisplayString());
                distanceInfo.setVisibility(GONE);
                durationInfo.setVisibility(GONE);
            }
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
}
