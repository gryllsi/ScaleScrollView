package com.totcy.salelibrary.util;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * @version V1.0
 * Description: 圆角path
 * create by: tu
 * date 2016/08/25 23:38
 */
public class DrawUtils {
    public static Path addRoundPath3(int width, int height, float radius) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
        return path;
    }

    /**
     * 获取圆周上y值相对值
     * @param tempAngle
     * @param radius 算开始坐标是传半径，算结束坐标时传刻度线的长度
     * @return
     */
    public static float getCoordinateY(int tempAngle,float radius){

        //利用正弦函数算出y坐标
        return (float) (Math.sin(tempAngle*Math.PI/180)*(radius - 15)); //10 是离圆弧的距离
    }
    /**
     * 获取圆周上X值相对值
     * @param tempAngle
     * @return
     */
    public static float getCoordinateX(int tempAngle,float radius){

        //利用余弦函数算出X坐标
        return (float) (Math.cos(tempAngle*Math.PI/180)*(radius - 15));
    }
}
