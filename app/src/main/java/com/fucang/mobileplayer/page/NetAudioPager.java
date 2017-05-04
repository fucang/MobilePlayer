package com.fucang.mobileplayer.page;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.utils.Logger;

/**
 * 网络音乐页面
 */
public class NetAudioPager extends BasePager {

    private TextView textView;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        Logger.info("==========================初始化网络音频");
        super.initData();
        textView.setText("网络音频页面");
    }
}
