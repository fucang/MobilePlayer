package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/9.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 缓存数据
 */
public class CacheUtils {

    /**
     * 保存数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("fucang", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 得到缓存数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("fucang", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
