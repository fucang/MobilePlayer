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

/**
 * 本地视频页面
 */
public class VideoPager extends BasePager {

    private TextView textView;

    public VideoPager(Context context) {
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
        super.initData();
        textView.setText("本地视频页面");
    }
}
