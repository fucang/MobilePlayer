package com.fucang.mobileplayer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fucang.mobileplayer.R;
import com.fucang.mobileplayer.bean.GroomBean;
import com.fucang.mobileplayer.utils.DensityUtil;
import com.fucang.mobileplayer.utils.Utils;

import org.xutils.x;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 初始化Item布局
            switch (itemViewType) {
                case TYPE_VIDEO://视频
                    convertView = View.inflate(context, R.layout.all_video_item, null);
                    viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                    viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                    viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                    viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
                    viewHolder.jcv_videoplayer = (JCVideoPlayer) convertView.findViewById(R.id.jcv_videoplayer);
                    break;
                case TYPE_IMAGE://图片
                    convertView = View.inflate(context, R.layout.all_image_item, null);
                    viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                    break;
                case TYPE_TEXT://文字
                    convertView = View.inflate(context, R.layout.all_text_item, null);
                    break;
                case TYPE_GIF://gif
                    convertView = View.inflate(context, R.layout.all_gif_item, null);
                    viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
                    break;
                case TYPE_AD://软件广告
                    convertView = View.inflate(context, R.layout.all_ad_item, null);
                    viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                    viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
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
                    viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
                    viewHolder.tv_shenhe_ding_number = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
                    viewHolder.tv_shenhe_cai_number = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
                    viewHolder.tv_posts_number = (TextView) convertView.findViewById(R.id.tv_posts_number);
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
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), 0, mediaItem.getVideo().getThumbnail().get(0));
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

                break;
            case TYPE_IMAGE://图片
                bindData(viewHolder, mediaItem);
                int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) DensityUtil.getScreenWidth(), height);
                viewHolder.iv_image_icon.setLayoutParams(params);
                if (mediaItem.getImage() != null
                        && mediaItem.getImage().getBig() != null
                        && mediaItem.getImage().getBig().size() > 0) {
                    Glide.with(context).load(mediaItem.getImage().getBig().get(0))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.iv_image_icon);
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
                bindData(viewHolder, mediaItem);
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

        List<GroomBean.ListBean.TagsBean> tagsBeen = mediaItem.getTags();
        if (tagsBeen != null && tagsBeen.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsBeen.size(); i++) {
                buffer.append(tagsBeen.get(i).getName() + "");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());
        }
        viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp());
        viewHolder.tv_shenhe_cai_number.setText(mediaItem.getDown());
        viewHolder.tv_posts_number.setText(mediaItem.getForward());
    }

    static class ViewHolder {

        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;

        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        TextView tv_context;

        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayer jcv_videoplayer;

        ImageView iv_image_icon;
        GifImageView iv_image_gif;
        Button btn_install;
    }
}
