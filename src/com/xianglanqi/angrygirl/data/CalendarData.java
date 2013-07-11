package com.xianglanqi.angrygirl.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.CellType;
import com.xianglanqi.angrygirl.model.Mood;

public class CalendarData {

    private static final CalendarData instance = new CalendarData();

    private CalendarData() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    public static final CalendarData getIntance() {
        return instance;
    }

    private Calendar calendar;

    private Map<String, CalendarCell> moodMap;

    public static final String[] WEEKS = { "一", "二", "三", "四", "五", "六", "日" };

    public Calendar getCurrentCalendar() {
        return calendar;
    }

    public void preMonth() {
        calendar.add(Calendar.MONTH, -1);
    }

    public void postMonth() {
        calendar.add(Calendar.MONTH, 1);
    }

    public void setMoodMap(Map<String, CalendarCell> moodMap) {
        if (null == this.moodMap) {
            this.moodMap = new HashMap<String, CalendarCell>();
        } else {
            this.moodMap.clear();
        }
        this.moodMap.putAll(moodMap);
    }

    public String getWeek(int pos) {
        return WEEKS[pos % 7];
    }

    public String getDay(int pos) {

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
        if (dayOfWeek < 0) {
            dayOfWeek = 6;
        }
        int position = (dayOfMonth / 7) * 7 + dayOfWeek + 7;

        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());

        cal.add(Calendar.DAY_OF_YEAR, pos - position);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public CalendarCell getCell(int pos) {
        CellType cellType = null;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        if (pos < 7) {
            cellType = CellType.CELL_WEEK;
            return new CalendarCell(year, month, -1, WEEKS[pos % 7], cellType);
        }

        // today
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek < 0) {
            dayOfWeek = 6;
        }

        int firstDayPosition = (dayOfMonth / 7) * 7 + dayOfWeek + 7;

        // that day at pos
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        cal.add(Calendar.DAY_OF_YEAR, pos - firstDayPosition);
        int thatDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int thatMonth = cal.get(Calendar.MONTH);
        int thatYear = cal.get(Calendar.YEAR);
        Log.d("hy", "center:(" + dayOfMonth + "," + firstDayPosition + ")" + "\tpos:(" + thatDayOfMonth + "," + pos
                + ")");

        CalendarCell cell = null;
        String key = year + "_" + month + "_" + thatDayOfMonth;
        if (thatYear < year) {
            cellType = CellType.CELL_DAY_PRE_MONTH;
        } else if (thatYear > year) {
            cellType = CellType.CELL_DAY_POST_MONTH;
        } else if (thatMonth < month) {
            cellType = CellType.CELL_DAY_PRE_MONTH;
        } else if (thatMonth > month) {
            cellType = CellType.CELL_DAY_POST_MONTH;
        } else {
            cellType = CellType.CELL_DAY;
            if (this.moodMap.containsKey(key)) {
                cell = this.moodMap.get(key);
                cell.setCellType(cellType);
                cell.setText(String.valueOf(thatDayOfMonth));
            }
        }

        if (null == cell) {
            cell = new CalendarCell(year, thatMonth, thatDayOfMonth, String.valueOf(thatDayOfMonth), cellType);
        }

        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int thatDay = cal.get(Calendar.DAY_OF_YEAR);
        if (thatDay == today && thatYear == Calendar.getInstance().get(Calendar.YEAR)) {
            cell.setCanChange(true);
            cell.setToday(true);
        } else if (thatDay == today - 1 && thatYear == Calendar.getInstance().get(Calendar.YEAR)
                && cell.getMood() == Mood.UNKNOWN) {
            cell.setCanChange(true);
        }

        return cell;
    }
}
