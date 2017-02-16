package com.totcy.scalescrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.totcy.scalescrollview.util.DensityUtil;


/**
 * Description 滑动尺子基类 一些属性获取
 * Author: tu
 * Date: 2016-08-31
 * Time: 11:01
 */
public abstract class BaseScaleScrollView  extends ViewGroup{
    protected int scaleColor = Color.WHITE;
    protected float scaleTextSize = 20;
    protected int scaleWidth = (int) DensityUtil.dp2px(getContext(),6);//刻度间隔
    protected int scaleMaxHeight = (int) DensityUtil.dp2px(getContext(),16);//刻度高度（最长的）
    protected float minScale = 0;//最小刻度
    protected float maxScale = 100;//最大刻度
    protected int cardViewPandding = 1;
    protected boolean scaleShowUp = true;//尺子默认向上
    protected boolean isIntegar = true;//默认整型数据

    public BaseScaleScrollView(Context context) {
        this(context, null);
    }

    public BaseScaleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        cardViewPandding = DensityUtil.dp2px(getContext(),1);
        /**
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ScaleScrollView, defStyleAttr,0);
        int n = a.getIndexCount();

        for (int i =   0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ScaleScrollView_ScaleValueColor:
                    scaleColor =  a.getColor(attr,scaleColor);
                    break;
                case R.styleable.ScaleScrollView_ScaleTextSize:
                    scaleTextSize =  a.getDimension(attr,scaleTextSize);
                    break;
                case R.styleable.ScaleScrollView_ScaleWidth:
                    scaleWidth = (int) a.getDimension(attr,scaleWidth);
                    break;
                case R.styleable.ScaleScrollView_ScaleMaxHeight:
                    scaleMaxHeight = (int) a.getDimension(attr,scaleMaxHeight);
                    break;
                case R.styleable.ScaleScrollView_MinScale:
                    minScale = a.getFloat(attr,minScale);
                    break;
                case R.styleable.ScaleScrollView_MaxScale:
                    maxScale = a.getFloat(attr,maxScale);
                    break;
                case R.styleable.ScaleScrollView_ScaleShowUp:
                    scaleShowUp = a.getBoolean(attr,scaleShowUp);
                    break;
                case R.styleable.ScaleScrollView_isIntegar:
                    isIntegar = a.getBoolean(attr,isIntegar);
                    break;
                default:
                    break;
            }
        }

        a.recycle();
    }
}
