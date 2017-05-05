package com.fucang.mobileplayer.activity;

/**
 * Created by 浮滄 on 2017/5/4.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.fucang.mobileplayer.R;

/**
 * 系统播放器
 */
public class SystemVideoPlayer extends Activity {

    private VideoView videoview;

    private Uri uri; // 播放地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);

        videoview = (VideoView) findViewById(R.id.videoview);

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
