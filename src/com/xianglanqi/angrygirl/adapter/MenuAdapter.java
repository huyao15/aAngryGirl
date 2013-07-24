package com.xianglanqi.angrygirl.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianglanqi.angrygirl.BackgroundUtil;
import com.xianglanqi.angrygirl.R;
import com.xianglanqi.angrygirl.data.CalendarDB;
import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.MenuCell;
import com.xianglanqi.angrygirl.model.Mood;

public class MenuAdapter extends BaseAdapter {

    private Context context;

    private CalendarDB calendarDB;

    private List<MenuCell> cells;

    public MenuAdapter(Context context) {
        this.context = context;
        this.calendarDB = new CalendarDB(context);
        initCells();
    }

    private void initCells() {
        this.cells = new ArrayList<MenuCell>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (int i = month; i > 0; i--) {
            if (i == month) {
                this.cells.add(new MenuCell(year));
            }
            this.cells.add(new MenuCell(year, i));
        }
        int minYear = this.calendarDB.getMinYear();
        if (minYear > -1 && minYear < year) {
            for (int i = year - 1; i >= Math.max(minYear, year - 2); i--) {
                for (int j = 12; j > 0; j--) {
                    if (j == 12) {
                        cells.add(new MenuCell(i));
                    }
                    this.cells.add(new MenuCell(i, j));
                }
            }
        }
    }

    @Override
    public int getCount() {
        return cells.size() + 1;
    }

    @Override
    public MenuCell getItem(int pos) {
        if (pos == 0) {
            return null;
        }
        return this.cells.get(pos - 1);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        final MenuCell cell = getItem(pos);
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_menucell, null);
            holder = new ViewHolder();
            holder.year = (TextView) convertView.findViewById(R.id.textview_year);
            holder.iconYear = (ImageView) convertView.findViewById(R.id.imageview_year_icon);
            holder.cellYear = (ViewGroup) convertView.findViewById(R.id.layout_year);
            holder.month = (TextView) convertView.findViewById(R.id.textview_month);
            holder.cellMonth = (ViewGroup) convertView.findViewById(R.id.layout_month);
            holder.moods = (ViewGroup) convertView.findViewById(R.id.gridview_month_mood);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == cell) {
            holder.year.setText("换背景图");
            int rst = BackgroundUtil.changeBackground(context, new ImageView[] { holder.iconYear });
            if (rst == -1) {
                holder.iconYear.setImageResource(R.drawable.bg_default);
            }
            holder.cellYear.setVisibility(View.VISIBLE);
            holder.cellMonth.setVisibility(View.GONE);
            return convertView;
        }

        if (cell.isParent()) {
            holder.year.setText(cell.getYear() + "年");
            holder.iconYear.setImageResource(R.drawable.icon_year);
            holder.cellYear.setVisibility(View.VISIBLE);
            holder.cellMonth.setVisibility(View.GONE);
        } else {
            holder.month.setText(cell.getMonth() + "月");
            holder.cellMonth.setVisibility(View.VISIBLE);
            holder.cellYear.setVisibility(View.GONE);

            holder.moods.removeAllViews();
            cell.getMoodCount().clear();
            Map<String, CalendarCell> cells = this.calendarDB.getMood(cell.getYear(), cell.getMonth() - 1);
            for (CalendarCell cc : cells.values()) {
                cell.addMood(cc.getMood(), 1);
            }

            for (int i = 0; i < cell.getMoodCount().size(); i++) {
                Mood mood = cell.getMood(i);
                int count = cell.getCount(mood);
                View view = (View) LayoutInflater.from(context).inflate(R.layout.layout_mood, null);
                ImageView moodPic = (ImageView) view.findViewById(R.id.imageview_mood_pic);
                TextView moodCount = (TextView) view.findViewById(R.id.textview_mood_count);
                try {
                    moodPic.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(mood.getCode() + ".png",
                            AssetManager.ACCESS_STREAMING)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                moodCount.setText("X" + count);
                holder.moods.addView(view);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView year;
        ImageView iconYear;
        ViewGroup cellYear;
        TextView month;
        ViewGroup cellMonth;
        ViewGroup moods;
    }

}
