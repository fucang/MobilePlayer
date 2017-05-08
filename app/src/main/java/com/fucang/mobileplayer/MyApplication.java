package com.fucang.mobileplayer;

import android.app.Application;

import org.xutils.*;
import org.xutils.BuildConfig;

/**
 * Created by 浮滄 on 2017/5/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        // 是否输出debug日志
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
