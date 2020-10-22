package com.example.dyjw_mq.ui.score_inquiry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class score_inquiry_ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public score_inquiry_ViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}