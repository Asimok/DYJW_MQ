package com.example.dyjw_mq.ui.score_detail;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

    public class scoredetailViewModel extends ViewModel {

        private MutableLiveData<String> mText;

        public scoredetailViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("成绩分析暂未开发");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }