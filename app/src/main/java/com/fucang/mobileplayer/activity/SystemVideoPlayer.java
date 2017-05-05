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
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Utils;
import com.fucang.mobileplayer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 系统播放器
 */
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    // 视频进度的更新
    private static final int PROGRESS = 1;

    // 隐藏控制面板
    private static final int HIDE_MEDIACONTROLLER = 2;

    // 全屏显示
    private static final int FULL_SCREEN = 1;

    // 默认屏幕大小显示
    private static final int DEFAULT_SCREEN = 2;

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

    /**
     * 定义手势识别器
     */
    private GestureDetector detector;

    /**
     * 控制面板
     */
    private RelativeLayout media_controller;

    /**
     * 控制面板是否显示
     */
    private boolean isShowMediaController = false;

    /**
     * 全屏显示
     */
    private boolean isFullScreen = false;

    /**
     * 屏幕的宽
     */
    private int screenWidth = 0;

    /**
     * 屏幕的高
     */
    private int screenHeight = 0;

    /**
     * 视频真实的宽
     */
    private int mVideoWidth;

    /**
     * 视频真实的高
     */
    private int mVideoHeight;

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
        media_controller = (RelativeLayout) findViewById(R.id.media_controller);

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
            startAndPause();
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSiwchScreen ) {
            // Handle clicks for btnVideoSiwchScreen
            setFullScreenAndDefault();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
    }

    private void startAndPause() {
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
            if ((position == 0) && (position == mediaItems.size() - 1)) {
                setPreButton(false);
                setNextButton(false);
            } else if ((position > 0) && (position == mediaItems.size() - 1)) {
                setPreButton(true);
                setNextButton(false);
            } else if ((position == 0) && (position < mediaItems.size() - 1)) {
                setPreButton(false);
                setNextButton(true);
            } else {
                setPreButton(true);
                setNextButton(true);
            }
        } else if (uri != null) {
            // 两个按钮均为灰色且不可点击
            setPreButton(false);
            setNextButton(false);
        }
    }

    /**
     * 设置播放上一个视频按钮
     * @param isEnabled
     */
    private void setPreButton(boolean isEnabled) {
        if (isEnabled) {
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
        } else {
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray); // 设置为灰色
            btnVideoPre.setEnabled(false); // 设置不可点击
        }
    }

    /**
     * 设置播放下一个视频按钮
     * @param isEnabled
     */
    private void setNextButton(boolean isEnabled) {
        if (isEnabled) {
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
        } else {
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray); // 设置为灰色
            btnVideoNext.setEnabled(false); // 设置不可点击
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_MEDIACONTROLLER:
                    // 隐藏控制面板
                    hideMediaController();
                    break;
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

        // 实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 单击 隐藏——显示控制面板
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShowMediaController) {
                    // 隐藏
                    hideMediaController();
                    // 把隐藏消息移除
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                } else {
                    // 显示
                    showMediaController();
                    // 发送隐藏消息，5秒后自动隐藏
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
                }
                return super.onSingleTapConfirmed(e);
            }

            /**
             * 双击
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            /**
             * 长按
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause(); // 长按暂停,播放
            }
        });

        // 得到屏幕的宽和高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

    } // initData()

    /**
     * 切换屏幕大小
     */
    private void setFullScreenAndDefault() {
        if (isFullScreen) {
            // 默认
            setVideoType(DEFAULT_SCREEN);
        } else {
            // 全屏
            setVideoType(FULL_SCREEN);
        }
    }

    /**
     * 设置屏幕大小
     * @param defaultScreen
     */
    private void setVideoType(int defaultScreen) {
        switch (defaultScreen) {
            case FULL_SCREEN: // 全屏大小
                // 1、设置屏幕大小,屏幕有多大就显示多大
                videoview.setVideoSize(screenWidth, screenHeight);
                // 2、设置按钮状态
                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_defalut_selector);

                isFullScreen = true;
                break;
            case DEFAULT_SCREEN: // 默认大小
                // 计算默认的最佳大小
                int width = screenWidth; // 屏幕最优宽
                int height = screenHeight; // 屏幕最优高
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoview.setVideoSize(width, height);

                btnVideoSiwchScreen.setBackgroundResource(R.drawable.btn_video_siwch_screen_full_selector);

                isFullScreen = false;
                break;
            default:
                break;
        }
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
     * SeekBar状态变化的监听
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
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        /**
         * 当手指离开的时候回调
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
        }
    }

    /**
     * 准备播放的监听
     */
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

            // 设置视频的宽和高
            mVideoWidth = mediaPlayer.getVideoWidth();
            mVideoHeight = mediaPlayer.getVideoHeight();

            videoview.start(); // 开始播放
            // 1、得到视频的总时长，关联总长度
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);
            // 设置文本总时长
            tvDuration.setText(utils.stringForTime(duration));

            // 默认隐藏控制面板
            hideMediaController();
            setVideoType(DEFAULT_SCREEN);

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
        // 将事件传递给手势识别器
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
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

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;
    }
}
