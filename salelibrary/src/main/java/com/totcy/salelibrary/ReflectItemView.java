package com.totcy.salelibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.totcy.salelibrary.util.DrawUtils;


/**
 * 圆角控件.
 *
 * @author hailongqiu 356752238@qq.com
 */
public class ReflectItemView extends FrameLayout {

    private static final String TAG = "ReflectItemView";

    private static final int DEFUALT_RADIUS = 30;
    private Paint mShapePaint = null;
    private boolean mIsDrawShape = true;
    private float mRadius = DEFUALT_RADIUS;

    public ReflectItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ReflectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ReflectItemView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        //setClipChildren(false);
        //setClipToPadding(false);
        //viewgroup如果什么都没有的话  不会调用 ondraw方法   if (!dirtyOpaque) onDraw(canvas);
        setWillNotDraw(false);
        // 初始化属性.
/*        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.reflectItemView);// 获取配置属性
            mIsReflection = tArray.getBoolean(R.styleable.reflectItemView_isReflect, false);
            mRefHeight = (int) tArray.getDimension(R.styleable.reflectItemView_reflect_height, DEFUALT_REFHEIGHT);
            mIsDrawShape = tArray.getBoolean(R.styleable.reflectItemView_isShape, false);
            mRadius = tArray.getDimension(R.styleable.reflectItemView_radius, DEFUALT_RADIUS); // 圆角半径.
            mRefleSpacing = (int) tArray.getDimension(R.styleable.reflectItemView_refle_spacing, 0);
            setRadius(mRadius);
        }*/

        // 初始化圆角矩形.
        initShapePaint();
    }

    private void initShapePaint() {
        mShapePaint = new Paint();
        mShapePaint.setAntiAlias(true);
        // 取两层绘制交集。显示下层。
        mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    /**
     * 设置绘制奇形怪状的形状.
     */
    public void setDrawShape(boolean isDrawShape) {
        mIsDrawShape = isDrawShape;
        invalidate();
    }

    public boolean isDrawShape() {
        return this.mIsDrawShape;
    }


    public Path getShapePath(int width, int height, float radius) {
        return DrawUtils.addRoundPath3(width, height, radius);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (canvas != null) {

                if (mIsDrawShape && (mRadius > 0)) {
                    drawShapePathCanvas(canvas);
                } else {
                    super.draw(canvas);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制圆角控件. 修复使用clipPath有锯齿问题.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawShapePathCanvas(Canvas shapeCanvas) {
        if (shapeCanvas != null) {
            int width = getWidth();
            int height = getHeight();
            if (width == 0 || height == 0)
                return;
            int count = shapeCanvas.save();
            int count2 = shapeCanvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            //
            Path path = getShapePath(width, height, mRadius);
            super.draw(shapeCanvas);
            shapeCanvas.drawPath(path, mShapePaint);
            //mShapePaint.setStyle(Paint.Style.STROKE);
            //shapeCanvas.drawRoundRect(0, 0, width, height,mRadius,mRadius,mShapePaint);
            //
            if (count2 > 0) {
                shapeCanvas.restoreToCount(count2);
            }
            shapeCanvas.restoreToCount(count);
        }
    }
    /*
     * 设置/获取-圆角的角度.
	 */

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

}