package com.easyfitness.fonte.liveworkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.R;

public class FontesLiveWorkoutFragment extends Fragment {
    @Nullable Program selectedProgram;

    AppViMo appViewModel;

    FontesLiveWorkoutPickerFragment livePicker;
    FontesLiveWorkoutExerciseFragment liveExercises;

    enum Screen {
        Picker,
        Exercise;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showFragment(livePicker);
        appViewModel.getProgramActiveInLiveView().observe(getViewLifecycleOwner(), (Program program) -> {
            if(program != null) {
                showFragment(liveExercises);
            } else {
                showFragment(livePicker);
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.liveWorkoutContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle inBundle) {
        super.onCreate(inBundle);
        if(inBundle == null) {
            livePicker = new FontesLiveWorkoutPickerFragment();
            liveExercises = new FontesLiveWorkoutExerciseFragment();
        } else {
            livePicker = (FontesLiveWorkoutPickerFragment) getChildFragmentManager().getFragment(inBundle, Screen.Picker.name());
            liveExercises = (FontesLiveWorkoutExerciseFragment) getChildFragmentManager().getFragment(inBundle, Screen.Exercise.name());
        }

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outBundle) {
        super.onSaveInstanceState(outBundle);

        getChildFragmentManager().putFragment(outBundle, Screen.Picker.name(), livePicker);
        getChildFragmentManager().putFragment(outBundle, Screen.Exercise.name(), liveExercises);
    }
}

