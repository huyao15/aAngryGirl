package com.xianglanqi.angrygirl.model;

import com.xianglanqi.angrygirl.R;

public enum Mood {

    /**
     */
    UNKNOWN(-1, "0000", -1),

    /**
     */
    HAPPY(0, "0001", R.drawable.happy),

    /**
     */
    SAD(1, "0002", R.drawable.sad),

    /**
     */
    ANGRY(2, "0003", R.drawable.angry);

    /**
     */
    public static final int NUMBER = 3;

    public static Mood getMood(int index) {
        switch (index) {
        case 0:
            return HAPPY;
        case 1:
            return SAD;
        case 2:
            return ANGRY;
        default:
            return UNKNOWN;
        }
    }

    public static Mood getMood(String code) {
        if ("0001".equals(code)) {
            return HAPPY;
        }
        if ("0002".equals(code)) {
            return SAD;
        }
        if ("0003".equals(code)) {
            return ANGRY;
        }
        return UNKNOWN;
    }

    private Mood(int index, String code, int resource) {
        this.index = index;
        this.code = code;
        this.resource = resource;
    }

    private int index;

    private String code;

    private int resource;

    public int getIndex() {
        return index;
    }

    public String getCode() {
        return code;
    }

    public int getResource() {
        return resource;
    }

}
