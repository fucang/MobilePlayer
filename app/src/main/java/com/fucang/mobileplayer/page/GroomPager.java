package com.fucang.mobileplayer.page;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.adapter.GroomAdapter;
import com.fucang.mobileplayer.base.BasePager;
import com.fucang.mobileplayer.bean.GroomBean;
import com.fucang.mobileplayer.utils.CacheUtils;
import com.fucang.mobileplayer.utils.Constants;
import com.fucang.mobileplayer.utils.Logger;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 网络音乐页面
 */
public class GroomPager extends BasePager {

    @ViewInject(R.id.listview)
    private ListView mListview;

    @ViewInject(R.id.tv_nonet)
    private TextView tv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar pb_loading;

    // 页面的数据
    private List<GroomBean.ListBean> datas;

    private GroomAdapter groomAdapter;

    public GroomPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.groom_pager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        Logger.info("==========================初始化推荐数据");
        super.initData();
        // 先加载缓存数据
        String saveJson = CacheUtils.getString(context, Constants.NET_GROOM_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        // 联网
        getDataFromNet();
    }

    /**
     * 联网获取数据
     */
    private void getDataFromNet() {
        // 联网
        RequestParams params = new RequestParams(Constants.NET_GROOM_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.info("联网成功==");
                // 缓存数据
                CacheUtils.putString(context, Constants.NET_GROOM_URL, result);
               // 解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.info("联网失败==" + ex.getMessage());
                // 显示联网失败
                mListview.setVisibility(View.GONE);
                pb_loading.setVisibility(View.GONE);
                tv_nonet.setVisibility(View.VISIBLE);
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
     * 解析数据和显示数据
     * GsonFormat
     * @param json
     */
    private void processData(String json) {
        // 1、GsonFormat生成bean对象
        GroomBean groomBean = parseJson(json);
        // 2、Gson解析数据
        datas = groomBean.getList();
        if (datas != null && datas.size() > 0) {
            // 有数据
            tv_nonet.setVisibility(View.GONE);
            // 设置适配器
            groomAdapter = new GroomAdapter(context, datas);
            mListview.setAdapter(groomAdapter);
        } else {
            // 没有数据
            mListview.setVisibility(View.GONE);
            tv_nonet.setVisibility(View.VISIBLE);
        }
        pb_loading.setVisibility(View.GONE);
    }

    private GroomBean parseJson(String json) {
        return new Gson().fromJson(json, GroomBean.class);
    }
}
