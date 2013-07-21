package com.xianglanqi.angrygirl.adapter;

import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianglanqi.angrygirl.R;
import com.xianglanqi.angrygirl.data.CalendarDB;
import com.xianglanqi.angrygirl.data.CalendarData;
import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.CellType;
import com.xianglanqi.angrygirl.model.Mood;

public class CalendarDayAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private CalendarDB calendarDB;

    private CalendarCell lastCell;

    public CalendarDayAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.calendarDB = new CalendarDB(context);
    }

    @Override
    public int getCount() {
        return CalendarData.getIntance().getCount();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        final CalendarCell cell = CalendarData.getIntance().getCell(pos + 7);
        return loadCalendarDay(pos, convertView, cell);
    }

    private View loadCalendarDay(int pos, View view, CalendarCell cell) {

        int resourceId = 0;
        // 是否可见
        if (cell.getCellType() == CellType.CELL_DAY) {
            resourceId = R.layout.layout_calendar_day;

        } else {
            resourceId = R.layout.layout_calendar_day_unenable;
        }
        final View convertView = inflater.inflate(resourceId, null);
        convertView.setTag(cell);

        final TextView tvDay = (TextView) convertView.findViewById(R.id.textview_calendar_day);
        final ImageView imgDay = (ImageView) convertView.findViewById(R.id.imageview_calendar_day);
        tvDay.setText(cell.getText());

        // 显示什么表情
        if (cell.getMood() != Mood.UNKNOWN) {
            imgDay.setImageResource(cell.getMood().getResource());
        } else {
            if (cell.isToday()) {
                imgDay.setImageResource(R.drawable.bg_click_here);
            } else {
            }
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                final CalendarCell cell = (CalendarCell) view.getTag();
                if (cell.getCellType() != CellType.CELL_DAY) {
                    return;
                }

                // 在不同天之间切换的时候，不做表情的改变；只有连续点同一天的时候，才改表情
                if (!cell.isCanChange() || (cell != lastCell && cell.getMood() != Mood.UNKNOWN)) {
                    // return;

                } else {
                    Mood mood = Mood.getMood((cell.getMood().getIndex() + 1) % Mood.NUMBER);
                    cell.setMood(mood);
                    cell.setUpdatedTime(new Date());
                    calendarDB.changeMood(cell);

                    imgDay.setImageResource(mood.getResource());
                }

                lastCell = cell;
                CalendarData.getIntance().dayChanged(cell);
            }
        });

        return convertView;
    }

}
