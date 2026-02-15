package com.easyfitness;

import android.database.Observable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.program.Program;

public class AppViMo extends ViewModel {

    private final MutableLiveData<Profile> profile = new MutableLiveData<>();

    public LiveData<Profile> getProfile() {
        return profile;
    }

    public void setProfile(Profile pProfile) {
        profile.setValue(pProfile);
    }


    private final MutableLiveData<Program> programActiveInLiveView = new MutableLiveData<>() {
    };

    public LiveData<Program> getProgramActiveInLiveView() {
        return programActiveInLiveView;
    }

    public void startProgramInLiveView(Program program) throws IllegalStateException {
        if(programActiveInLiveView.getValue() != null) {
            throw new IllegalStateException("Tried to start program when another was still running.");
        }
        programActiveInLiveView.setValue(program);
    }

}
