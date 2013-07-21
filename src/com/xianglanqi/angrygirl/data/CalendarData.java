package com.xianglanqi.angrygirl.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.CellType;
import com.xianglanqi.angrygirl.model.Mood;
import com.xianglanqi.angrygirl.observer.CalendarDayObserver;

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
    // public static final String[] WEEKS = { "Mon", "Tue", "Wed", "Thu", "Fri",
    // "Set", "Sun" };

    private List<CalendarDayObserver> observers = new ArrayList<CalendarDayObserver>();

    public Calendar getCurrentCalendar() {
        return calendar;
    }

    public void setCalendar(int year, int month) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
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

    public void registObserver(final CalendarDayObserver observer) {
        if (this.observers.contains(observer)) {
            return;
        }
        this.observers.add(observer);
    }

    public void unRegistObserver(final CalendarDayObserver observer) {
        if (this.observers.contains(observer)) {
            this.observers.remove(observer);
        }
    }

    public void dayChanged(final CalendarCell cell) {
        for (CalendarDayObserver observer : this.observers) {
            observer.dayChanged(cell);
        }
    }

    /**
     * 获取这个月一共需要多个格子，包含上个月的末尾，不包含下个月的开头
     * 
     * @return
     */
    public int getCount() {
        int c = this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                + getDayOfWeek(this.calendar.get(Calendar.DAY_OF_WEEK));
        int mod = c % 7;
        if (mod == 0) {
            return c;
        }
        return ((c / 7) + 1) * 7;
    }

    /**
     * 获取当前顺序的周几（周一：0， 周日：6）
     * 
     * @param before
     * @return
     */
    public int getDayOfWeek(int before) {
        int after = (before - 2) % 7;
        if (after < 0) {
            after = 6;
        }
        return after;
    }

    public String getWeek(int pos) {
        return WEEKS[pos % 7];
    }

    public String getDay(int pos) {

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
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
        int dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

        int firstDayPosition = (dayOfMonth / 7) * 7 + dayOfWeek + 7;

        // that day at pos
        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        cal.add(Calendar.DAY_OF_YEAR, pos - firstDayPosition);
        int thatDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int thatMonth = cal.get(Calendar.MONTH);
        int thatYear = cal.get(Calendar.YEAR);
        // Log.d("hy", "center:(" + dayOfMonth + "," + firstDayPosition + ")" +
        // "\tpos:(" + thatDayOfMonth + "," + pos
        // + ")");

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

        } else if (cal.getTime().before(Calendar.getInstance().getTime()) && cell.getMood() == Mood.UNKNOWN) {
            // 所有之前没有改过的都可以改一次
            cell.setCanChange(true);
        }

        return cell;
    }
}
