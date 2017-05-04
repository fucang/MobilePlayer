package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/4.
 */

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * 打印日志
 */
public class Logger {
    public static final String SAVE_FILE_PATH_DIRECTORY = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/" + "log";
    public static final String TAG = "funshion";
    public static boolean DEBUG = true;
    private static boolean PRINTLOG = false;
    private static boolean IS_DEBUG = false;
    public static final String LOG_PATH = SAVE_FILE_PATH_DIRECTORY + "/log.txt";

    public static void info(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static void info(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static void info(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, msg, tr);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static void v(String msg) {
        if (DEBUG || IS_DEBUG) {
            Log.v(TAG, msg);
        }
//        if (PRINTLOG) {
//            writeFile(msg);
//            FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
//        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG || IS_DEBUG) {
            Log.v(tag, msg);
        }
//        if (PRINTLOG) {
//            writeFile(msg);
//            FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
//        }
    }

    public static void error(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static void error(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static void error(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, msg, tr);
        }
        if (PRINTLOG) {
            writeFile(msg);
        }
    }

    public static File file = new File(LOG_PATH);
    /**
     * 写入文件
     *
     * @param content
     */
    public synchronized static void writeFile(String content) {
//        if (TextUtils.isEmpty(content) || !UIUtils.isSDcardExist()) {
//            return;
//        }
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            RandomAccessFile raf = new RandomAccessFile(file, "rw");
//            long len = raf.length();
//            raf.seek(len);
//            raf.writeBytes(content);
//            raf.close();
//        } catch (Exception e) {
//            Logger.error(TAG, e.toString());
//        }
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean debug) {
        DEBUG = debug;
    }

    public static boolean isPrintlog() {
        return PRINTLOG;
    }

    public static boolean isPRINTLOG() {
        return PRINTLOG;
    }

    public static void setPRINTLOG(boolean pRINTLOG) {
        PRINTLOG = pRINTLOG;
    }


//    public static void Logger(String msg) {
//        if (IS_DEBUG) {
//            Log.e(TAG, msg);
//        }
//        if (PRINTLOG) {
//            FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
//        }
//    }

}
