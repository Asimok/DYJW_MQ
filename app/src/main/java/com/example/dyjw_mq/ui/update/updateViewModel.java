package com.example.dyjw_mq.ui.update;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

    public class updateViewModel extends ViewModel {

        private MutableLiveData<String> mText;

        public updateViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("更新暂未开发");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }