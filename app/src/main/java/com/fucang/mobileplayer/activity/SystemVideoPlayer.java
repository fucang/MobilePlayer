package com.fucang.mobileplayer.activity;

/**
 * Created by 浮滄 on 2017/5/4.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.utils.Utils;

/**
 * 系统播放器
 */
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    // 视频进度的更新
    private static final int PROGRESS = 1;

    private VideoView videoview;

    private Uri uri; // 播放地址

    private Utils utils;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSiwchScreen;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-05 15:19:10 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);

        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSiwchScreen = (Button)findViewById( R.id.btn_video_siwch_screen );

        videoview = (VideoView) findViewById(R.id.videoview);

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSiwchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-05 15:19:10 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            if (videoview.isPlaying()) { // 视频是否在播放
                // 视频在播放，
                // 状态设置为暂停，
                videoview.pause();
                // 按钮状态设置为播放
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            } else {
                // 视频在暂停，
                // 状态设置为播放，
                videoview.start();
                // 按钮状态设置为暂停
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }

        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
        } else if ( v == btnVideoSiwchScreen ) {
            // Handle clicks for btnVideoSiwchScreen
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    // 1、得到当前视频的播放进度
                    int currentPosition = videoview.getCurrentPosition();

                    // 2、设置SeekBar.setProgress（当前进度）
                    seekbarVideo.setProgress(currentPosition);

                    // 更新文本播放时长
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    // 3、每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils();

        findViews();

        // 准备好的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        // 播放出错监听
        videoview.setOnErrorListener(new MyOnErrorListener());

        // 播放完成的监听
        videoview.setOnCompletionListener(new MyOnCompletionListener());

        // 得到播放地址
        uri = getIntent().getData();
        if (uri != null) {
            videoview.setVideoURI(uri);
        }

        // 设置控制面板,即控制视频的播放
//        videoview.setMediaController(new MediaController(this)); // 系统的控制面板

    }

    /**
     * 准备好播放的监听
     */
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoview.start(); // 开始播放
            // 1、得到视频的总时长，关联总长度
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);
            // 设置文本总时长
            tvDuration.setText(utils.stringForTime(duration));
            // 2、发送消息
            handler.sendEmptyMessage(PROGRESS);
        }
    }

    /**
     * 播放完成的监听
     */
    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Toast.makeText(SystemVideoPlayer.this, "播放出错了...", Toast.LENGTH_SHORT).show();
            return false; // 会弹出对话框
        }
    }

    /**
     * 播放完成监听
     */
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Toast.makeText(SystemVideoPlayer.this, "播放完成了..." + uri, Toast.LENGTH_SHORT).show();
        }
    }
}
