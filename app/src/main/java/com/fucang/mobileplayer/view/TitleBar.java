package com.fucang.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fucang.mobileplayer.R;

/**
 * Created by 浮滄 on 2017/5/4.
 */

/**
 * 自定义标题栏
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {

    private View tv_search; // 输入搜索文本框

    private View rl_game; // 游戏

    private View iv_record; // 历史纪录

    private Context context; // 上下文

    /**
     * 通常在代码中实例化该类的时候使用
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }

    /**
     * 当在布局文件使用该类的时候
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设计样式的时候使用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 当布局文件加载完成的时候会掉这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 得到孩子的实例
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_record = getChildAt(3);

        // 设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search: // 搜索
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game: // 游戏
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record: // 播放历史
                Toast.makeText(context, "播放历史", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
