package com.fucang.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fucang.mobileplayer.domain.Lyric;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.TreeMap;


/**
 * Created by 浮滄 on 2017/5/10.
 */

/**
 * 自定义歌词显示控件
 */
public class ShowLyricView extends TextView {

    private ArrayList<Lyric> lyrics;

    // 当前是第几句歌词
    private int index;

    // 当前的播放进度
    private long currentPositionTime;

    // 每行的高
    private static final int TEXT_HEIGHT = 20;

    // 画笔
    private Paint currentPaint;

    private Paint otherPaint;

    // 控件的宽
    private int width;

    // 控件的高
    private int height;

    // 高亮时间
    private long sleepTime;

    // 时间戳
    private long timePoint;

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        // 创建画笔
        currentPaint = new Paint();
        currentPaint.setColor(Color.GREEN);
        currentPaint.setTextSize(20);
        currentPaint.setAntiAlias(true);
        // 设置居中对齐
        currentPaint.setTextAlign(Paint.Align.CENTER);

        otherPaint = new Paint();
        otherPaint.setColor(Color.WHITE);
        otherPaint.setTextSize(20);
        otherPaint.setAntiAlias(true);
        otherPaint.setTextAlign(Paint.Align.CENTER);

        this.lyrics = Lists.newArrayList();
        for (int i = 0; i < 1000; ++i) {
            Lyric lyric = new Lyric();
            lyric.setTimePoint(1000 * i);
            lyric.setSleepTime(1500 + i);
            lyric.setContent(i + "=fucang=" + i);
            lyrics.add(lyric);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyrics != null && lyrics.size() > 0) {
            // 绘制歌词
            // 绘制当前句
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText, width / 2, height / 2, currentPaint);
            // 绘制前面部分
            int tempY = height / 2;
            for (int i = index - 1; i >= 0 && tempY > 0; i--) {
                String preText = lyrics.get(i).getContent();
                tempY -= TEXT_HEIGHT;
                canvas.drawText(preText, width / 2, tempY, otherPaint);
            }

            // 绘制后面部分
            tempY = height / 2;
            for (int i = index + 1; i < lyrics.size() && tempY < height; i++) {
                String nextText = lyrics.get(i).getContent();
                tempY += TEXT_HEIGHT;
                canvas.drawText(nextText, width / 2, tempY, otherPaint);
            }
        } else {
            // 没有歌词
            canvas.drawText("抱歉，没有找到对应的歌词", width / 2, height / 2, currentPaint);
        }
    }

    /**
     * 根据当前播放的位置，找出该高亮显示的歌词
     * @param currentPositionTime
     */
    public void setNextLyric(long currentPositionTime) {
        this.currentPositionTime = currentPositionTime;
        if (lyrics == null || lyrics.size() == 0) {
            return;
        }
        for (int i = 1; i < lyrics.size(); i++) {
            if (currentPositionTime < lyrics.get(i).getTimePoint()) {
                if (currentPositionTime >= lyrics.get(i - 1).getTimePoint()) {
                    // 当前正在播放的歌词
                    index = i - 1;
                    sleepTime = lyrics.get(index).getSleepTime();
                    timePoint = lyrics.get(index).getTimePoint();
                }
            }
        }
        // 重新绘制
        invalidate();
    }
}
