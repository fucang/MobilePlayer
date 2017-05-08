package com.fucang.mobileplayer.page;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.activity.SystemVideoPlayer;
import com.fucang.mobileplayer.adapter.NetVideoPagerAdapter;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Constants;
import com.fucang.mobileplayer.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 网络视频页面
 */
public class NetVideoPager extends BasePager {

    // 有x.view().inject(this, view);才可使用注解
    @ViewInject(R.id.listview)
    private ListView mListview;

    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    // 装数据的集合
    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapter adapter;

    public NetVideoPager(Context context) {
        super(context);
    }

    /**
     * 初始化当前页面
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netvideo_pager, null);
        x.view().inject(this, view);
        mListview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    @Override
    public void initData() {
        Logger.info("==========================初始化网络视频");
        super.initData();

        // 联网
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.info("联网成功==");
                // 解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.info("联网失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.info("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Logger.info("onFinished==");
            }
        });
    }

    /**
     * 解析数据
     * @param json
     */
    private void processData(String json) {
        mediaItems = parseJson(json);

        // 设置适配器
        if (mediaItems != null && mediaItems.size() > 0) {
            // 有数据
            // 设置适配器
            adapter = new NetVideoPagerAdapter(context, mediaItems);
            mListview.setAdapter(adapter);

            // 隐藏文本
            mTv_nonet.setVisibility(View.GONE);
        } else {
            // 没有数据,显示没有数据的文本
            mTv_nonet.setVisibility(View.VISIBLE);
        }
        // 隐藏ProgressBar(转动圈)
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     *      两种方法：
     *      1、用系统的接口解析
     *      2、使用第三方解析数据（Gson，fastjson）
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(String json) {
        ArrayList<MediaItem> mediaItemList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if (jsonObjectItem != null) {
                        MediaItem mediaItem = new MediaItem();

                        String movieName = jsonObjectItem.optString("movieName");
                        mediaItem.setName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");
                        mediaItem.setDesc(videoTitle);

                        String imageUrl = jsonObjectItem.optString("coverImg");
                        mediaItem.setImageUrl(imageUrl);

                        String hightUrl = jsonObjectItem.optString("hightUrl");
                        mediaItem.setData(hightUrl);

                        mediaItems.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            Logger.error("读取网络视频错误");
            throw new RuntimeException(e);
        }

        return mediaItemList;
    }

    class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

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
}
