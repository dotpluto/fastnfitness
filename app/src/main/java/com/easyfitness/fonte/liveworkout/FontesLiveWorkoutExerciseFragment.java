package com.easyfitness.fonte.liveworkout;

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

    TextView text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        text = (TextView)view.findViewById(R.id.fontes_live_exercise_exerciseName);
        appViewModel.getActiveWorkoutData().observe(getViewLifecycleOwner(), (List<Record> workoutData) -> {
            text.setText(appViewModel.getNextExercise().getExercise());
        });

        var button = (Button)view.findViewById(R.id.fontes_live_exercise_nextButton);
        button.setOnClickListener((buttonView) -> {
            if(appViewModel.getActiveWorkoutData().getValue() != null) {
                var nextRecord = appViewModel.getNextExercise();
                if(nextRecord != null) {
                    text.setText(nextRecord.getExercise());
                } else {
                    appViewModel.stopProgramInLiveView();
                }

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }
}
