package com.xianglanqi.angrygirl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CalendarWeekCellLayout extends RelativeLayout {

    public CalendarWeekCellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec * 3 / 5);

        measureChildren(widthMeasureSpec, widthMeasureSpec * 3 / 5);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * 3 / 5;

        setMeasuredDimension(width, height);
    }
}
