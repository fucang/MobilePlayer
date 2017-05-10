package com.fucang.mobileplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.adapter.SearchAdapter;
import com.fucang.mobileplayer.domain.SearchBean;
import com.fucang.mobileplayer.utils.Constants;
import com.fucang.mobileplayer.utils.Logger;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by 浮滄 on 2017/5/10.
 */
public class SearchActivity extends Activity {

    private EditText etInput;
    private TextView tv_search_data;
    private ListView listview;
    private ProgressBar progressBar;
    private TextView tvNodata;
    private String url;
    private List<SearchBean.ItemData> items;

    private SearchAdapter adapter;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-10 19:19:43 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        etInput = (EditText)findViewById( R.id.et_input );
        tv_search_data = (TextView)findViewById( R.id.tv_search_data );
        listview = (ListView)findViewById( R.id.listview );
        progressBar = (ProgressBar)findViewById( R.id.progressBar );
        tvNodata = (TextView)findViewById( R.id.tv_nodata );

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        // 设置点击事件
        tv_search_data.setOnClickListener(myOnClickListener);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_search_data: // 搜索
                    searchText();
                    break;
                default:
                    break;
            }
        }
    }

    private void searchText() {
        String text = etInput.getText().toString().trim();

        if (!TextUtils.isEmpty(text)) {
            if (items != null && items.size() > 0) {
                items.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
            try {
                text = URLEncoder.encode(text, "UTF-8");
                url = Constants.NET_SEARCH_URL + text;
                getDataFromNet();
            } catch (UnsupportedEncodingException e) {
                Logger.error("搜索网路数据错误：" + e.getMessage());
            }
        }
    }

    private void getDataFromNet() {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void processData(String result) {
        SearchBean searchBean = parseJson(result);
        items = searchBean.getItems();

        showData();
    }

    private void showData() {
        if (items != null && items.size() > 0) {
            // 设置适配器
            adapter = new SearchAdapter(this, items);
            listview.setAdapter(adapter);
            tvNodata.setVisibility(View.GONE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 解析json数据
     * @param result
     * @return
     */
    private SearchBean parseJson(String result) {
        return new Gson().fromJson(result, SearchBean.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViews();
    }
}
