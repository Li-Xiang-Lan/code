package com.example.junweiliu.hanzicode.open;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.junweiliu.hanzicode.R;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class OpenActivity extends AppCompatActivity {
    LinearLayout layout;
    TextView text;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        layout= (LinearLayout) findViewById(R.id.layout);
        text= (TextView) findViewById(R.id.text);
        list= (ListView) findViewById(R.id.list);
        
        list.setAdapter(new MyAdapter());
    }


    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return "111";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null==convertView)
                convertView=LayoutInflater.from(OpenActivity.this).inflate(R.layout.item_layout,parent,false);
            return convertView;
        }
    }

    boolean show=false;
    public void open(View view) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible

        animation.setDuration(500); // duration - half a second

        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate

        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely

        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        final TextView btn = (TextView) findViewById(R.id.a);

        btn.startAnimation(animation);


//        //View是否显示的标志
//        show = !show;
//        //属性动画对象
//        ValueAnimator va ;
//        int height = text.getHeight();
//        Log.e("qwer",height+"...");
//        if(show){
//            //显示view，高度从0变到height值
//            va = ValueAnimator.ofInt(0,height);
//        }else{
//            //隐藏view，高度从height变为0
//            va = ValueAnimator.ofInt(height,0);
//        }
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                //获取当前的height值
//                int h =(Integer)valueAnimator.getAnimatedValue();
//                //动态更新view的高度
//                layout.getLayoutParams().height = h;
//                layout.requestLayout();
//            }
//        });
//        va.setDuration(500);
//        //开始动画
//        va.start();
    }
}
