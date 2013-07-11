package com.xianglanqi.angrygirl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CalendarDayCellLayout extends RelativeLayout {

    public CalendarDayCellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        setMeasuredDimension(width, height);
    }
}
