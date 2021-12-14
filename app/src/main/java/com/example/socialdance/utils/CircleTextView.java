package com.example.socialdance.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

public class CircleTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int defaultColor = Color.parseColor("#FF7800");
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private Paint mPaint;


    public CircleTextView(Context context) {
        super(context);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int size = Math.max(height, width);
        mCenterX = mCenterY = mRadius = size/2;
        setMeasuredDimension(size, size);

    }

    public void setColorBackground(int color) {
        defaultColor = color;
        mPaint.setColor(defaultColor);
        this.invalidate();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setGravity(Gravity.CENTER);
        setColorBackground(defaultColor);
    }
}
