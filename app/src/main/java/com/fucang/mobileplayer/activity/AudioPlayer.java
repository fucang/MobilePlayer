package com.fucang.mobileplayer.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.widget.ImageView;

import com.fucang.mobileplayer.IMusicPlayerService;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.service.MusicPlayerService;
import com.fucang.mobileplayer.utils.Logger;

/**
 * Created by 浮滄 on 2017/5/9.
 */
public class AudioPlayer extends Activity {

    private ImageView ivIcon;

    private int position;

    private IMusicPlayerService service;

    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当链接成功时回调
         * @param componentName
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    service.openAudio(position);
                } catch (RemoteException e) {
                    Logger.error("服务播放歌曲错误："  + e.getMessage());
                }
            }
        }

        /**
         * 当断开链接的时候回调
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (service != null) {
                try {
                    service.stop();
                    service = null;
                } catch (RemoteException e) {
                    Logger.error("断开服务错误："  + e.getMessage());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getData();
        // 绑定并且启动服务
        bindAndStartService();
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.fucang.mobileplayer_OPEN_AUDIO");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent); // 保证服务只启动一次
    }

    /**
     * 得到数据
     */
    private void getData() {
        position = getIntent().getIntExtra("position", 0);
    }

    private void initView() {
        setContentView(R.layout.activity_audio_player);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_list);

        AnimationDrawable rocketAnimation = (AnimationDrawable) ivIcon.getBackground();
        rocketAnimation.start();
    }

}
