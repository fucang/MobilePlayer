package com.fucang.mobileplayer.utils;

/**
 * Created by 浮滄 on 2017/5/10.
 */

import android.support.test.espresso.core.deps.guava.collect.Lists;

import com.fucang.mobileplayer.domain.Lyric;

import java.io.BufferedInputStream;
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
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), getCharset(file)));
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

    /**
     * 判断文件编码
     * @param file 文件
     * @return 编码：GBK,UTF-8,UTF-16LE
     */
    public String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }
}
