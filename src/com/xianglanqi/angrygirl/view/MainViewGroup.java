package com.xianglanqi.angrygirl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MainViewGroup extends ViewGroup {

    public MainViewGroup(Context context) {
        super(context);
    }

    public MainViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width, height);

        this.measureChildren(width, height);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        int left = 0;
        View child;
        for (int index = 0; index < this.getChildCount(); index++) {
            child = this.getChildAt(index);
            child.layout(left, 0, left + this.getMeasuredWidth(), this.getMeasuredHeight());
            left += this.getMeasuredWidth();
        }
    }

}
