package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/10.
 */

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 根据不同的手机调整歌词的大小
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从dip单位转换为px（像素）
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 从px到dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
