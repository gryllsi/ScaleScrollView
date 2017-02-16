package com.totcy.scalescrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.totcy.scalescrollview.util.DensityUtil;
import com.totcy.scalescrollview.util.DrawUtils;

/**
 * Description 水平滑动标尺 上下标尺
 * Author: tu
 * Date: 2016-08-30
 * Time: 18:38
 */
public class MultHorizontalScaleScrollView extends BaseScaleScrollView {

    private ScaleView scaleViewTop, scaleViewBottom;

    public MultHorizontalScaleScrollView(Context context) {
        this(context, null);
    }

    public MultHorizontalScaleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultHorizontalScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            width = Math.max(width, childView.getMeasuredWidth());
            height = Math.max(height, childView.getMeasuredHeight());
        }
        //高度需要加上指针的高度 （height + 一个最大刻度高度 + 斜边的高度） 两个指针
        height = (int) (height + 2 * (scaleMaxHeight + DrawUtils.getCoordinateY(30, scaleMaxHeight)));

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //这里的话第一个childview是尺子 因此只需要设置宽度全屏 高度自适应(尺子)
        int count = getChildCount();
        int marginTop = (int) (scaleMaxHeight + DrawUtils.getCoordinateY(30, scaleMaxHeight));
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (i == 0) { //
                childView.layout(0, marginTop, childView.getMeasuredWidth(), childView.getMeasuredHeight() + marginTop);
            } else if (i == 1) {//指针1
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(getWidth() / 2 - childWidth / 2, cardViewPandding, getWidth() / 2 + childWidth / 2, childHeight + cardViewPandding);
            } else if (i == 2) {
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(getWidth() / 2 - childWidth / 2, getHeight() - childHeight - cardViewPandding, getWidth() / 2 + childWidth / 2, getHeight() - cardViewPandding);
            }
        }
    }

    /**
     * 设置最大和最小值
     *
     * @param min
     * @param max
     */
    public void setTopMinAndMaxScale(float min, float max) {
        scaleViewTop.setMinAndMaxScale(min, max);
    }

    /**
     * 设置最大和最小值
     *
     * @param min
     * @param max
     */
    public void setBottomMinAndMaxScale(float min, float max) {
        scaleViewBottom.setMinAndMaxScale(min, max);
    }

    private void init() {

        scaleViewTop = new ScaleView(getContext());
        LinearLayout.LayoutParams lScale = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scaleViewTop.setLayoutParams(lScale);
        //初始化属性
        scaleViewTop.setScaleWidth(scaleWidth);
        scaleViewTop.setScaleMaxHeight(scaleMaxHeight);
        scaleViewTop.setTextSize(scaleTextSize);
        scaleViewTop.setTextColor(scaleColor);
        scaleViewTop.setIntegar(true);
        scaleViewTop.setShowUp(true);
        scaleViewTop.setScrollListener(new ScaleView.OnScrollListener() {
            @Override
            public void onScaleScroll(float scale) {
                if (mScrollListener != null) {
                    mScrollListener.onScaleScrollTop((int) scale);
                }
            }
        });

        scaleViewBottom = new ScaleView(getContext());
        scaleViewBottom.setLayoutParams(lScale);
        //初始化属性
        scaleViewBottom.setScaleWidth(scaleWidth);
        scaleViewBottom.setScaleMaxHeight(scaleMaxHeight);
        scaleViewBottom.setTextSize(scaleTextSize);
        scaleViewBottom.setTextColor(scaleColor);
        scaleViewBottom.setIntegar(true);
        scaleViewBottom.setShowUp(false);
        scaleViewBottom.setScrollListener(new ScaleView.OnScrollListener() {
            @Override
            public void onScaleScroll(float scale) {
                if (mScrollListener != null) {
                    mScrollListener.onScaleScrollBottom((int) scale);
                }
            }
        });

        //尺子父布局
        LinearLayout llScale = new LinearLayout(getContext());
        LinearLayout.LayoutParams lpllScale = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llScale.setLayoutParams(lpllScale);
        llScale.setOrientation(LinearLayout.VERTICAL);
        llScale.addView(scaleViewTop);
        //分割线
        LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cardViewPandding);
        View vLine = new View(getContext());
        vLine.setLayoutParams(lpLine);
        vLine.setBackgroundColor(Color.WHITE);
        llScale.addView(vLine);
        llScale.addView(scaleViewBottom);
        //圆角控件
        FrameLayout.LayoutParams lpReflect = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ReflectItemView reflectItemView = new ReflectItemView(getContext());
        reflectItemView.setLayoutParams(lpReflect);
        reflectItemView.setBackgroundColor(Color.RED);
        reflectItemView.setRadius(DensityUtil.dp2px(getContext(),10));
        reflectItemView.addView(llScale);
        //边框
        FrameLayout.LayoutParams lpCardView = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ReflectItemView cv = new ReflectItemView(getContext());
        cv.setLayoutParams(lpCardView);
        cv.addView(reflectItemView);
        cv.setBackgroundColor(Color.WHITE);
        cv.setRadius(DensityUtil.dp2px(getContext(),10));
        cv.setPadding(cardViewPandding, cardViewPandding, cardViewPandding, cardViewPandding);

        addView(cv);

        //指针向下的
        LinearLayout.LayoutParams lpPointer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        PointerView pointerView = new PointerView(getContext());
        pointerView.setLayoutParams(lpPointer);
        pointerView.setScaleMaxHeight(scaleMaxHeight);
        addView(pointerView);
        //指针向上的
        PointerView pointerViewUp = new PointerView(getContext());
        pointerViewUp.setLayoutParams(lpPointer);
        pointerViewUp.setShowUp(true);
        pointerViewUp.setScaleMaxHeight(scaleMaxHeight);
        addView(pointerViewUp);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Path mPath = new Path();
//        float rxY = getResources().getDimension(R.dimen.dim_10);
//        mPath.addRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),rxY,rxY,Path.Direction.CCW);//Path.Direction.CCW 顺时针方向
//        canvas.clipPath(mPath);

        super.draw(canvas);

    }

    private OnScrollListener mScrollListener;

    public interface OnScrollListener {
        void onScaleScrollBottom(int scale);

        void onScaleScrollTop(int scale);
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

}
