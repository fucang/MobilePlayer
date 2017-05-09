package com.fucang.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.domain.MediaItem;
import com.fucang.mobileplayer.utils.Utils;

import java.util.ArrayList;

/**
 * Created by 浮滄 on 2017/5/4.
 */

public class VideoPagerAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<MediaItem> mediaItems;

    private Utils utils; // 转换

    // 判断是视频还是音频
    private final boolean isVideo;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems, boolean isVideo) {
        this.context = context;
        this.mediaItems = mediaItems;
        this.utils = new Utils();
        this.isVideo = isVideo;
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
            view = View.inflate(context, R.layout.item_video_pager, null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHoder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHoder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHoder.tv_size = (TextView) view.findViewById(R.id.tv_size);

            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }
        // 根据position得到列表中相应的数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());
        viewHoder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
        viewHoder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));

        if (!isVideo) {
            // 音频
            viewHoder.iv_icon.setImageResource(R.drawable.music_default_bg);
        }
        return view;
    }

    static class ViewHoder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
