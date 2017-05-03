package com.fucang.mobileplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.page.AudioPager;
import com.fucang.mobileplayer.page.NetAudioPager;
import com.fucang.mobileplayer.page.NetVideoPager;
import com.fucang.mobileplayer.page.VideoPager;

import java.util.ArrayList;

/**
 * Created by 浮滄 on 2017/5/3.
 */
public class MainActivity extends FragmentActivity {

    // 实例化
    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;

    // 页面的集合
    private ArrayList<BasePager> basePagers;

    // 页面的位置，即处于哪个页面
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this)); // 添加本地视频页面 0
        basePagers.add(new AudioPager(this)); // 添加本地音频页面 1
        basePagers.add(new NetVideoPager(this)); // 添加网络视频页面 2
        basePagers.add(new NetAudioPager(this)); // 添加网络音频页面 3

        // 设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rg_bottom_tag.check(R.id.rb_video);
    }

    public BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null) {
            basePager.initData(); //  初始化数据
        }
        return basePager;
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb_audio: // 本地音频
                    position = 1;
                    break;
                case R.id.rb_netvideo: // 网络视频
                    position = 2;
                    break;
                case R.id.rb_netaudio: // 网络音频
                    position = 3;
                    break;
                default: // 默认为本地视频
                    position = 0;
                    break;
            }

            setFragment();
        }
    }

    /**
     * 将页面添加到ragment中
     */
    private void setFragment() {
        // 1、得到ragmentManger
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        // 2、开启事务
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        // 3、替换
        Fragment fragment = new Fragment() {
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                BasePager basePager = getBasePager();
                if (basePager != null) {
                    // 各个页面的视图
                    return basePager.rootview;
                }
                return null;
            }
        };

       fragmentTransaction.replace(R.id.fl_main_content, fragment);

        // 4、提交事务
        fragmentTransaction.commit();

    }
}
