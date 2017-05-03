package com.fucang.mobileplayer.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.fucang.mobileplayer.R;

/**
 * Created by 浮滄 on 2017/5/3.
 */
public class MainActivity extends Activity {

    // 实例化
    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        rg_bottom_tag.check(R.id.rb_video);
    }
}
