package com.xianglanqi.angrygirl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xianglanqi.angrygirl.adapter.CalendarDayAdapter;
import com.xianglanqi.angrygirl.adapter.CalendarWeekAdapter;
import com.xianglanqi.angrygirl.data.CalendarDB;
import com.xianglanqi.angrygirl.data.CalendarData;
import com.xianglanqi.angrygirl.data.WXAdapter;

public class MainActivity extends Activity {

    private GridView gridViewCalendarDay;

    private CalendarDayAdapter adapterCalendarDay;

    private GridView gridViewCalendarWeek;

    private CalendarWeekAdapter adapterCalendarWeek;

    private TextView textViewCalendarMonth;

    private TextView textViewPreMonth;

    private TextView textViewPostMonth;

    private CalendarDB calendarDB;

    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MobclickAgent.setDebugMode(true);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);

        WXAdapter.getInstance().regToWX(this);

        calendarDB = new CalendarDB(this);

        gridViewCalendarWeek = (GridView) findViewById(R.id.gridview_calendar_week);
        adapterCalendarWeek = new CalendarWeekAdapter(this);
        gridViewCalendarWeek.setAdapter(adapterCalendarWeek);

        gridViewCalendarDay = (GridView) findViewById(R.id.gridview_calendar_day);
        adapterCalendarDay = new CalendarDayAdapter(this);
        gridViewCalendarDay.setAdapter(adapterCalendarDay);

        textViewCalendarMonth = (TextView) findViewById(R.id.textview_calendar_month);
        setMonth();

        textViewPreMonth = (TextView) findViewById(R.id.textview_pre_month);
        textViewPreMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarData.getIntance().preMonth();
                changeMonth();
            }
        });

        textViewPostMonth = (TextView) findViewById(R.id.textview_post_month);
        textViewPostMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarData.getIntance().postMonth();
                changeMonth();
            }
        });

        shareButton = (Button) findViewById(R.id.button_share);
        if (WXAdapter.getInstance().isSupportTimeline()) {
            shareButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    shotscreenAndSend();
                }
            });
        } else {
            //shareButton.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @SuppressLint("SimpleDateFormat")
    private void setMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月");
        textViewCalendarMonth.setText(format.format(CalendarData.getIntance().getCurrentCalendar().getTime()));
        int year = CalendarData.getIntance().getCurrentCalendar().get(Calendar.YEAR);
        int month = CalendarData.getIntance().getCurrentCalendar().get(Calendar.MONTH);
        CalendarData.getIntance().setMoodMap(calendarDB.getMood(year, month));
    }

    private void changeMonth() {
        setMonth();
        adapterCalendarDay.notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    private void shotscreenAndSend() {
        View view = getWindow().getDecorView();
        Display display = this.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

        WXAdapter.getInstance().sendImageToTimeline("哈哈哈", bitmap);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
