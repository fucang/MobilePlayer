package com.fucang.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAllPlayer(View view) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2016/11/28/mp4/161128092413441663sd.rmvb"), "video/*");
        startActivity(intent);
    }
}
