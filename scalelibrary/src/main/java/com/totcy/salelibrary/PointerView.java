package com.totcy.salelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.totcy.salelibrary.util.DensityUtil;
import com.totcy.salelibrary.util.DrawUtils;


/**
 * Description 尺子的指针
 * Author: tu
 * Date: 2016-08-29
 * Time: 16:05
 */
public class PointerView extends View {

    private int scaleMaxHeight = 60;//大刻度高度
    private int radius = (int) DensityUtil.dp2px(getContext(),3);;//原点的半径
    private Paint mPaint = new Paint();
    private boolean isShowUp = false;//指针方向 默认向下

    public boolean isShowUp() {
        return isShowUp;
    }

    public void setShowUp(boolean showUp) {
        isShowUp = showUp;
        invalidate();
    }

    public int getScaleMaxHeight() {
        return scaleMaxHeight;
    }

    public void setScaleMaxHeight(int scaleMaxHeight) {
        this.scaleMaxHeight = scaleMaxHeight;
        invalidate();
    }

    public PointerView(Context context) {
        this(context, null);
    }

    public PointerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        //初始化坐标画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//
        mPaint.setColor(Color.WHITE);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        mPaint.setStrokeWidth(2f);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height;
        int width;
        width = (int) (2.5* DrawUtils.getCoordinateX(30,scaleMaxHeight + radius));
        height = (int) (DrawUtils.getCoordinateY(30,scaleMaxHeight+ radius) + 2 * scaleMaxHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndex(canvas);
    }

    /**
     * 绘制指针
     * @param canvas
     */
    private void drawIndex(Canvas canvas){
        int viewHeight = getHeight();
        if(isShowUp) {
            //绘制左边斜线
            canvas.drawLine(getWidth() / 2, viewHeight -DrawUtils.getCoordinateY(30, scaleMaxHeight + radius)
                    , getWidth() / 2 - DrawUtils.getCoordinateX(30, scaleMaxHeight + radius) * 3 / 2
                    , viewHeight - 0, mPaint);
            //绘制右边斜线
            canvas.drawLine(getWidth() / 2, viewHeight - DrawUtils.getCoordinateY(30, scaleMaxHeight + radius)
                    , getWidth() / 2 + DrawUtils.getCoordinateX(30, scaleMaxHeight + radius) * 3 / 2
                    , viewHeight - 0, mPaint);
            //绘制第一个原点
            canvas.drawCircle(getWidth() / 2, viewHeight - DrawUtils.getCoordinateY(30, scaleMaxHeight + radius), radius, mPaint);
            //绘制竖线
            canvas.drawLine(getWidth() / 2, viewHeight - DrawUtils.getCoordinateY(30, scaleMaxHeight + radius)
                    , getWidth() / 2
                    , viewHeight - (DrawUtils.getCoordinateY(30, scaleMaxHeight + radius) + 2 * scaleMaxHeight), mPaint);
            //绘制第二个原点
            canvas.drawCircle(getWidth() / 2,
                    viewHeight - (DrawUtils.getCoordinateY(30, scaleMaxHeight + radius) + 2 * scaleMaxHeight - radius)
                    , radius, mPaint);
        }else{
            //绘制左边斜线
            canvas.drawLine(getWidth()/2, DrawUtils.getCoordinateY(30,scaleMaxHeight+ radius)
                    ,getWidth()/2 - DrawUtils.getCoordinateX(30,scaleMaxHeight + radius)*3/2
                    ,0, mPaint);
            //绘制右边斜线
            canvas.drawLine(getWidth()/2, DrawUtils.getCoordinateY(30,scaleMaxHeight+ radius)
                    ,getWidth()/2 + DrawUtils.getCoordinateX(30,scaleMaxHeight + radius)*3/2
                    ,0, mPaint);
            //绘制第一个原点
            canvas.drawCircle(getWidth()/2,DrawUtils.getCoordinateY(30,scaleMaxHeight + radius),radius,mPaint);
            //绘制竖线
            canvas.drawLine(getWidth()/2, DrawUtils.getCoordinateY(30,scaleMaxHeight + radius)
                    ,getWidth()/2
                    ,DrawUtils.getCoordinateY(30,scaleMaxHeight + radius) + 2*scaleMaxHeight, mPaint);
            //绘制第二个原点
            canvas.drawCircle(getWidth()/2,
                    DrawUtils.getCoordinateY(30,scaleMaxHeight + radius) + 2 * scaleMaxHeight - radius
                    ,radius,mPaint);
        }

    }
}
