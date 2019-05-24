package com.example.junweiliu.hanzicode;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.hanzicode.R;

/**
 * Created by junweiliu on 16/5/9.
 */
public class CodeView extends ImageView {
    /**
     * ������
     */
    private final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    /**
     * ���ֻ���
     */
    private Paint hzPaint;
    /**
     * �������
     */
    private Paint dbPaint;
    /**
     * ���ֻ�����ɫ
     */
    private int hzColor;
    /**
     * ���������ɫ
     */
    private int dbColor;
    /**
     * ������ĸ���,Ĭ��30��
     */
    private int DEFAULT_DBSIZE;
    /**
     * Ĭ�ϻ�����ɫ
     */
    private int DEFAULT_DBCOLOR, DEFAULT_HZCOLOR;
    /**
     * ������������ɵ�λ��
     */
    private float dbRandomX, dbRandomY;
    /**
     * ��֤������
     */
    private List<String> codeList;
    /**
     * ���
     */
    private Random random = new Random();
    /**
     * ��������
     */
    private Rect mBounds;
    /**
     * �������ִ�С
     */
    private int dbTextSize;
    /**
     * ��֤�����ִ�С
     */
    private int hzTextSize;
    /**
     * ������ת����,Ĭ��Ϊ6
     */
    private int rotate;


    public CodeView(Context context) {
        this(context, null);
    }

    public CodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    ;

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // ��ȡ�Զ�������
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CodeView);
        DEFAULT_DBSIZE = ta.getInteger(R.styleable.CodeView_disturbSize, 30);
        dbTextSize = DisplayUtil.sp2px(context, ta.getDimensionPixelSize(R.styleable.CodeView_disturbTextSize, 10));
        hzTextSize = DisplayUtil.sp2px(context, ta.getDimensionPixelSize(R.styleable.CodeView_codeTextSize, 20));
        DEFAULT_DBCOLOR = ta.getColor(R.styleable.CodeView_disturbTextColor, 0);
        DEFAULT_HZCOLOR = ta.getColor(R.styleable.CodeView_codeTextColor, 0);
        rotate = ta.getInteger(R.styleable.CodeView_rotate, 6);
        init();

    }

    /**
     * ��ʼ��
     */
    private void init() {
        // ��֤��������ɫһ��,ֻ��Ҫ����һ��
        if (0 == DEFAULT_HZCOLOR) {
            hzColor = Util.randomDarkColor();
        } else {
            hzColor = DEFAULT_HZCOLOR;
        }
        mBounds = new Rect();
        codeList = new ArrayList<String>();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap bp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);
        String dbCode = createDBCode();
        // ���Ƹ�����
        for (int i = 0; i < dbCode.length(); i++) {
            randomDBStyele();
            randomDBPosition();
            c.drawText(dbCode.charAt(i) + "", dbRandomX, dbRandomY, dbPaint);
        }
        canvas.drawBitmap(bp, 0, 0, dbPaint);
        // ������֤����
        if (null != codeList && codeList.size() > 0) {
            for (int i = 0; i < codeList.size(); i++) {
                randomHZStyle();
                canvas.save();
                canvas.rotate((int) (Math.floor(Math.random() * rotate + 1)
                        - Math.floor(Math.random() * rotate * 2 + 1)));
                canvas.drawText(codeList.get(i), mBounds.width() * (i + 1), getHeight() / 2 + mBounds.height() / 2 + rotate, hzPaint);
                canvas.restore();
            }
        }
//        super.onDraw(canvas);
    }

    /**
     * ����
     */
    public void reSet(List<String> codes) {
        // ���ú��ֻ�����ɫ
        if (0 == DEFAULT_HZCOLOR) {
            hzColor = Util.randomDarkColor();
        } else {
            hzColor = DEFAULT_HZCOLOR;
        }
        this.codeList.clear();
        this.codeList = codes;
        invalidate();
    }

    /**
     * ������֤������
     *
     * @param codes
     */
    public void setCode(List<String> codes) {
        this.codeList = codes;
        invalidate();
    }

    /**
     * ������ɺ��ֻ�����ʽ
     */
    private void randomHZStyle() {
        hzPaint = new Paint();
//        hzPaint.setAntiAlias(true);
        hzPaint.setColor(hzColor);
        hzPaint.setFakeBoldText(random.nextBoolean());
        hzPaint.setTextSize(hzTextSize);
        hzPaint.getTextBounds("һ", 0, "һ".length(), mBounds);
    }


    /**
     * ������ɸ��������ʽ
     */
    private void randomDBStyele() {
        if (0 == DEFAULT_DBCOLOR) {
            dbColor = Util.randomDarkColor();
        } else {
            dbColor = DEFAULT_DBCOLOR;
        }
        dbPaint = new Paint();
//        dbPaint.setAntiAlias(true);
        dbPaint.setColor(dbColor);
        dbPaint.setTextSize(dbTextSize);
    }

    /**
     * ������ɸ��������ʾλ��
     */
    private void randomDBPosition() {
        dbRandomX = (float) Math.random() * getWidth();
        dbRandomY = (float) Math.random() * getHeight();
    }

    /**
     * ����������
     *
     * @return
     */
    private String createDBCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < DEFAULT_DBSIZE; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

}

