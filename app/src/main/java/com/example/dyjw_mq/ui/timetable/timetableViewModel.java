package com.example.dyjw_mq.ui.timetable;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

    public class timetableViewModel extends ViewModel {

        private MutableLiveData<String> mText;

        public timetableViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("课表暂未开发");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }