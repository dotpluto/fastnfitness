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

public class FontesLiveWorkoutRestFragment extends Fragment {
    AppViMo appViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fontes_liveworkout_rest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button skipButton = view.findViewById(R.id.fontes_liveworkout_rest_skipbutton);
        skipButton.setOnClickListener((buttonView) -> {
            appViewModel.goToNextExercise();
        });
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
