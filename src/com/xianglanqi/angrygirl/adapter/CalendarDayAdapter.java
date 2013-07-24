package com.xianglanqi.angrygirl.adapter;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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

    private Context context;

    private LayoutInflater inflater;

    private CalendarDB calendarDB;

    private CalendarCell lastCell;

    public CalendarDayAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.calendarDB = new CalendarDB(context);
        this.context = context;
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
            try {
                imgDay.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(
                        cell.getMood().getCode() + ".png", AssetManager.ACCESS_STREAMING)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (cell.isToday()) {
                imgDay.setImageResource(R.drawable.bg_click_here);
            } else {
            }
        }

        convertView.setLongClickable(true);
        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                final CalendarCell cell = (CalendarCell) view.getTag();
                if (cell.getCellType() != CellType.CELL_DAY || !cell.isCanChange()) {
                    return true;
                }
                SharedPreferences sp = context.getSharedPreferences("user_setting", Context.MODE_PRIVATE);
                String group = Mood.getCurrentGroup(sp);
                String newGroup = group.equals(Mood.DEFAULT_GROUP) ? Mood.NIU_GROUP : Mood.DEFAULT_GROUP;
                Mood.setCurrentGroup(sp, newGroup);
                Mood mood = Mood.getMood(newGroup, 0);
                changeMood(cell, imgDay, mood);
                lastCell = cell;
                return true;
            }
        });

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
                    CalendarData.getIntance().daySelected(cell);

                } else {
                    String group = Mood.getCurrentGroup(context.getSharedPreferences("user_setting",
                            Context.MODE_PRIVATE));
                    Mood mood = Mood.getMood(group, (cell.getMood().getIndex() + 1) % Mood.getCount(group));
                    changeMood(cell, imgDay, mood);
                }
                lastCell = cell;

            }
        });

        return convertView;
    }

    private void changeMood(final CalendarCell cell, final ImageView imgDay, final Mood mood) {

        cell.setMood(mood);
        cell.setUpdatedTime(new Date());
        calendarDB.changeMood(cell);

        try {
            imgDay.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(mood.getCode() + ".png",
                    AssetManager.ACCESS_STREAMING)));
            CalendarData.getIntance().dayChanged(cell);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
