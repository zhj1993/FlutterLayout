package com.zhj.flutter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhj.flutter.flutter.FlutterLayout;

public class MainActivity extends AppCompatActivity {

    FlutterLayout mFlutterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlutterLayout = (FlutterLayout) findViewById(R.id.flutterlayout);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlutterLayout.addFlutterMsg("测试飘屏");
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlutterLayout.clear();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mFlutterLayout) {
            mFlutterLayout.clear();
        }
    }
}
