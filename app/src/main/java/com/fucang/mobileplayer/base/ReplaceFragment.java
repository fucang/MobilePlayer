package com.fucang.mobileplayer.base;

/**
 * Created by 浮滄 on 2017/5/3.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 解决java.lang.IllegalStateException: Fragment null must be a public static class to be  properly recreated from instance state.
 */
public class ReplaceFragment extends Fragment {

    private BasePager basePager;

    public ReplaceFragment(BasePager basePager) {
        this.basePager = basePager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (basePager != null) {
            // 各个页面的视图
            return basePager.rootview;
        }
        return null;
    }
}
