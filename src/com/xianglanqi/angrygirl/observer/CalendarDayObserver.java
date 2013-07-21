package com.xianglanqi.angrygirl.observer;

import com.xianglanqi.angrygirl.model.CalendarCell;

public interface CalendarDayObserver {

    public void dayChanged(final CalendarCell cell);
}
