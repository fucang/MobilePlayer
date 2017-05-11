package com.fucang.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fucang.mobileplayer.IMusicPlayerService;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.bean.MediaItem;
import com.fucang.mobileplayer.service.MusicPlayerService;
import com.fucang.mobileplayer.utils.Logger;
import com.fucang.mobileplayer.utils.LyricUtils;
import com.fucang.mobileplayer.utils.Utils;
import com.fucang.mobileplayer.view.BaseVisualizerView;
import com.fucang.mobileplayer.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by 浮滄 on 2017/5/9.
 */
public class AudioPlayer extends Activity implements View.OnClickListener {

    // 进度更新
    private static final int PROGRESS = 1;

    // 显示歌词
    private static final int SHOW_LYRIC = 2;

    // true:从状态栏进入，不需要重新播放
    // false：从播放列表进入
    private boolean notification;

    private Utils utils;

    private int position;

    private IMusicPlayerService service;

    //    private ImageView imageView;
    private TextView ivArtist;
    private TextView ivName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;

    private ShowLyricView showLyricView;

    private BaseVisualizerView baseVisualizerView;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-09 18:37:42 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

        setContentView(R.layout.activity_audio_player);
//        ivIcon = (ImageView) findViewById(R.id.iv_icon);
//        ivIcon.setBackgroundResource(R.drawable.animation_list);
//        AnimationDrawable rocketAnimation = (AnimationDrawable) ivIcon.getBackground();
//        rocketAnimation.start();

        ivArtist = (TextView) findViewById(R.id.iv_artist);
        ivName = (TextView) findViewById(R.id.iv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnLyrc = (Button) findViewById(R.id.btn_lyrc);

        showLyricView = (ShowLyricView) findViewById(R.id.showLyricView);
        baseVisualizerView = (BaseVisualizerView) findViewById(R.id.baseVisualizerView);

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnLyrc.setOnClickListener(this);


        // 设置视频拖动
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-09 18:37:42 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            // Handle clicks for btnAudioPlaymode
            setPlayMode();
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
            if (service != null) {
                try {
                    service.pre();
                } catch (RemoteException e) {
                    Logger.error("播放上一个音频出错：" + e.getMessage());
                }
            }
        } else if (v == btnAudioStartPause) {
            // Handle clicks for btnAudioStartPause
            if (service != null) {
                try {
                    if (service.isPlaying()) {
                        // 音频暂停
                        service.pause();
                        // 按钮播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    } else {
                        // 音频播放
                        service.start();
                        // 按钮暂停
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    Logger.error("获取音乐的播放状态错误：" + e.getMessage());
                }
            }
        } else if (v == btnAudioNext) {
            // Handle clicks for btnAudioNext
            if (service != null) {
                try {
                    service.next();
                } catch (RemoteException e) {
                    Logger.error("播放下一个音频出错：" + e.getMessage());
                }
            }
        } else if (v == btnLyrc) {
            // Handle clicks for btnLyrc
        }
    }

