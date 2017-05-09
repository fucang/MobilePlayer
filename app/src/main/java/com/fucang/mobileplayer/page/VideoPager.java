package com.fucang.mobileplayer.page;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.activity.SystemVideoPlayer;
import com.fucang.mobileplayer.adapter.VideoPagerAdapter;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Logger;

import java.util.ArrayList;

/**
 * 本地视频页面
 */
public class VideoPager extends BasePager {

    private ListView listView; // 视频列表

    private TextView tvNoMedia; // 显示没有本地视频

    private ProgressBar pbLoading; // 加载本地视频的转动圈

    private ArrayList<MediaItem> mediaItems; // 保存视频数据的集合

    private VideoPagerAdapter videoPagerAdapter; // 适配器

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                // 有数据
                // 设置适配器
                videoPagerAdapter = new VideoPagerAdapter(context, mediaItems, true);
                listView.setAdapter(videoPagerAdapter);

                // 隐藏文本
                tvNoMedia.setVisibility(View.GONE);
            } else {
                // 没有数据,显示没有数据的文本
                tvNoMedia.setVisibility(View.VISIBLE);
            }
            // 隐藏ProgressBar(转动圈)
            pbLoading.setVisibility(View.GONE);
        }
    };

    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager, null);
        this.listView = (ListView) view.findViewById(R.id.listview);
        this.tvNoMedia = (TextView) view.findViewById(R.id.tv_nomedia);
        this.pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        // 设置listview的item的点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

            // 1、调取系统所有播放器
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            context.startActivity(intent);

            // 2、调用自己写的播放器播放视频——一个播放地址
//            Intent intent = new Intent(context, SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            context.startActivity(intent);

            // 3、传递列表数据，需要序列化
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        Logger.info("==========================初始化本地视频");
        super.initData();

        // 加载本地视频数据
        getDataFromLocal();
    }

    /**
     * 加载本地视频：
     *      1、方法1）根据后缀名遍历sdcard（速度太慢）
     *      2、方法2）从内容提供者里面获取视频
     */
    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        // 创建线程获取数据
        new Thread(){
            @Override
            public void run() {
                super.run();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME, // 视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION, // 视频总时长
                        MediaStore.Video.Media.SIZE, // 视频的文件大小
                        MediaStore.Video.Media.DATA, // 视频文件在sdcard中的绝对地址
                        MediaStore.Video.Media.ARTIST, // 歌曲的演唱者
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
                // handler发消息
                handler.sendEmptyMessage(10);
            }
        }.start(); // thread
    }
}
