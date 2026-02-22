package com.easyfitness.fonte.liveworkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.DAO.program.DAOProgram;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.R;

public class LivePickerFragment extends Fragment {
    AppViMo appViewModel;
    @Nullable Program selectedProgram;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.tab_live_picker, container, false);

        Spinner spinner = view.findViewById(R.id.liveWorkoutPickerSpinner);
        var context = getContext();
        assert context != null;
        var spinnerAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item,
                new DAOProgram(this.getContext()).getAll());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProgram = (Program)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button button = view.findViewById(R.id.liveWorkoutPickerStartButton);
        button.setOnClickListener(buttonView -> {
            appViewModel.startProgramInLiveView(selectedProgram);
        });
        return view;
    }



}