    private void setPlayMode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                playmode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                playmode = MusicPlayerService.REPEAT_ALL;
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            } else {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }
            // 保存
            service.setPlayMode(playmode);
            // 设置图片
            showPlaymode(true);
        } catch (RemoteException e) {
            Logger.error("设置播放模式错误：" + e.getMessage());
        }
    }

    private void showPlaymode(boolean isToast) {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                if (isToast) {
                    Toast.makeText(AudioPlayer.this, "顺序播放", Toast.LENGTH_SHORT).show();
                }
            } else if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                if (isToast) {
                    Toast.makeText(AudioPlayer.this, "单曲循环", Toast.LENGTH_SHORT).show();
                }
            } else if (playmode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                if (isToast) {
                    Toast.makeText(AudioPlayer.this, "列表循环", Toast.LENGTH_SHORT).show();
                }
            } else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                if (isToast) {
                    Toast.makeText(AudioPlayer.this, "顺序播放", Toast.LENGTH_SHORT).show();
                }
            }

            // 校验播放和暂停
            if (service.isPlaying()) {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
            } else {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            }
        } catch (RemoteException e) {
            Logger.error("设置播放模式图片错误：" + e.getMessage());
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS: // 进度更新
                    try {
                        // 1、得到当前进度
                        int currentPosition = service.getCurrentPosition();
                        // 2、设置SeekBar.setProgress（进度）
                        seekbarAudio.setProgress(currentPosition);
                        // 3、时间进度更新
                        tvTime.setText(utils.stringForTime(currentPosition) + "/" + utils.stringForTime(service.getDuration()));
                        // 4、每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        Logger.error("发送进度条更新消息错误：" + e.getMessage());
                    }
                    break;

                case SHOW_LYRIC: // 显示歌词
                    try {
                        // 1、得到当前进度
                        long currentPosition = service.getCurrentPosition();
                        // 把进度传入ShowLyricView并计算该高亮哪一句
                        showLyricView.setNextLyric(currentPosition);
                        // 实时的发消息
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        Logger.error("发送歌词更新消息错误：" + e.getMessage());
                    }
                    break;

                default:
                    break;
            }
        }
    };

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
                    if (!notification) { // 从播放列表进入
                        service.openAudio(position);
                    } else { // 从状态栏进入
                        showViewData();
                    }
                } catch (RemoteException e) {
                    Logger.error("服务播放歌曲错误：" + e.getMessage());
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
                    Logger.error("断开服务错误：" + e.getMessage());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        // 移除所有消息
        handler.removeCallbacksAndMessages(null);

        // 取消注册广播
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//            receiver = null;
//        }

        EventBus.getDefault().unregister(this);

        // 与服务解绑定
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        // 绑定并且启动服务
        bindAndStartService();
    }

    private void initData() {
        utils = new Utils();
        // 注册广播
//        receiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MusicPlayerService.COM_FUCANG_MOBILEPLAYER_OPEN_AUDIO);
//        registerReceiver(receiver, intentFilter);

        // 1、EventBus注册
        EventBus.getDefault().register(this);


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
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification) { // 从播放列表进入
            position = getIntent().getIntExtra("position", 0);
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showData(null);
        }
    }

    // 订阅方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void showData(MediaItem mediaItem) {
        // 发消息歌词同步 
        showLyric();

        showViewData();
        showPlaymode(false);

        setupVisualizerFxAndUi();
    }

    private Visualizer mVisualizer;

    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi() {
        try {
            int audioSessionid = service.getAudioSessionId();
            mVisualizer = new Visualizer(audioSessionid);
            // 参数内必须是2的位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // 设置允许波形表示，并且捕获它
            baseVisualizerView.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            Logger.error("频谱闪动错误：" + e.getMessage());
        }
    }

    private void showLyric() {
        LyricUtils lyricUtils = new LyricUtils();
        // 传送歌词文件
        try {
            String path = service.getAudioPath();
            path = path.substring(0, path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if (!file.exists()) {
                file = new File(path + ".krc"); // 酷狗
            }
            if (!file.exists()) {
                file = new File(path + ".txt");
            }
            lyricUtils.readLyricFile(file);
            showLyricView.setLyrics(lyricUtils.getLyrics());

            if (lyricUtils.isExistLyric()) {
                handler.sendEmptyMessage(SHOW_LYRIC);
            }
        } catch (RemoteException e) {
            Logger.error("获取歌词文件路径错误：" + e.getMessage());
        }
    }

    private void showViewData() {
        try {
            ivArtist.setText(service.getArtist());
            ivName.setText(service.getName());
            // 设置进度条的最大值
            seekbarAudio.setMax(service.getDuration());
            // 发消息
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            Logger.error("显示音乐内容错误：" + e.getMessage());
        }
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                // 拖动进度
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    Logger.error("拖动音频异常：" + e.getMessage());
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }
}
