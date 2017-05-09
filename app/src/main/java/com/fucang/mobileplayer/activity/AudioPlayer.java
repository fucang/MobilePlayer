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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fucang.mobileplayer.IMusicPlayerService;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.service.MusicPlayerService;
import com.fucang.mobileplayer.utils.Logger;

/**
 * Created by 浮滄 on 2017/5/9.
 */
public class AudioPlayer extends Activity implements View.OnClickListener {

    private int position;

    private IMusicPlayerService service;

    private ImageView ivIcon;
    private TextView ivArtist;
    private TextView ivName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-09 18:37:42 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

        setContentView(R.layout.activity_audio_player);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable rocketAnimation = (AnimationDrawable) ivIcon.getBackground();
        rocketAnimation.start();

        ivArtist = (TextView)findViewById( R.id.iv_artist );
        ivName = (TextView)findViewById( R.id.iv_name );
        tvTime = (TextView)findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = (Button)findViewById( R.id.btn_audio_playmode );
        btnAudioPre = (Button)findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = (Button)findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = (Button)findViewById( R.id.btn_audio_next );
        btnLyrc = (Button)findViewById( R.id.btn_lyrc );

        btnAudioPlaymode.setOnClickListener( this );
        btnAudioPre.setOnClickListener( this );
        btnAudioStartPause.setOnClickListener( this );
        btnAudioNext.setOnClickListener( this );
        btnLyrc.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-09 18:37:42 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnAudioPlaymode ) {
            // Handle clicks for btnAudioPlaymode
        } else if ( v == btnAudioPre ) {
            // Handle clicks for btnAudioPre
        } else if ( v == btnAudioStartPause ) {
            // Handle clicks for btnAudioStartPause
            if (service != null) {
                try {
                    if (service.isPlaying()) {
                        // 视频暂停
                        service.pause();
                        // 按钮播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    } else {
                        // 视频播放
                        service.start();
                        // 按钮暂停
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    Logger.error("获取音乐的播放状态错误：" + e.getMessage());
                }
            }
        } else if ( v == btnAudioNext ) {
            // Handle clicks for btnAudioNext
        } else if ( v == btnLyrc ) {
            // Handle clicks for btnLyrc
        }
    }

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
        findViews();
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
}
