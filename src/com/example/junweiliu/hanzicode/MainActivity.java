package com.example.junweiliu.hanzicode;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.hanzicode.R;

public class MainActivity extends Activity {
    /**
     * ��֤�𰸿�
     */
    private AnswerLayout mAnswerLayout;
    /**
     * ��֤���ֿ�
     */
    private GridView mGridView;
    /**
     * ��֤��
     */
    private CodeView mCodeView;
    /**
     * ���ð�ť
     */
    private Button mResetBtn;
    /**
     * ������
     */
    private HanZiCodeAdapter hzAdapter;
    /**
     * ��ȡ�������ֱ���
     */
    private List<String> cHanZiList;
    /**
     * ��ȡ��������
     */
    private List<String> mHanZiList;
    /**
     * ѡ�������
     */
    private List<String> mChooseHZList;
    /**
     * ��ȷ��
     */
    private List<String> mCorrectList;
    /**
     * ��֤������ɫ
     */
    private int mColor;
    /**
     * ���Լ���
     */
    private int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initGridView();
    }

    /**
     * ��ʼ��gridview
     */
    private void initGridView() {
        mGridView = (GridView) findViewById(R.id.gv_hz);
        hzAdapter = new HanZiCodeAdapter();
        mGridView.setAdapter(hzAdapter);
        Util.calGridViewWidthAndHeigh(3, mGridView);
    }

    /**
     * ��ʼ������
     */
    private void initData() {
        mHanZiList = new ArrayList<String>();
        cHanZiList = new ArrayList<String>();
        mChooseHZList = new ArrayList<String>();
        mCorrectList = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            mHanZiList.add(Util.getRandomJianHan(1));
        }
        cHanZiList.addAll(mHanZiList);
        // ��Ӳ��ظ�������
        for (int i = 0; i < 4; i++) {
            String hz = cHanZiList.get((int) (Math.random() * cHanZiList.size()));
            cHanZiList.remove(hz);
            mCorrectList.add(hz);
        }
        mColor = Util.randomColor();
//        Log.e("main", "��ɫֵΪ" + mColor + "\n��ȡ��������Ϊ\n" + mCorrectList.get(0) + "\n" + mCorrectList.get(1) + "\n" + mCorrectList.get(2) + "\n" + mCorrectList.get(3));
    }


    /**
     * ��ʼ���ؼ�
     */
    private void initView() {
        mAnswerLayout = (AnswerLayout) findViewById(R.id.al_mal);
        mAnswerLayout.setCorrectAnswers(mCorrectList);
        mAnswerLayout.setCheckAnswerListener(new AnswerLayout.CheckAnswerListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "�ɹ�", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(MainActivity.this, "ʧ��", Toast.LENGTH_SHORT).show();
            }
        });
        mCodeView = (CodeView) findViewById(R.id.cv_hz);
        mCodeView.setCode(mCorrectList);
        mResetBtn = (Button) findViewById(R.id.btn_reset);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                mAnswerLayout.reSet(mCorrectList);
                mCodeView.reSet(mCorrectList);
                hzAdapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * ����չʾ��������
     */
    class HanZiCodeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mHanZiList.size();
        }

        @Override
        public Object getItem(int i) {
            return mHanZiList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            if (null == view) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.hanzi_item, null, false);
                holder.hzImg = (HanZiView) view.findViewById(R.id.hz_item);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //����holder
            holder.hzImg.setText(mHanZiList.get(i));
            holder.hzImg.setTextColor(mColor);
            holder.hzImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mChooseHZList.size() < 4) {
                        mChooseHZList.add(mHanZiList.get(i));
                        mAnswerLayout.setAnswers(mChooseHZList);
                    }
//                    Toast.makeText(MainActivity.this, "�����" + mHanZiList.get(i), Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        class ViewHolder {
            HanZiView hzImg;
        }

    }
}
