package com.fucang.mobileplayer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.bean.GroomBean;
import com.fucang.mobileplayer.utils.Utils;
import com.fucang.mobileplayer.utils.liagde.ProgressTarget;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import org.xutils.x;

import java.io.File;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by 浮滄 on 2017/5/4.
 */

/**
 * 推荐的适配器
 */
public class GroomAdapter extends BaseAdapter {

    // 视频类型
    private static final int TYPE_VIDEO = 0;
    // 图片类型
    private static final int TYPE_IMAGE = 1;
    // 文字类型
    private static final int TYPE_TEXT = 2;
    // GIF动态图片
    private static final int TYPE_GIF = 3;
    // 广告
    private static final int TYPE_AD = 4;

    private Context context;

    private List<GroomBean.ListBean> mGroomDatas;
    private Utils utils;

    public GroomAdapter(Context context, List<GroomBean.ListBean> datas) {
        this.context = context;
        this.mGroomDatas = datas;
        this.utils = new Utils();
    }

    @Override
    public int getViewTypeCount() {
        return 5; // 有5中类型
    }

    /**
     * 根据位置得到viewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        GroomBean.ListBean listBean = mGroomDatas.get(position);
        String type = listBean.getType(); // video, text, image, gif, ad
        int itemViewType = -1;
        if (type.equals("video")) {
            itemViewType = TYPE_VIDEO;
        } else if (type.equals("image")) {
            itemViewType = TYPE_IMAGE;
        } else if (type.equals("text")) {
            itemViewType = TYPE_TEXT;
        } else if (type.equals("gif")) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;
        }
        return itemViewType;
    }

    @Override
    public int getCount() {
        return mGroomDatas.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(position); // 得到类型
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 初始化Item布局
            switch (itemViewType) {
                case TYPE_VIDEO://视频
                    convertView = View.inflate(context, R.layout.all_video_item, null);
                    viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                    viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                    viewHolder.jcv_videoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
                    break;
                case TYPE_IMAGE://图片
                    convertView = View.inflate(context, R.layout.all_image_item, null);
                    viewHolder.iv_image_icon = (LargeImageView) convertView.findViewById(R.id.iv_image_icon);
                    break;
                case TYPE_TEXT://文字
                    convertView = View.inflate(context, R.layout.all_text_item, null);
                    break;
                case TYPE_GIF://gif
                    convertView = View.inflate(context, R.layout.all_gif_item, null);
                    viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
                    break;
                case TYPE_AD://软件广告,此处不显示
                    convertView = View.inflate(context, R.layout.all_ad_item, null);
                    break;
            }
            // 初始化公共视图
            switch (itemViewType) {
                case TYPE_VIDEO:
                case TYPE_IMAGE:
                case TYPE_TEXT:
                case TYPE_GIF:
                    viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                    viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
                    viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);

                    viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                    viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);
                    break;
            }

            // 公共部分
            viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);

            // 设置tag
            convertView.setTag(viewHolder);
        } else {
            // 获取tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 绑定数据
        GroomBean.ListBean mediaItem = mGroomDatas.get(position);
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                bindData(viewHolder, mediaItem);
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0),
                        JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,  "");

                String thumbUrl = mediaItem.getVideo().getThumbnail().get(0);

                Glide.with(context).load(thumbUrl).into(viewHolder.jcv_videoplayer.thumbImageView);

                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                break;
            case TYPE_IMAGE://图片
                bindData(viewHolder, mediaItem);
//                int height = (int) Math.min(mediaItem.getImage().getHeight(), DensityUtil.getScreenHeight(context) * 0.75);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mediaItem.getImage().getWidth(), mediaItem.getImage().getHeight());
                viewHolder.iv_image_icon.setLayoutParams(params);
                if (mediaItem.getImage() != null
                        && mediaItem.getImage().getBig() != null
                        && mediaItem.getImage().getBig().size() > 0) {
                    String imageUrl = mediaItem.getImage().getBig().get(0);
                    Glide.with(context).load(imageUrl).downloadOnly(new ProgressTarget<String, File>(imageUrl, null) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                        }

                        @Override
                        public void onProgress(long bytesRead, long expectedLength) {
                        }

                        @Override
                        public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                            super.onResourceReady(resource, animation);
                            viewHolder.iv_image_icon.setImage(new FileBitmapDecoderFactory(resource));
                        }

                        @Override
                        public void getSize(SizeReadyCallback cb) {
                            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        }
                    });
                }
                break;
            case TYPE_TEXT://文字
                bindData(viewHolder, mediaItem);
                break;
            case TYPE_GIF://gif
                bindData(viewHolder, mediaItem);
                Glide.with(context).load(mediaItem.getGif().getImages().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(viewHolder.iv_image_gif);
                break;
            case TYPE_AD://软件广告
                break;
        }

        return convertView;
    }

    private void bindData(ViewHolder viewHolder, GroomBean.ListBean mediaItem) {
        if (!TextUtils.isEmpty(mediaItem.getText())) {
            viewHolder.tv_context.setText(mediaItem.getText());
        }

        if (mediaItem.getU() != null
                && mediaItem.getU().getHeader() != null
                && mediaItem.getU().getHeader().get(0) != null) {
            x.image().bind(viewHolder.iv_headpic, mediaItem.getU().getHeader().get(0));
        }

        if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
            viewHolder.tv_name.setText(mediaItem.getU().getName() + "");
        }
        viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());
    }

    static class ViewHolder {

        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;

        ImageView iv_video_kind;
        LinearLayout ll_download;

        TextView tv_context;

        TextView tv_play_nums;
        TextView tv_video_duration;
        JCVideoPlayerStandard jcv_videoplayer;

        LargeImageView iv_image_icon;
        GifImageView iv_image_gif;
    }
}
