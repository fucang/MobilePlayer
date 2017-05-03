package com.fucang.mobileplayer.base;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.Context;
import android.view.View;

/**
 * 基类
 * VideoPager
 * AudioPager
 * NetVideoPager
 * NetAudioPager
 * 继承BasePager
 */
public abstract class BasePager {
    /**
     * 上下文
     */
    public Context context;

    private View view;

    public BasePager(Context context) {
        this.context = context;
        this.view = initView();
    }

    /**
     * 子类实现，实现特定的页面
     * @return
     */
    public abstract View initView();

    /**
     * 当子页面需要初始化数据时，重写该方法
     */
    public void initData() {
    }
}
