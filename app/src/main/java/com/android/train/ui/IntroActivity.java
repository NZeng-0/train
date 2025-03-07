package com.android.train.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.train.MainActivity;
import com.android.train.R;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // EdgeToEdge 适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        // 读取 SharedPreferences 判断是否是第一次启动
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("first_launch", true);

        if (isFirstLaunch) {
            prefs.edit().putBoolean("first_launch", false).apply();
        }

        // 延迟 1.5 秒后跳转到 MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            // 添加淡入淡出动画
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1500);

    }
}
