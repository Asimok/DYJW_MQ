package com.example.dyjw_mq.ui.score_detail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dyjw_mq.R;


public class scoredetailFragment extends Fragment {

    private scoredetailViewModel coursedetailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coursedetailViewModel =
                ViewModelProviders.of(this).get(scoredetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scoredetail, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        coursedetailViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}