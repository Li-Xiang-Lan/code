package com.example.junweiliu.hanzicode;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by junweiliu on 16/5/4.
 */
public class BorderTextView extends TextView {
    /**
     * ����
     */
    private Paint mPaint;

    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        //  ���߿���Ϊ��ɫ
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((float) 3.0);
    }

    /**
     * ���ñ߿���ɫ
     *
     * @param color
     */
    public void setBorderColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * ���ñ߿����
     *
     * @param size
     */
    public void setBorderStrokeWidth(float size) {
        mPaint.setStrokeWidth(size);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  ��TextView��4����
        canvas.drawLine(0, 0, this.getWidth(), 0, mPaint);
        canvas.drawLine(0, 0, 0, this.getHeight(), mPaint);
        //  �ұ���
        //  canvas.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight(), mPaint);
        canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), mPaint);
        super.onDraw(canvas);
    }

}
