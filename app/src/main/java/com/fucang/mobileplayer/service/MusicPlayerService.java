package com.fucang.mobileplayer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.fucang.mobileplayer.IMusicPlayerService;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 浮滄 on 2017/5/9.
 */

public class MusicPlayerService extends Service {

    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {

        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            service.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }
    };

    private ArrayList<Object> mediaItems;

    private int position;

    // 当前播放的音频文件
    private MediaItem mediaItem;

    // 用于播放音乐
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 加载音乐列表
        getDataFromLocal();
    }

    /**
     * 加载音乐列表
     */
    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        // 创建线程获取数据
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                };
                Cursor query = resolver.query(uri, objs, null, null, null);
                if (query != null) {
                    while (query.moveToNext()) {
                        String name = query.getString(0); // 视频的名称
                        long duration = query.getLong(1); // 视频的时长
                        long size = query.getLong(2); // 视频的文件大小
                        String data = query.getString(3); // 视频的播放地址
                        String artist = query.getString(4); // 艺术家

                        mediaItems.add(new MediaItem(name, duration, size, data, artist));
                    }
                    query.close();
                }
            }
        }.start(); // thread
    }

    /**
     * 根据位置打开对应的音频文件,并且播放
     *
     * @param position
     */
    private void openAudio(int position) {
        this.position = position;
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = (MediaItem) mediaItems.get(position);
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer.reset();
            }
            try {
                mediaPlayer = new MediaPlayer();
                // 设置监听
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());

                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Logger.error("获取播放的音乐数据错误：" + e.getMessage());
            }
        } else {
            Toast.makeText(MusicPlayerService.this, "本地没有音频数据...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放音乐
     */
    private void start() {
        mediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mediaPlayer.pause();
    }

    /**
     * 停止
     */
    private void stop() {

    }

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return 0;
    }

    /**
     * 得到当前的播放的音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return 0;
    }

    /**
     * 得到歌手
     *
     * @return
     */
    private String getArtist() {
        return "";
    }

    /**
     * 得到歌曲名字
     *
     * @return
     */
    private String getName() {
        return "";
    }

    /**
     * 得到歌曲播放的路径
     *
     * @return
     */
    private String getAudioPath() {
        return "";
    }

    /**
     * 播放下一个
     */
    private void next() {

    }

    /**
     * 播放上一个
     */
    private void pre() {

    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    private void setPlayMode(int playMode) {

    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return 0;
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            start();
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            next();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            // 出错默认播放下一首
            next();
            return true;
        }
    }
}
