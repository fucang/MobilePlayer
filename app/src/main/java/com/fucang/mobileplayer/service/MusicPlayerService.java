package com.fucang.mobileplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.activity.AudioPlayer;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.CacheUtils;
import com.fucang.mobileplayer.utils.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 浮滄 on 2017/5/9.
 */

public class MusicPlayerService extends Service {

    public static final String COM_FUCANG_MOBILEPLAYER_OPEN_AUDIO = "com.fucang.mobileplayer_OPEN_AUDIO";

    // 顺序播放模式
    public static final int REPEAT_NORMAL = 1;

    // 单曲循环播放模式
    public static final int REPEAT_SINGLE = 2;

    // 循环播放模式
    public static final int REPEAT_ALL = 3;

    // 播放模式
    private int playmode = REPEAT_NORMAL;

    private ArrayList<Object> mediaItems;

    private int position;

    // 当前播放的音频文件
    private MediaItem mediaItem;

    // 用于播放音乐
    private MediaPlayer mediaPlayer;

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

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        playmode = CacheUtils.getPlaymode(this, "playmode");
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
//                mediaPlayer.release();
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

                if (playmode == MusicPlayerService.REPEAT_SINGLE) {
                    // 单曲循环播放，不会触发播放完成的回调
                    mediaPlayer.setLooping(true);
                } else {
                    mediaPlayer.setLooping(false);
                }

            } catch (IOException e) {
                Logger.error("获取播放的音乐数据错误：" + e.getMessage());
            }
        } else {
            Toast.makeText(MusicPlayerService.this, "本地没有音频数据...", Toast.LENGTH_SHORT).show();
        }
    }

    private NotificationManager manager;

    /**
     * 播放音乐
     */
    private void start() {
        mediaPlayer.start();
        // 当播放歌曲的时候，在状态栏显示正在播放的歌曲，点击的时候，可以进入音乐播放页面
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayer.class);
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("fucang音乐")
                .setContentText("正在播放：" + getName())
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mediaPlayer.pause();
        manager.cancel(1);
    }

    /**
     * 停止
     */
    private void stop() {
       // mediaPlayer.stop();
    }

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 得到当前的播放的音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * 得到歌手
     *
     * @return
     */
    private String getArtist() {
        return mediaItem.getArtist();
    }

    /**
     * 得到歌曲名字
     *
     * @return
     */
    private String getName() {
        return mediaItem.getName();
    }

    /**
     * 得到歌曲播放的路径
     *
     * @return
     */
    private String getAudioPath() {
        return mediaItem.getData();
    }

    /**
     * 播放下一个
     */
    private void next() {
        // 1、根据当前的播放模式设置下一个position
        // 2、根据position播放下一个音频
        int playMode = getPlayMode();
        ++position;
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position >= mediaItems.size()) {
                position = mediaItems.size() - 1;
                mediaPlayer.pause();
            } else {
                openAudio(position);
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            if (position >= mediaItems.size()) {
                position = 0;
            }
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            if (position >= mediaItems.size()) {
                position = 0;
            }
            openAudio(position);
        } else {
            if (position >= mediaItems.size()) {
                position = mediaItems.size() - 1;
            } else {
                openAudio(position);
                mediaPlayer.pause();
            }
        }
    }

    /**
     * 播放上一个
     */
    private void pre() {
        int playMode = getPlayMode();
        --position;
        if (playMode == MusicPlayerService.REPEAT_NORMAL) {
            if (position < 0) {
                position = 0;
                mediaPlayer.pause();
            } else {
                openAudio(position);
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            if (position < 0) {
                position = mediaItems.size() - 1;
            }
            openAudio(position);
        } else {
            if (position < 0) {
                position = 0;
                mediaPlayer.pause();
            } else {
                openAudio(position);
            }
        }
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    private void setPlayMode(int playMode) {
        this.playmode = playMode;
        CacheUtils.putPlaymode(this, "playmode", this.playmode);
        if (playmode == MusicPlayerService.REPEAT_SINGLE) {
            // 单曲循环播放，不会触发播放完成的回调
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return this.playmode;
    }

    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            // 通知activity来获取信息——广播
//            notifyChange(COM_FUCANG_MOBILEPLAYER_OPEN_AUDIO);
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    /**
     * 根据动作发送广播
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
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
