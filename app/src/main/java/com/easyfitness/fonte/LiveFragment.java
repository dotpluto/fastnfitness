package com.easyfitness.fonte;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.DAO.program.DAOProgram;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.R;

import java.util.List;

public class LiveFragment extends Fragment {
    @Nullable Program selectedProgram;

    AppViMo appViewModel;

    LivePickerFragment livePicker;
    LiveExerciseFragment liveExercises;

    enum Screen {
        Picker,
        Exercise;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_live, container, false);
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
            livePicker = new LivePickerFragment();
            liveExercises = new LiveExerciseFragment();
        } else {
            livePicker = (LivePickerFragment) getChildFragmentManager().getFragment(inBundle, Screen.Picker.name());
            liveExercises = (LiveExerciseFragment) getChildFragmentManager().getFragment(inBundle, Screen.Exercise.name());
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

