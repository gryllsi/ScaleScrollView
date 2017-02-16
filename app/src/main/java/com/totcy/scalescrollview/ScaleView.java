package com.totcy.scalescrollview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.totcy.scalescrollview.util.DensityUtil;

import java.math.BigDecimal;

/**
 * Description 水平滑动标尺
 * Author: tu
 * Date: 2016-08-25
 * Time: 14:30
 */
public class ScaleView extends View {

    private int startX, startY;//起始点
    private int viewWidth;//view的宽度
    private int viewHigth;//view的高度
    private Paint mPaint;
    private int scaleWidth = 20;//刻度间隔
    private int scaleMinHeight = 30;//小刻度高度
    private int scaleMaxHeight = 60;
    ;//大刻度高度
    private float minScale = 70;//最小刻度
    private float maxScale = 830;//最大刻度

    private int maxRight;//右边可移动最大范围
    private int minLeft;//左边可移动最大范围
    private RectF rectF = new RectF();
    private int textColor = Color.WHITE;
    private int color1 = Color.argb(255, 235, 104, 119);//背景圆颜色 红
    private int color2 = Color.argb(255, 241, 181, 18);//背景圆颜色  黄
    private int color3 = Color.argb(255, 67, 206, 162);//背景圆颜色  绿

    protected Scroller mScroller;
    protected int mScrollLastX;

    protected int mCountScale = 0; //滑动的总刻度
    private VelocityTracker mVelocityTracker;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;

    private boolean isIntegar = true;//是否显示整型数据
    private boolean isShowUp = true;//向上显示


    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化坐标画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//
        mPaint.setColor(textColor);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(20);

        startX = scaleWidth * 3;
        startY = 0;

        scaleMinHeight = scaleMaxHeight / 2;

        mScroller = new Scroller(getContext());
        mScaledMinimumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        int width;

