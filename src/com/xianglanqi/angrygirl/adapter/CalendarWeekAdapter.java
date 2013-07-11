package com.xianglanqi.angrygirl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xianglanqi.angrygirl.R;
import com.xianglanqi.angrygirl.data.CalendarData;
import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.CellType;

public class CalendarWeekAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public CalendarWeekAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 7;
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
        final CalendarCell cell = CalendarData.getIntance().getCell(pos);
        if (cell.getCellType() == CellType.CELL_WEEK) {
            return loadCalendarWeek(pos, convertView, cell);
        }
        return null;
    }

    private View loadCalendarWeek(int pos, View convertView, CalendarCell cell) {
        convertView = inflater.inflate(R.layout.layout_calendar_week, null);
        TextView tvWeek = (TextView) convertView.findViewById(R.id.textview_calendar_week);
        tvWeek.setText(cell.getText());
        tvWeek.setTag(cell);
        return convertView;
    }

}
