package com.example.junweiliu.hanzicode;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

import com.example.hanzicode.R;

/**
 * Created by junweiliu on 16/5/3.
 */
public class HanZiView extends ImageView {
    /**
     * �ָ���
     */
    private static final int WIDTH = 9, HEIGHT = 9;
    /**
     * ������
     */
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    /**
     * λͼ����
     */
    private Bitmap mBitmap;
    /**
     * ��׼����������
     */
    private float[] matrixOriganal = new float[COUNT * 2];
    /**
     * �任�����������
     */
    private float[] matrixMoved = new float[COUNT * 2];

    private float clickX, clickY;// ������Ļʱ��ָ��xy����

    private Paint origPaint, movePaint, linePaint;// ��׼�㡢�任����߶εĻ���Paint
    /**
     * ���������ػ�
     */
    private boolean needDraw = true;
    /**
     * ����
     */
    private Paint mPaint;
    /**
     * ���ִ�С
     */
    private int mTextSize;
    /**
     * ����
     */
    private String mText = "��";
    /**
     * ������ɫ
     */
    private int mTextColor;
    /**
     * ��������
     */
    private Rect mBounds;
    /**
     * ���ܵ����׿��ֵ
     */
    private int mBorderWidthSize;
    /**
     * ���ܵ����׸߶�ֵ
     */
    private int mBorderHeightSize;
    /**
     * ��ȡŤ��������λ��
     */
    private float mRandomX, mRandomY;
    /**
     * ������ת�ĽǶ�
     */
    private int DEFAULT_ROTATE_SIZE;
    /**
     * Ť����ϵ��
     */
    private float SMUDGE_COEFFICIENT;
    /**
     * ��ת�ĽǶ�
     */
    private int rotate;
    /**
     * ���
     */
    private Random r = new Random();

    public HanZiView(Context context) {
        this(context, null);
    }

