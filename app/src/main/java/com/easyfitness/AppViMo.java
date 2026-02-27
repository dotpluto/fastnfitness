package com.easyfitness;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyfitness.DAO.DatabaseAccess;
import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.program.Program;
import com.easyfitness.DAO.program.ProgramHistory;
import com.easyfitness.DAO.record.Record;
import com.easyfitness.enums.ProgramRecordStatus;
import com.easyfitness.enums.ProgramStatus;
import com.easyfitness.utils.DateConverter;

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
            long historyId = DatabaseAccess.getProgramHistoryDAO().add(new ProgramHistory(-1, program.getId(), getProfile().getValue().getId(), ProgramStatus.CLOSED, DateConverter.currentDate(MyApplication.getAppContext()), DateConverter.currentTime(MyApplication.getAppContext()), "", ""));
            programHistoryActiveInLiveView = DatabaseAccess.getProgramHistoryDAO().get(historyId);

            exerciseIndex = 0;
            activeWorkoutData = workoutData;
            programActiveInLiveView.setValue(program);
            currentExerciseInLiveWorkout.setValue(activeWorkoutData.get(exerciseIndex));
        }

    }

    public void goToNextExercise() {
        liveViewInBreak.setValue(false);
        currentSetInLiveWorkout.setValue(0);
        if(exerciseIndex + 1 < activeWorkoutData.size()) {
            exerciseIndex += 1;
            currentExerciseInLiveWorkout.setValue(activeWorkoutData.get(exerciseIndex));
        } else {
            stopProgramInLiveView();
        }
    }

    MutableLiveData<Integer> currentSetInLiveWorkout = new MutableLiveData<>(0);

    public LiveData<Integer> getCurrentSetInLiveWorkout() {
        return currentSetInLiveWorkout;
    }
    public void doNextSet() {
        var curExercise = getCurrentExerciseInLiveWorkout().getValue();
        assert curExercise != null;
        assert currentSetInLiveWorkout.getValue() != null;
        if(currentSetInLiveWorkout.getValue() < curExercise.getSets() - 1) {
            currentSetInLiveWorkout.setValue(currentSetInLiveWorkout.getValue() + 1);
        } else {
            recordExerciseAsCompleted();
            goToNextExercise();
        }

    }

    public void skipExercise() {
        recordExerciseIfStarted();
        goToNextExercise();
    }

    public void quitLiveWorkout() {
        recordExerciseIfStarted();
        stopProgramInLiveView();
    }

    private void recordExerciseIfStarted() {
        assert currentSetInLiveWorkout.getValue() != null;
        if(currentSetInLiveWorkout.getValue() > 0) {
            assert currentExerciseInLiveWorkout.getValue() != null;
            assert profile.getValue() != null;
            assert programHistoryActiveInLiveView != null;
            currentExerciseInLiveWorkout.getValue().instantiateTemplate(profile.getValue(), programHistoryActiveInLiveView.getId(), ProgramRecordStatus.SUCCESS, currentSetInLiveWorkout.getValue(), null, null, null, null, null, null, null, null);
        }
    }

    private void recordExerciseAsCompleted() {
        assert currentExerciseInLiveWorkout.getValue() != null;
        assert profile.getValue() != null;
        assert programHistoryActiveInLiveView != null;
        currentExerciseInLiveWorkout.getValue().instantiateTemplate(profile.getValue(), programHistoryActiveInLiveView.getId(), ProgramRecordStatus.SUCCESS, null, null, null, null, null, null, null, null, null);
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
        currentSetInLiveWorkout.setValue(0);

        programHistoryActiveInLiveView.setEndDate(DateConverter.currentDate(MyApplication.getAppContext()));
        programHistoryActiveInLiveView.setEndTime(DateConverter.currentTime(MyApplication.getAppContext()));
        DatabaseAccess.getProgramHistoryDAO().update(programHistoryActiveInLiveView);
        programHistoryActiveInLiveView = null;
    }

    //break state
    MutableLiveData<Boolean> liveViewInBreak = new MutableLiveData<>(false);

    public void markCurrentExerciseAsDone() {
        liveViewInBreak.setValue(true);
    }

    public LiveData<Boolean> getliveViewInBreakData() {
        return liveViewInBreak;
    }

    @Nullable
    ProgramHistory programHistoryActiveInLiveView = null;

}
