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
        float scale = 0.4f;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * scale);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, mode);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // Log.d("hy", String.format("onMeasure: (%d,%d)", widthMeasureSpec,
        // heightMeasureSpec));

        setMeasuredDimension(width, height);
        // Log.d("hy", String.format("setMeasuredDimension: (%d,%d)", width,
        // height));

    }
}