    public HanZiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HanZiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // ʵ�����ʲ�������ɫ
        origPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        origPaint.setColor(0x660000FF);
        movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setColor(0x99FF0000);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFFFFFB00);
        // ��ȡ�Զ�������
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HanZiView);
        mBorderWidthSize = ta.getDimensionPixelSize(R.styleable.HanZiView_borderWidthSize, DisplayUtil.dip2px(context, 30));
        mBorderHeightSize = ta.getDimensionPixelSize(R.styleable.HanZiView_borderHeightSize, DisplayUtil.dip2px(context, 20));
        DEFAULT_ROTATE_SIZE = ta.getInteger(R.styleable.HanZiView_rotateSize, 40);
        SMUDGE_COEFFICIENT = ta.getFloat(R.styleable.HanZiView_smudgeCoefficieng, 1.0f);
        mTextSize = ta.getDimensionPixelSize(R.styleable.HanZiView_textSize, DisplayUtil.sp2px(context, 20));
        mTextColor = ta.getColor(R.styleable.HanZiView_textColor, 0);
        ta.recycle();
        init();

    }

    /**
     * ��ʼ������
     */
    private void init() {
        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        if (0 != mTextColor) {
            mPaint.setColor(mTextColor);
        } else {
            mPaint.setColor(Util.randomDarkColor());
        }
        // ���ʹ�ô���
        mPaint.setFakeBoldText(r.nextBoolean());
        mBounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);

    }

    /**
     * �������Ť��λ�õ�XY����
     */
    private void initRandomXY() {
        mRandomX = (float) Math.random() * getWidth();
        mRandomY = (float) Math.random() * getHeight();
    }

    /**
     * ������ʾ������
     *
     * @param text
     */
    public void setText(String text) {
        this.mText = text;
        needDraw = true;
        invalidate();

    }

    /**
     * ������ʾ�����ֵ���ɫ
     *
     * @param color
     */
    public void setTextColor(int color) {
        mPaint.setColor(color);
        needDraw = true;
        invalidate();

    }

    /**
     * ������ʾ���ֵ���ɫ��ARGB
     *
     * @param A
     * @param R
     * @param G
     * @param B
     */
    public void setTextColor(int A, int R, int G, int B) {
        mPaint.setARGB(A, R, G, B);
        needDraw = true;
        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                width = getPaddingLeft() + getPaddingRight() + specSize + mBorderWidthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = getPaddingLeft() + getPaddingRight() + mBounds.width() + mBorderWidthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getPaddingLeft() + getPaddingRight() + mBounds.width() + mBorderWidthSize;
                break;
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                height = getPaddingTop() + getPaddingBottom() + specSize + mBorderHeightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = getPaddingTop() + getPaddingBottom() + mBounds.height() + mBorderHeightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getPaddingTop() + getPaddingBottom() + mBounds.height() + mBorderHeightSize;
                break;

        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (needDraw) {
            // �����������ֵ�ͼƬ
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(mBitmap);
            c.drawText(mText, getWidth() / 2 - mBounds.width() / 2, mBounds.height() / 2 + getHeight() / 2, mPaint);
            // ��ʼ����������
            int index = 0;
            for (int y = 0; y <= HEIGHT; y++) {
                float fy = mBitmap.getHeight() * y / HEIGHT;

                for (int x = 0; x <= WIDTH; x++) {
                    float fx = mBitmap.getWidth() * x / WIDTH;
                    setXY(matrixMoved, index, fx, fy);
                    setXY(matrixOriganal, index, fx, fy);
                    index += 1;
                }
            }
            initRandomXY();
            smudgeMatrix();
            // �����ת�Ƕ�
            rotate = (int) (Math.floor(Math.random() * DEFAULT_ROTATE_SIZE + 1)
                    - Math.floor(Math.random() * DEFAULT_ROTATE_SIZE * 2 + 1));
        }
        needDraw = false;
        // ��������λͼ
        canvas.drawBitmapMesh(rotateBitmap(rotate, mBitmap), WIDTH, HEIGHT, matrixMoved, 0, null, 0, null);
        // ���Ʋο�Ԫ��
//        drawGuide(canvas);
    }

    /**
     * ������������
     *
     * @param array ��������
     * @param index ��ʶֵ
     * @param x     x����
     * @param y     y����
     */
    private void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    /**
     * ���Ʋο�Ԫ��
     *
     * @param canvas ����
     */
    private void drawGuide(Canvas canvas) {
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixOriganal[i + 0];
            float y = matrixOriganal[i + 1];
            canvas.drawCircle(x, y, 4, origPaint);

            float x1 = matrixOriganal[i + 0];
            float y1 = matrixOriganal[i + 1];
            float x2 = matrixMoved[i + 0];
            float y2 = matrixMoved[i + 1];
            canvas.drawLine(x1, y1, x2, y2, origPaint);
        }

        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixMoved[i + 0];
            float y = matrixMoved[i + 1];
            canvas.drawCircle(x, y, 4, movePaint);
        }

        canvas.drawCircle(clickX, clickY, 6, linePaint);
    }

    /**
     * ����任��������
     */
    private void smudgeMatrix() {
        for (int i = 0; i < COUNT * 2; i += 2) {

            float xOriginal = matrixOriganal[i + 0];
            float yOriginal = matrixOriganal[i + 1];

            float dist_random_to_origin_x = mRandomX - xOriginal;
            float dist_random_to_origin_y = mRandomY - yOriginal;
//            float dist_random_to_origin_x = clickX - xOriginal;
//            float dist_random_to_origin_y = clickY - yOriginal;

            float kv_kat = dist_random_to_origin_x * dist_random_to_origin_x + dist_random_to_origin_y * dist_random_to_origin_y;

            float pull = (float) (1000 * SMUDGE_COEFFICIENT / kv_kat / Math.sqrt(kv_kat));

            if (pull >= 1) {
                matrixMoved[i + 0] = mRandomX;
                matrixMoved[i + 1] = mRandomY;
//                matrixMoved[i + 0] = clickX;
//                matrixMoved[i + 1] = clickY;
            } else {
                matrixMoved[i + 0] = xOriginal + dist_random_to_origin_x * pull;
                matrixMoved[i + 1] = yOriginal + dist_random_to_origin_y * pull;
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        clickX = event.getX();
//        clickY = event.getY();
//        initRandomXY();
//        smudgeMatrix();
//        invalidate();
//        return true;
//    }

    /**
     * ��תͼƬ
     *
     * @param degree
     * @param bitmap
     * @return
     */
    public Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bm;
    }
}
