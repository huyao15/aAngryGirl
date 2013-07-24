package com.xianglanqi.angrygirl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Mood implements Serializable {

    private static final long serialVersionUID = 3443394553992774425L;

    public static final Mood UNKNOWN = new Mood(-1, "0000");

    public static final String DEFAULT_GROUP = "";

    public static final String NIU_GROUP = "m";

    private static Map<String, List<Mood>> moodGroups = new HashMap<String, List<Mood>>();
    static {
        List<Mood> defaultGroup = new ArrayList<Mood>();
        defaultGroup.add(new Mood(0, "0001"));
        defaultGroup.add(new Mood(1, "0002"));
        defaultGroup.add(new Mood(2, "0003"));
        moodGroups.put(DEFAULT_GROUP, defaultGroup);

        List<Mood> niuGroup = new ArrayList<Mood>();
        for (int i = 0; i < 10; i++) {
            String code = "000" + (i + 1);
            code = "m" + code.substring(code.length() - 4, code.length());
            niuGroup.add(new Mood(i, code));
        }
        moodGroups.put(NIU_GROUP, niuGroup);
    }

    public static final int getCount(String group) {
        return moodGroups.get(group).size();
    }

    public static Mood getMood(String group, int index) {
        return moodGroups.get(group).get(index);
    }

    public static Mood getMood(String code) {
        String group = code.substring(0, code.length() - 4);
        for (Mood mood : moodGroups.get(group)) {
            if (mood.getCode().equals(code)) {
                return mood;
            }
        }
        return UNKNOWN;
    }

    private Mood(int index, String code) {
        this.index = index;
        this.code = code;
    }

    private int index;

    private String code;

    public int getIndex() {
        return index;
    }

    public String getCode() {
        return code;
    }

    public static final String getCurrentGroup(SharedPreferences sp) {
        return sp.getString("current_group", NIU_GROUP);
    }

    public static final void setCurrentGroup(SharedPreferences sp, String group) {
        Editor e = sp.edit();
        e.putString("current_group", group);
        e.commit();
    }

}
