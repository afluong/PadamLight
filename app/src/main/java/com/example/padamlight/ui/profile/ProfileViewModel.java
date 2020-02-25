package com.example.padamlight.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> description;
    private MutableLiveData<String> motivation;

    public ProfileViewModel() {
        description = new MutableLiveData<>();
        description.setValue("Sometimes I pretend to be normal but it gets boring So I go back to being me");

        motivation = new MutableLiveData<>();
        motivation.setValue("Sometimes there just isn't any ;)");

    }

    public LiveData<String> setProfileDescription() {
        return description;
    }

    public LiveData<String> setProfileMotivation() {
        return motivation;
    }
}