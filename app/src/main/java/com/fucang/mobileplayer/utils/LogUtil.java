package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/4.
 */

import android.support.v4.BuildConfig;
import android.util.Log;

/**
 * 打印日志
 */
public class LogUtil {
    public static void info(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.i(tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.d(tag, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.e(tag, msg);
        }
    }


    public static void warning(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.w(tag, msg);
        }
    }
}