        //宽度测量
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getMeasuredWidth();
        }
        viewWidth = width;
        //高度测量
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getMeasuredHeight();
        }
        height = viewHigth = scaleMaxHeight * 3;//3倍的刻度高度
        setMeasuredDimension(width, height);
        if (isIntegar()) {
            maxRight = (int) ((maxScale - minScale) * scaleWidth + startX - viewWidth / 2);//2 * startX 左右间隔
        } else {
            int max = new BigDecimal(10).multiply(new BigDecimal(maxScale - minScale)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

            maxRight = (max * scaleWidth + startX - viewWidth / 2);
        }
        minLeft = 0 - viewWidth / 2 + startX;//2 * startX 左右间隔

    }

    // 画刻度
    private void onDrawScale(Canvas canvas) {
        float xScale, yScale;
        mPaint.setColor(textColor);
        int max = 0;
        if (isIntegar) {
            max = (int) (maxScale - minScale);
        } else {
            max = new BigDecimal(10).multiply(new BigDecimal(maxScale - minScale)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        }
        for (int i = 0; i <= max; i++) {
            xScale = startX + scaleWidth * i;
            //计算绘制整点数值 10 20 …… 1.0 2.0 ……
            int valueTemp = isIntegar ? (int) (minScale + i) : (int) (minScale * 10) + i;

            if (i == 0 || valueTemp % 10 == 0) {
                yScale = scaleMaxHeight;
                float value = isIntegar ? minScale + i : minScale + (float) i/10;
                if(i==0){
                    //当数据 i==0时，第一个数据不一定是整点的1.0(10) 可能是1.1(2,3,4)这样的，
                    // 这里如果是1.1-1.5则显示，1.6-1.9不显示，会与2.0挨得太进
                    valueTemp = valueTemp%10;
                    if(valueTemp == 0)
                        yScale = scaleMaxHeight;
                    else if(valueTemp == 5){
                        yScale = scaleMinHeight + 5;
                    }else{
                        yScale = scaleMinHeight;
                    }
                    if(valueTemp >= 0 && valueTemp < 6){
                        //这里的文字y值要与其他的水平 因此传 scaleMaxHeight
                        drawScaleValue(canvas, value, xScale, scaleMaxHeight);
                    }
                }else{
                    drawScaleValue(canvas, value, xScale, yScale);
                }
            } else if (valueTemp % 5 == 0) {
                yScale = scaleMinHeight + 5;
            } else {
                yScale = scaleMinHeight;
            }
            //上下刻度线
            if(isShowUp)
                canvas.drawLine(xScale, startY, xScale, yScale, mPaint);
            else
                canvas.drawLine(xScale, viewHigth, xScale, viewHigth - yScale, mPaint);

        }

    }

    /**
     * 绘制阶段背景zsr
     *
     * @param canvas
     */
    private void drawArcBg(Canvas canvas) {
        int xScale;
        //绘制背景
        for (int i = 0; i <= (maxScale - minScale); i++) {

            xScale = startX + scaleWidth * (i + 1);
            //绘制背景
            rectF.top = 0;
            rectF.bottom = viewHigth;
            if (i == 10) {
                rectF.left = 0;
                rectF.right = xScale;
                mPaint.setColor(color1);
                canvas.drawRect(rectF, mPaint);
            } else if (i == 20) {
                rectF.left = rectF.right;
                rectF.right = xScale;
                mPaint.setColor(color2);
                canvas.drawRect(rectF, mPaint);
            } else if (i == 40) {
                rectF.left = rectF.right;
                rectF.right = xScale;
                mPaint.setColor(color3);
                canvas.drawRect(rectF, mPaint);
            }
        }

    }

    /**
     * 绘制刻度值
     *
     * @param x
     * @param y
     */
    private void drawScaleValue(Canvas canvas, float value, float x, float y) {
        float textHeight = mPaint.measureText("0");
        String drawText = "";
        if (isIntegar) {
            drawText = (int) value + "";
        } else {
            drawText = value + "";
        }
        float valueY = 0;
        //向上的刻度
        if(isShowUp){
            valueY =  y + scaleMinHeight + textHeight;
        }else{//向下的刻度
            valueY = viewHigth - y - scaleMinHeight;
        }
        canvas.drawText(drawText, x,valueY, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(color1);
        drawArcBg(canvas);
        onDrawScale(canvas);
    }

    /**
     * 使用Scroller时需重写
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 判断Scroller是否执行完毕
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 通过重绘来不断调用computeScroll
            postInvalidate();
            //需要减去一开始的偏移距离
            int finalX = mScroller.getCurrX() - startX + viewWidth / 2;
            //滑动的刻度 (除以刻度间隔 算出滑动了多少格)
            int tmpCountScale = (int) Math.rint((double) finalX / (double) scaleWidth); //四舍五入取整
            //边界判断
            if(tmpCountScale < 0){
                tmpCountScale = 0;
            }
            if (mScrollListener != null) {
                mScrollListener.onScaleScroll(isIntegar ? tmpCountScale + minScale :
                        new BigDecimal((float)(tmpCountScale) / 10 + minScale).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
    }

    private int scrollLastX = 0;

    public void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);

        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mScrollLastX = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                mCountScale = getScrollX();
                int dataX = mScrollLastX - x;
                scrollBy(dataX, 0);
                //smoothScrollBy(dataX,0);
                mScrollLastX = x;
                invalidate();

                if (mScrollListener != null) {
                    int finalX = getScrollX() - startX + viewWidth / 2;
                    //滑动的刻度
                    int tmpCountScale = (int) Math.rint((double) finalX / (double) scaleWidth); //四舍五入取整
                    tmpCountScale = tmpCountScale < 0 ? 0:tmpCountScale;
                    //边界数据处理
                    if(tmpCountScale < 0){
                        tmpCountScale = 0;
                    }
                    mScrollListener.onScaleScroll(isIntegar ? tmpCountScale + minScale :
                            new BigDecimal((float)(tmpCountScale)/ 10 + minScale).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue());
                }

                return true;
            case MotionEvent.ACTION_UP:

                //使用mVelocityTracker来计算速度，
                //计算当前速度， 1代表px/毫秒, 1000代表px/秒,
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                mVelocityTracker.clear();

                if (Math.abs(xVelocity) > mScaledMinimumFlingVelocity) {
                    mScroller.fling(getScrollX(), 0, -xVelocity, 0, minLeft, maxRight, 0, 0);//根据坐标轴正方向问题，这里需要加上-号
                } else {
                    if (mCountScale < minLeft) {
                        mCountScale = minLeft;
                    } else if (mCountScale > maxRight) {
                        mCountScale = maxRight;
                    }
                    mScroller.setFinalX(mCountScale); //纠正指针位置
                }
                postInvalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private OnScrollListener mScrollListener;

    public interface OnScrollListener {
        void onScaleScroll(float scale);
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }
    public boolean isShowUp() {
        return isShowUp;
    }

    public void setShowUp(boolean showUp) {
        isShowUp = showUp;
    }

    public void setIntegar(boolean integar) {
        isIntegar = integar;
        invalidate();
    }

    public boolean isIntegar() {
        return isIntegar;
    }

    public void setMinAndMaxScale(float min, float max) {
        minScale = min;
        maxScale = max;
        invalidate();
    }

    public int getScaleMaxHeight() {
        return scaleMaxHeight;
    }
    public void setTextSize(float size){
        mPaint.setTextSize(size);
        invalidate();
    }
    public void setScaleMaxHeight(int scaleMaxHeight) {
        this.scaleMaxHeight = scaleMaxHeight;
    }

    public int getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }
}
