package com.easyfitness;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyfitness.DAO.DatabaseAccess;
import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.DAO.record.Record;

import java.util.List;

public class AppViMo extends ViewModel {

    private final MutableLiveData<Profile> profile = new MutableLiveData<>();

    public LiveData<Profile> getProfile() {
        return profile;
    }

    public void setProfile(Profile pProfile) {
        profile.setValue(pProfile);
    }

    //The program in the live view and wheter something is running at all.
    private final MutableLiveData<Program> programActiveInLiveView = new MutableLiveData<>();
    public LiveData<Program> getProgramActiveInLiveView() {
        return programActiveInLiveView;
    }

    List<Record> activeWorkoutData = null;

    private int exerciseIndex = 0;

    final MutableLiveData<Record> currentExerciseInLiveWorkout = new MutableLiveData<>(null);
    public LiveData<Record> getCurrentExerciseInLiveWorkout() {
        return currentExerciseInLiveWorkout;
    }

    public void startProgramInLiveView(Program program) throws IllegalStateException {
        if(programActiveInLiveView.getValue() != null) {
            throw new IllegalStateException("Tried to start program when another was still running.");
        }
        var workoutData = DatabaseAccess.getRecordDAO().getAllTemplateRecordByProgramArray(program.getId());
        if(!workoutData.isEmpty()) {
            exerciseIndex = 0;
            activeWorkoutData = workoutData;
            programActiveInLiveView.setValue(program);
            currentExerciseInLiveWorkout.setValue(activeWorkoutData.get(exerciseIndex));
        }

    }

    public void goToNextExercise() {
        assert activeWorkoutData != null;
        if(exerciseIndex + 1 < activeWorkoutData.size()) {
            exerciseIndex += 1;
            currentExerciseInLiveWorkout.setValue(activeWorkoutData.get(exerciseIndex));
        } else {
            stopProgramInLiveView();
        }

    }

    public Record getCurrentExercise() {
        if(activeWorkoutData == null) {
            return null;
        }

        if(exerciseIndex < activeWorkoutData.size()) {
            return activeWorkoutData.get(exerciseIndex);
        } else {
            return null;
        }
    }

    public void stopProgramInLiveView() {
        programActiveInLiveView.setValue(null);
        activeWorkoutData = null;
        currentExerciseInLiveWorkout.setValue(null);
    }

}
