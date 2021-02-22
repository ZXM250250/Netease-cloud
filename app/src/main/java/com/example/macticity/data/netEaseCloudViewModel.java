package com.example.macticity.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class netEaseCloudViewModel extends ViewModel {

    private MutableLiveData<Integer>  currentMs = new MutableLiveData<>();
    private MutableLiveData<String>    time = new MutableLiveData<>();

    public MutableLiveData<Integer> getCurrentMs() {
        return currentMs;
    }

    public MutableLiveData<String> getTime() {
        return time;
    }

    public void setCurrentMs(MutableLiveData<Integer> currentMs) {
        this.currentMs = currentMs;
    }

    public void setTime(MutableLiveData<String> time) {
        this.time = time;
    }
}
