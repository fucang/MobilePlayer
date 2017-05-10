package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/10.
 */

import android.support.test.espresso.core.deps.guava.collect.Lists;

import com.fucang.mobileplayer.domain.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 解析歌词工具类
 */
public class LyricUtils {

    private ArrayList<Lyric> lyrics;

    private boolean isExistLyric = false;

    public void readLyricFile(File file) {
        if (file == null || !file.exists()) { // 歌词文件不存在
            lyrics = null;
            isExistLyric = false;
            return;
        } else {
            // 读取歌词
            BufferedReader reader = null;
            isExistLyric = true;
            lyrics = Lists.newArrayList();
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    parseLyric(line);
                }
            } catch (Exception e) {
                Logger.error("读取歌词文件错误：" + e.getMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    Logger.error("关闭读取歌词流错误：" + e.getMessage());
                }
            }

            // 排序
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric left, Lyric right) {
                    return (int) (left.getTimePoint() - right.getTimePoint());
                }
            });

            // 计算每句的高亮显示时间
            for (int i = 0; i < lyrics.size() - 1; i++) {
                long sleepTime = lyrics.get(i + 1).getTimePoint() - lyrics.get(i).getTimePoint();
                lyrics.get(i).setSleepTime(sleepTime);
            }
        }
    }

    /**
     * 分析歌词
     *
     * @param line [02:04.12][03:37.32][00:59.73]歌词内容
     * @return
     */
    private void parseLyric(String line) {
        int posFront = 0;
        int posBack = 0;
        ArrayList<Lyric> tempLyric = Lists.newArrayList();
        while ((posFront = line.indexOf("[", posBack)) != -1) {
            posBack = line.indexOf("]", posFront);
            if (posBack > posFront) {
                try {
                    Lyric lyric = new Lyric();
                    lyric.setTimePoint(strTime2LongTime(line.substring(posFront + 1, posBack)));
                    tempLyric.add(lyric);
                } catch (Exception e) {
                    Logger.error("歌词转换错误：" + e.getMessage());
                }
            }
        }
        String content = line.substring(Math.max(posFront, posBack) + 1);
        for (int i = 0; i < tempLyric.size(); i++) {
            tempLyric.get(i).setContent(content);
        }
        this.lyrics.addAll(tempLyric);
    }

    // 02:04.12
    private long strTime2LongTime(String substring) {
        // 将02:04.12切割成02  04.12
        String[] s1 = substring.split(":");
        // 将04.12 切割成04  12
        String[] s2 = s1[1].split("\\.");
        // 分
        long min = Long.parseLong(s1[0]);
        // 秒
        long secong = Long.parseLong(s2[0]);
        // 毫秒
        long msec = Long.parseLong(s2[1]);

        return min * 60 * 1000 + secong * 1000 + msec * 10;
    }

    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    public boolean isExistLyric() {
        return isExistLyric;
    }
}
