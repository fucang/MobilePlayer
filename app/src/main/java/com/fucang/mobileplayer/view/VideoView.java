package com.fucang.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by 浮滄 on 2017/5/5.
 */

/**
 * 自定义VideoView
 */
public class VideoView extends android.widget.VideoView {

    /**
     * 在代码中创建的时候调用
     * @param context
     */
    public VideoView(Context context) {
        this(context, null);
    }

    /**
     * 当这个类在布局文件的时候，系统通过该构造方法实例化对象
     * @param context
     * @param attrs
     */
    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设计样式的时候调用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置播放视屏的屏幕的宽和高
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = videoWidth;
        layoutParams.height = videoHeight;
        setLayoutParams(layoutParams);
    }
}
