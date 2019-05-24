package com.example.junweiliu.hanzicode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.hanzicode.R;

/**
 * Created by junweiliu on 16/5/4.
 */
public class AnswerLayout extends LinearLayout {
    /**
     * ����Ŀ��
     */
    private int pWidth;
    /**
     * ��Ҫ��֤�ֵĸ���(���ٸ���,Ĭ��Ϊ4��)
     */
    private int HANZI_TEST_SIZE;
    /**
     * ��Ҫ��������View����
     */
    private int childViewCount;
    /**
     * �ش�Ĵ��б�
     */
    private List<String> mAnswers;
    /**
     * ��ȷ��
     */
    private List<String> mCorrectAnswer;
    /**
     * ɾ����ť��Դ
     */
    private int deleteBtnSrc;
    /**
     * ���Ӹ�߿���ɫ
     */
    private int hanziBorderColor;
    /**
     * ���Ӹ�߿���
     */
    private float hanziBorderStrokeWidth;
    /**
     * ���ָ����ֵ���ɫ
     */
    private int hanziTextColor;
    /**
     * ���ָ����ֵĴ�С
     */
    private int hanziTextSize;

    /**
     * �ص��ӿ�
     */
    interface CheckAnswerListener {
        // �ɹ��ص�
        public void onSuccess();

        // ʧ�ܻص�
        public void onFail();

    }

    /**
     * �ص�
     */
    private CheckAnswerListener mListener;

    /**
     * ���ûص�
     *
     * @param listener
     */
    public void setCheckAnswerListener(CheckAnswerListener listener) {
        this.mListener = listener;
    }

    public AnswerLayout(Context context) {
        this(context, null);
    }

    public AnswerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // ��ȡ�Զ�������
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnswerLayout);
        deleteBtnSrc = ta.getResourceId(R.styleable.AnswerLayout_deleteBtnSrc, R.drawable.delete_btn_selector);
        hanziBorderColor = ta.getColor(R.styleable.AnswerLayout_hanziBorderColor, Color.GRAY);
        hanziBorderStrokeWidth = ta.getFloat(R.styleable.AnswerLayout_hanziBorderStrokeWidth, 3.0f);
        HANZI_TEST_SIZE = ta.getInt(R.styleable.AnswerLayout_hanziTestNum, 4);
        hanziTextColor = ta.getColor(R.styleable.AnswerLayout_hanziTextColor, Color.BLACK);
        hanziTextSize = ta.getDimensionPixelSize(R.styleable.AnswerLayout_hanziTextSize, 16);
        ta.recycle();
        init();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mAnswers.size() > HANZI_TEST_SIZE) {
            return;
        }
        if (mAnswers.size() == 0) {
            for (int i = 0; i < HANZI_TEST_SIZE; i++) {
                TextView tv = (TextView) getChildAt(i);
                tv.setText("");
            }
        } else {
            for (int i = 0; i < mAnswers.size(); i++) {
                TextView tv = (TextView) getChildAt(i);
                tv.setText(mAnswers.get(i));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (HANZI_TEST_SIZE <= 0) {
            return;
        }
        // ��Ҫ����һ��ɾ����ť,������view��������Ҫ��1
        childViewCount = HANZI_TEST_SIZE + 1;
        for (int i = 0; i < HANZI_TEST_SIZE; i++) {
            addView(makeItemView(i));
        }
        addView(makeDeleteView());
    }


    /**
     * ��ʼ������
     */
    public void init() {
        mAnswers = new ArrayList<String>();
        mCorrectAnswer = new ArrayList<String>();
    }

    /**
     * ��д�Ĵ�
     *
     * @param answers
     */
    public void setAnswers(List<String> answers) {
        this.mAnswers = answers;
        invalidate();
        if (compare(mAnswers, mCorrectAnswer) && mAnswers.size() == HANZI_TEST_SIZE) {
            if (null != mListener)
                mListener.onSuccess();
        } else if (mAnswers.size() == HANZI_TEST_SIZE) {
            if (null != mListener)
                mListener.onFail();
        }

    }

    /**
     * �ж�����list�Ƿ���ȫ��ͬ
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    private <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * ��ȷ��
     *
     * @param correctAnswers
     */
    public void setCorrectAnswers(List<String> correctAnswers) {
        this.mCorrectAnswer = correctAnswers;
    }

    /**
     * ����
     */
    public void reSet(List<String> correctAnswers) {
        mAnswers.clear();
        mCorrectAnswer = correctAnswers;
        invalidate();
    }

    /**
     * ����ɾ����ť
     *
     * @return
     */
    private View makeDeleteView() {
        ImageView deleteView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = pWidth / childViewCount;
        lp.height = pWidth / childViewCount;
        deleteView.setImageResource(deleteBtnSrc);
        deleteView.setScaleType(ImageView.ScaleType.FIT_XY);
        deleteView.setLayoutParams(lp);
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = HANZI_TEST_SIZE; i > 0; i--) {
                    TextView tv = (TextView) getChildAt(i - 1);
                    if (!"".equals(tv.getText())) {
                        mAnswers.remove(i - 1);
                        tv.setText("");
                        break;
                    }

                }
            }
        });
        return deleteView;
    }


    /**
     * ����������֤��
     *
     * @return
     */
    private View makeItemView(final int i) {
        final BorderTextView bv = new BorderTextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = pWidth / childViewCount;
        lp.height = pWidth / childViewCount;
        bv.setBorderColor(hanziBorderColor);
        bv.setTextSize(hanziTextSize);
        bv.setGravity(Gravity.CENTER);
        bv.setText("");
        bv.setLayoutParams(lp);
        bv.setTextColor(hanziTextColor);
        bv.setBorderStrokeWidth(hanziBorderStrokeWidth);
        return bv;
    }
}
