package com.xianglanqi.angrygirl.model;

import java.util.HashMap;
import java.util.Map;

public class MenuCell {

    private int year;

    private int month;

    private Map<Mood, Integer> moodCount;

    private boolean isParent = false;

    public MenuCell(int year, int month) {
        this.year = year;
        this.month = month;
        this.isParent = false;
        this.moodCount = new HashMap<Mood, Integer>();
    }

    public MenuCell(int year) {
        this.year = year;
        this.isParent = true;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Map<Mood, Integer> getMoodCount() {
        return moodCount;
    }

    public void setMoodCount(Map<Mood, Integer> moodCount) {
        this.moodCount = moodCount;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

    public void addMood(Mood mood, int count) {
        if (this.moodCount.containsKey(mood)) {
            count += this.moodCount.get(mood);
        }
        this.moodCount.put(mood, count);
    }

    public Mood getMood(int pos) {
        int i = 0;
        for (Mood mood : this.moodCount.keySet()) {
            if (i >= pos) {
                return mood;
            }
            i++;
        }
        return Mood.UNKNOWN;
    }

    public int getCount(Mood mood) {
        return this.moodCount.get(mood);
    }
}
