package com.easyfitness;

import android.content.Context;
import android.database.Observable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyfitness.DAO.DatabaseAccess;
import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.DAO.record.Record;

import java.util.ArrayList;
import java.util.List;

public class AppViMo extends ViewModel {

    private final MutableLiveData<Profile> profile = new MutableLiveData<>();

    public LiveData<Profile> getProfile() {
        return profile;
    }

    public void setProfile(Profile pProfile) {
        profile.setValue(pProfile);
    }

    private int exerciseIndex = 0;
    private final MutableLiveData<List<Record>> activeWorkoutData = new MutableLiveData<>();

    public LiveData<List<Record>> getActiveWorkoutData() {
        return activeWorkoutData;
    }

    private final MutableLiveData<Program> programActiveInLiveView = new MutableLiveData<>();

    public LiveData<Program> getProgramActiveInLiveView() {
        return programActiveInLiveView;
    }

    public void startProgramInLiveView(Program program) throws IllegalStateException {
        if(programActiveInLiveView.getValue() != null) {
            throw new IllegalStateException("Tried to start program when another was still running.");
        }
        exerciseIndex = 0;
        activeWorkoutData.setValue(DatabaseAccess.getRecordDAO().getAllTemplateRecordByProgramArray(program.getId()));
        programActiveInLiveView.setValue(program);
    }

    public Record getNextExercise() {
        assert (activeWorkoutData.getValue() != null);
        if(exerciseIndex < activeWorkoutData.getValue().size()) {
            var exercise = activeWorkoutData.getValue().get(exerciseIndex);
            exerciseIndex += 1;
            return exercise;
        } else {
            return null;
        }
    }

    public void stopProgramInLiveView() {
        programActiveInLiveView.setValue(null);
    }

}
