package com.example.junweiliu.hanzicode;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by junweiliu on 16/5/9.
 */
public class Util {


    /**
     * ��ȡ�������ɫ
     *
     * @return
     */
    public static int randomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.argb(255, red, green, blue);
    }

    /**
     * ��ȡ����İ�ɫ
     *
     * @return
     */
    public static int randomDarkColor() {
        int red = (int) (Math.random() * 100 + 56);
        int green = (int) (Math.random() * 100 + 56);
        int blue = (int) (Math.random() * 100 + 56);
        return Color.argb(255, red, green, blue);
    }

    /**
     * ��ȡָ�����������������
     *
     * @param len int
     * @return String
     */
    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // ����ߵ�λ
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //��ȡ��λֵ
            lowPos = (161 + Math.abs(random.nextInt(93))); //��ȡ��λֵ
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBk"); //ת������
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    /**
     * ����GridView���
     *
     * @param gridView
     */
    public static void calGridViewWidthAndHeigh(int numColumns, GridView gridView) {

        // ��ȡGridView��Ӧ��Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()�������������Ŀ
            View listItem = listAdapter.getView(i, null, gridView);
            // ��������View �Ŀ��
            listItem.measure(0, 0);

            if ((i + 1) % numColumns == 0) {
                // ͳ������������ܸ߶�
                totalHeight += listItem.getMeasuredHeight();
            }

            if ((i + 1) == len && (i + 1) % numColumns != 0) {
                // ͳ������������ܸ߶�
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        // �ʵ����һЩ�߶�,��ֹgridview�߶Ȳ���,���ֹ�����
        totalHeight += 60;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
        gridView.setLayoutParams(params);
    }
}
