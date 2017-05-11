package com.fucang.mobileplayer.page;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.activity.SystemVideoPlayer;
import com.fucang.mobileplayer.adapter.NetVideoPagerAdapter;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.bean.MediaItem;
import com.fucang.mobileplayer.utils.CacheUtils;
import com.fucang.mobileplayer.utils.Constants;
import com.fucang.mobileplayer.utils.Logger;
import com.fucang.mobileplayer.utils.Utils;
import com.fucang.mobileplayer.view.XListView;

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
    private XListView mListview;

    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    // 装数据的集合
    private ArrayList<MediaItem> mediaItems;

    private NetVideoPagerAdapter adapter;

    // 是否加载更多了
    private boolean isLoadMore = false;

    private Utils utils;

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
        mListview.setPullLoadEnable(true);
        mListview.setXListViewListener(new MyIXListViewListener());

        return view;
    }

    class MyIXListViewListener implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            getMoreDataFromNet();
        }
    }

    private void getMoreDataFromNet() {
        // 联网
        RequestParams params = new RequestParams(Constants.NET_VIDEO_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.info("联网成功==");
                // 解析数据
                processData(result);
                isLoadMore = true;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.info("联网失败==" + ex.getMessage());
                // 显示联网失败
//                mListview.setVisibility(View.GONE);
//                mProgressBar.setVisibility(View.GONE);
//                mTv_nonet.setVisibility(View.VISIBLE);
                showData();

                isLoadMore = false;
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.info("onCancelled==" + cex.getMessage());
                isLoadMore = false;
            }

            @Override
            public void onFinished() {
                Logger.info("onFinished==");
                isLoadMore = false;
            }
        });
    }

    @Override
    public void initData() {
        Logger.info("==========================初始化网络视频");
        super.initData();
        utils = new Utils();
        String saveJson = CacheUtils.getString(context, Constants.NET_VIDEO_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet(); // 联网获取数据
    }

    /**
     * 联网获取数据
     */
    private void getDataFromNet() {
        // 联网
        RequestParams params = new RequestParams(Constants.NET_VIDEO_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.info("联网成功==");
                // 缓存数据
                CacheUtils.putString(context, Constants.NET_VIDEO_URL, result);
                // 解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.info("联网失败==" + ex.getMessage());
                // 显示联网失败
//                mListview.setVisibility(View.GONE);
//                mProgressBar.setVisibility(View.GONE);
//                mTv_nonet.setVisibility(View.VISIBLE);
                showData();
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

    private void onLoad() {
        mListview.stopRefresh();
        mListview.stopLoadMore();
        mListview.setRefreshTime("更新时间" + utils.getSystemTime());
    }

    /**
     * 解析数据
     * @param json
     */
    private void processData(String json) {
        if (!isLoadMore) {
            mediaItems = parseJson(json);
            showData();
        } else {
            // 加载更多:要把得到更多的数据添加到原来的集合mediaItems中
            mediaItems.addAll(parseJson(json));
            // 刷新适配器
            adapter.notifyDataSetChanged();

            isLoadMore = false;

            onLoad();
        }

    }

    private void showData() {
        // 设置适配器
        if (mediaItems != null && mediaItems.size() > 0) {
            // 有数据
            // 设置适配器
            adapter = new NetVideoPagerAdapter(context, mediaItems);
            mListview.setAdapter(adapter);
            onLoad();

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

                        mediaItemList.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            Logger.error("读取网络视频错误");
        }

        return mediaItemList;
    }

    class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

            // 传递列表数据，需要序列化
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position - 1);
            context.startActivity(intent);
        }
    }
}
