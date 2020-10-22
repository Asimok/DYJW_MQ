package com.example.dyjw_mq.ui.score_detail;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class scoredetail_datainfo {
    private String xuenian;

    public ArrayList<BarEntry> getValues() {
        return values;
    }

    public void setValues(ArrayList<BarEntry> values) {
        this.values = values;
    }

    private ArrayList<BarEntry> values;
    public String getXuenian() {
        return xuenian;
    }

    public void setXuenian(String xuenian) {
        this.xuenian = xuenian;
    }

    public String getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(String maxscore) {
        this.maxscore = maxscore;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getPassrate() {
        return passrate;
    }

    public void setPassrate(String passrate) {
        this.passrate = passrate;
    }

    private String maxscore;
    private String mean;
    private String gpa;
    private String passrate;
}

