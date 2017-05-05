package com.fucang.mobileplayer.activity;

/**
 * Created by 浮滄 on 2017/5/4.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 系统播放器
 */
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    // 视频进度的更新
    private static final int PROGRESS = 1;

    private VideoView videoview;

    private Uri uri; // 播放地址

    private Utils utils;

    /**
     * 监听电量的变化
     */
    private MyReceiver receiver;

    /**
     * 传入进来的播放列表
     */
    private ArrayList<MediaItem> mediaItems;

    /**
     * 要播放的视频在列表中的具体位置
     */
    private int position;

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
            finish();
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreViedo();
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
            playNextVideo();
        } else if ( v == btnVideoSiwchScreen ) {
            // Handle clicks for btnVideoSiwchScreen
        }
    }

    /**
     * 播放上一个视频
     */
    private void playPreViedo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            // 播放下一个
            if (position - 1 >= 0) {
                MediaItem mediaItem = mediaItems.get(--position);
                tvName.setText(mediaItem.getName());
                videoview.setVideoPath(mediaItem.getData());

                // 设置按钮状态
                setButtonState();
            } else if (uri != null) {
                // 设置上一个与下一个按钮状态为灰色并且不可以点击
                setButtonState();
            }
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            // 播放下一个
            if (position + 1 < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(++position);
                tvName.setText(mediaItem.getName());
                videoview.setVideoPath(mediaItem.getData());

                // 设置按钮状态
                setButtonState();
            } else if (uri != null) {
                // 设置上一个与下一个按钮状态为灰色并且不可以点击
                setButtonState();
            }
        }
    }

    /**
     * 设置按钮状态
     */
    private void setButtonState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (position == 0) {
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray); // 设置为灰色
                btnVideoPre.setEnabled(false); // 设置不可点击
            }
            if (position == mediaItems.size() - 1) {
                // 设置next按钮为灰
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            }
        } else if (uri != null) {
            // 两个按钮均为灰色且不可点击
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray); // 设置为灰色
            btnVideoPre.setEnabled(false); // 设置不可点击
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
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

                    // 2、更新
                    // 2.1设置SeekBar.setProgress（当前进度）
                    seekbarVideo.setProgress(currentPosition);

                    // 2.1更新文本播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    // 2.3设置系统时间
                    tvSystemTime.setText(getSystemTime());

                    // 3、每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    /**
     * 得到系统时间
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        findViews();

        // 设置监听
        setListener();

        // 得到播放地址
        getData();

        // 设置播放的uri
        setData();

        // 设置控制面板,即控制视频的播放
//        videoview.setMediaController(new MediaController(this)); // 系统的控制面板

    }

    /**
     * 设置播放的地址
     */
    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName()); // 设置视频的名称
            videoview.setVideoPath(mediaItem.getData()); // 设置播放视频的地址
        } else if (uri != null) {
            tvName.setText(uri.toString());
            videoview.setVideoURI(uri);
        } else {
            Toast.makeText(SystemVideoPlayer.this, "没有要播放的数据...", Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

    /**
     * 得到视频列表数据
     */
    private void getData() {
        // 得到播放地址
        uri = getIntent().getData();

        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initData() {
        utils = new Utils();
        // 注册电量广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // 当电量变化的时候发送广播
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 设置电量图片
     * @param battery
     */
    private void setBattery(int battery) {
        if (battery <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (battery <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if(battery <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (battery <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if(battery <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if(battery <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if(battery <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0); // 0 ~ 100
            setBattery(level);
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        // 准备好的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        // 播放出错监听
        videoview.setOnErrorListener(new MyOnErrorListener());

        // 播放完成的监听
        videoview.setOnCompletionListener(new MyOnCompletionListener());

        // 设置SeekBar状态变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }

    /**
     *
     */
    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * 当手指滑动的时候，会引起SeekBar进度变化会回调
         * @param seekBar
         * @param progress
         * @param fromUser 如果是用户引起true，不是用户引起false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoview.seekTo(progress);
            }
        }

        /**
         * 当手指触碰时回调
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * 当手指离开的时候回调
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
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
            playNextVideo();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 要写在super的前面，释放资源的时候，先释放子类，再释放父类
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}
