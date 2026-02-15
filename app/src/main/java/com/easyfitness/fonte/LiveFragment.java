package com.easyfitness.fonte;

import android.os.Bundle;
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

import com.easyfitness.DAO.program.DAOProgram;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.R;

import java.util.List;

public class LiveFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        var layout = inflater.inflate(R.layout.tab_live, container, false);
        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.liveWorkoutContainer, LivePickerFragment.class, new Bundle())
                .commit();
        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     public static class FragmentViewModel extends ViewModel {
        MutableLiveData<Program> selectedProgram = new MutableLiveData<>();
    }
}


