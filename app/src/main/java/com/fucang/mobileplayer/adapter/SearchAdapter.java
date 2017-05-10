package com.fucang.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.domain.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 浮滄 on 2017/5/4.
 */

public class SearchAdapter extends BaseAdapter {

    private Context context;

    private List<SearchBean.ItemData> mediaItems;

    public SearchAdapter(Context context, List<SearchBean.ItemData> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_net_video_pager, null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHoder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHoder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);

            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        // 根据position得到列表中相应的数据
        SearchBean.ItemData mediaItem = mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getItemTitle());
        viewHoder.tv_desc.setText(mediaItem.getKeywords());

        // 使用xUtil3请求图片
//        x.image().bind(viewHoder.iv_icon, mediaItem.getImageUrl());

        // 使用Glide请求图片
        Glide.with(context)
                .load(mediaItem.getItemImage().getImgUrl1())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default_icon)
                .error(R.drawable.video_default_icon)
                .into(viewHoder.iv_icon);

        return view;
    }

    static class ViewHoder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
