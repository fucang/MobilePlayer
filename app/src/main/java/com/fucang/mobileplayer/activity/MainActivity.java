package com.fucang.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.base.ReplaceFragment;
import com.fucang.mobileplayer.page.AudioPager;
import com.fucang.mobileplayer.page.GroomPager;
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
        basePagers.add(new GroomPager(this)); // 添加网络音频页面 3

        // 设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rg_bottom_tag.check(R.id.rb_video);
    }

    /**
     * 根据不同的位置得到不同的页面
     *
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.isInitData()) {
            basePager.initData(); //  初始化数据
        }
        return basePager;
    }

    /**
     * 解决Android6.0以上系统不能动态读取sdcard权限的问题即获取权限
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb_audio: // 本地音频
                    isGrantExternalRW(MainActivity.this);
                    position = 1;
                    break;
                case R.id.rb_netvideo: // 网络视频
                    position = 2;
                    break;
                case R.id.rb_netaudio: // 网络音频
                    position = 3;
                    break;
                default: // 默认为本地视频
                    // 哪里需要读写内存卡的权限，就调用isGrantExternalRW方法
                    // 就像这样，调到下一个页面，下一个页面需要读取内存卡的权限
                    isGrantExternalRW(MainActivity.this);
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
        fragmentTransaction.replace(R.id.fl_main_content, new ReplaceFragment(getBasePager()));

        // 4、提交事务
        fragmentTransaction.commit();

    }

    // 用于实现两次退出,表示是否已经退出
    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出浮滄影音", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}