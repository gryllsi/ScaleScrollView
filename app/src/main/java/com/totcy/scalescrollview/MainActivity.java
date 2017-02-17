package com.totcy.scalescrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.totcy.salelibrary.HorizontalScaleScrollView;
import com.totcy.salelibrary.MultHorizontalScaleScrollView;

public class MainActivity extends AppCompatActivity implements  HorizontalScaleScrollView.OnScrollListener, MultHorizontalScaleScrollView.OnScrollListener {

    private TextView tv_scale;
    private TextView tv_scale2;
    private TextView tv_scale3;
    private TextView tv_scale4;
    private HorizontalScaleScrollView hsScale;
    private HorizontalScaleScrollView hsScale2;
    private MultHorizontalScaleScrollView mulScale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_scale = (TextView) findViewById(R.id.tv_scale);
        tv_scale2 = (TextView) findViewById(R.id.tv_scale2);
        tv_scale3 = (TextView) findViewById(R.id.tv_scale3);
        tv_scale4 = (TextView) findViewById(R.id.tv_scale4);
        hsScale = (HorizontalScaleScrollView) findViewById(R.id.hsScale);
        hsScale2 = (HorizontalScaleScrollView) findViewById(R.id.hsScale2);
        mulScale = (MultHorizontalScaleScrollView) findViewById(R.id.mul_scale);

        //整型数据测试
        hsScale.setScrollListener(this);
        //hsScale.setMinAndMaxScale(10,500);

        //浮点型测试
        hsScale2.setScrollListener(this);
        //hsScale2.setIsIntegarl(false);
       // hsScale2.setMinAndMaxScale(1.1f,33.3f);

        //上下组合测试
        mulScale.setScrollListener(this);
        mulScale.setTopMinAndMaxScale(30,299);
        mulScale.setBottomMinAndMaxScale(30,299);
    }

    @Override
    public void onScaleScroll(float scale) {
        tv_scale2.setText("进度:" + scale);
    }

    @Override
    public void onScaleScroll(int scale) {
        tv_scale.setText("进度:" + scale);
    }

    @Override
    public void onScaleScrollBottom(int scale) {
        tv_scale4.setText("下面进度:" + scale);
    }

    @Override
    public void onScaleScrollTop(int scale) {
        tv_scale3.setText("上面进度:" + scale);
    }
}
