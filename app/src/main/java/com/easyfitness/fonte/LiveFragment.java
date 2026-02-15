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
        var view = inflater.inflate(R.layout.tab_live, container, false);



        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.add(R.id.liveWorkoutContainer, LivePickerFragment.class, new Bundle());
        transaction.commit();
        appViewModel.getProgramActiveInLiveView().observe(getViewLifecycleOwner(), (Program program) -> {
            if(program != null) {
                FragmentTransaction changeTransaction = getParentFragmentManager().beginTransaction();
                changeTransaction.setReorderingAllowed(true);
                changeTransaction.replace(R.id.liveWorkoutContainer, LiveExerciseFragment.class, new Bundle());
                changeTransaction.commit();
            }
        });

        return view;
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

