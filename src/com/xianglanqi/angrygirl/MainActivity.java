package com.xianglanqi.angrygirl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xianglanqi.angrygirl.adapter.CalendarDayAdapter;
import com.xianglanqi.angrygirl.adapter.CalendarWeekAdapter;
import com.xianglanqi.angrygirl.data.CalendarDB;
import com.xianglanqi.angrygirl.data.CalendarData;
import com.xianglanqi.angrygirl.data.WXAdapter;
import com.xianglanqi.angrygirl.model.CalendarCell;
import com.xianglanqi.angrygirl.model.Mood;
import com.xianglanqi.angrygirl.observer.CalendarDayObserver;

public class MainActivity extends Activity implements CalendarDayObserver {

	private GridView gridViewCalendarDay;

	private CalendarDayAdapter adapterCalendarDay;

	private GridView gridViewCalendarWeek;

	private CalendarWeekAdapter adapterCalendarWeek;

	private TextView textViewCalendarMonth;

	private TextView textViewPreMonth;

	private TextView textViewPostMonth;

	private CalendarDB calendarDB;

	private TextView textViewCalendarDayDescription;

	private Button buttonShareToTimeline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// MobclickAgent.setDebugMode(true);

		setContentView(R.layout.activity_main);

		WXAdapter.getInstance().regToWX(this);

		calendarDB = new CalendarDB(this);

		gridViewCalendarWeek = (GridView) findViewById(R.id.gridview_calendar_week);
		adapterCalendarWeek = new CalendarWeekAdapter(this);
		gridViewCalendarWeek.setAdapter(adapterCalendarWeek);

		gridViewCalendarDay = (GridView) findViewById(R.id.gridview_calendar_day);
		adapterCalendarDay = new CalendarDayAdapter(this);
		gridViewCalendarDay.setAdapter(adapterCalendarDay);

		textViewCalendarMonth = (TextView) findViewById(R.id.textview_calendar_month);
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

		this.buttonShareToTimeline = (Button) findViewById(R.id.button_share_to_timeline);
		if (WXAdapter.getInstance().isSupportTimeline()) {
			final OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MainActivity.this.textViewCalendarDayDescription.setVisibility(View.INVISIBLE);
					MainActivity.this.buttonShareToTimeline.setVisibility(View.INVISIBLE);
					shotscreenAndSend();
				}
			};
			this.buttonShareToTimeline.setOnClickListener(listener);
		} else {
			this.buttonShareToTimeline.setEnabled(false);
		}
		this.textViewCalendarDayDescription = (TextView) findViewById(R.id.textview_day_description);
		this.textViewCalendarDayDescription
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(MainActivity.this,
								EditDescriptionActivity.class);
						intent.putExtra("cell",
								(CalendarCell) textViewCalendarDayDescription
										.getTag());
						startActivity(intent);
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		changeMonth();

		this.buttonShareToTimeline.setVisibility(View.VISIBLE);
		MobclickAgent.onResume(this);
		CalendarData.getIntance().registObserver(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		CalendarData.getIntance().unRegistObserver(this);
	}

	@SuppressLint("SimpleDateFormat")
	private void setMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月");
		textViewCalendarMonth.setText(format.format(CalendarData.getIntance()
				.getCurrentCalendar().getTime()));
		int year = CalendarData.getIntance().getCurrentCalendar()
				.get(Calendar.YEAR);
		int month = CalendarData.getIntance().getCurrentCalendar()
				.get(Calendar.MONTH);
		CalendarData.getIntance().setMoodMap(calendarDB.getMood(year, month));
	}

	private void changeMonth() {
		setMonth();
		this.adapterCalendarDay.notifyDataSetChanged();
		this.textViewCalendarDayDescription.setVisibility(View.GONE);
	}

	@SuppressWarnings("deprecation")
	private void shotscreenAndSend() {
		View view = getWindow().getDecorView();
		Display display = this.getWindowManager().getDefaultDisplay();
		view.layout(0, 0, display.getWidth(), display.getHeight());
		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

		WXAdapter.getInstance().sendImageToTimeline("", bitmap);

		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}

	@Override
	public void dayChanged(CalendarCell cell) {
		if (cell.getMood() == Mood.UNKNOWN) {
			return;
		}
		this.textViewCalendarDayDescription.setVisibility(View.VISIBLE);
		String prefix = String.format("[点我] %d年%d月%d日: %s", cell.getYear(),
				cell.getMonth() + 1, cell.getDay(), cell.getDescription());
		this.textViewCalendarDayDescription.setText(prefix);
		this.textViewCalendarDayDescription.setTag(cell);
	}
}
