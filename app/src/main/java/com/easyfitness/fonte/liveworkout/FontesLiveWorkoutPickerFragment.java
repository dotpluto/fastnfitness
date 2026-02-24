package com.easyfitness.fonte.liveworkout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.easyfitness.AppViMo;
import com.easyfitness.DAO.DatabaseAccess;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.R;

import java.util.List;
import java.util.function.Consumer;

public class FontesLiveWorkoutPickerFragment extends Fragment {
    AppViMo appViewModel;
    @Nullable Program selectedProgram;
    @Nullable View lastSelectedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViMo.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.tab_fontes_liveworkout_picker, container, false);

        ListView listView = view.findViewById(R.id.fonte_liveworkout_picker_listView);
        listView.setAdapter(new WorkoutSelectorAdapter(getActivity(), DatabaseAccess.getProgramDAO().getAll(), (program) -> {}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(view == lastSelectedView) {
                    return;
                }

                view.setBackgroundColor(Color.LTGRAY);
                selectedProgram = (Program) adapterView.getItemAtPosition(i);

                if(lastSelectedView != null) {
                    lastSelectedView.setBackgroundColor(Color.WHITE);
                }

                lastSelectedView = view;
            }
        });

        Button button = view.findViewById(R.id.fonte_liveworkout_picker_startButton);
        button.setOnClickListener(buttonView -> {
            if(selectedProgram != null) {
                if(appViewModel.getProgramActiveInLiveView().getValue() != null) {
                    throw new IllegalStateException("There already is a program running.");
                }
                appViewModel.startProgramInLiveView(selectedProgram);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        appViewModel = null;
        lastSelectedView = null;
        selectedProgram = null;
    }

    private static class WorkoutSelectorAdapter extends BaseAdapter {
        List<Program> workouts;
        Activity activity;
        Consumer<Program> onSelectWorkout;

        public WorkoutSelectorAdapter(Activity activity, List<Program> workouts, Consumer<Program> onSelectWorkout) {
            this.workouts = workouts;
            this.activity = activity;
            this.onSelectWorkout = onSelectWorkout;
        }


        @Override
        public int getCount() {
            return workouts.size();
        }

        @Override
        public Object getItem(int i) {
            return workouts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int index, View existingView, ViewGroup viewGroup) {
            View view = existingView;
            if(view == null) {
                view = activity.getLayoutInflater().inflate(R.layout.tab_fontes_liveworkout_picker_workoutitem, viewGroup, false);
            }
            TextView text = view.findViewById(R.id.fonte_liveworkout_picker_workoutitem_text);
            text.setText(workouts.get(index).getName());
            return view;
        }
    }

}
