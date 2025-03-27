package com.android.train;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.train.utils.NotificationUtil;
import com.android.train.utils.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.train.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private NavController navController;

    public static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化导航
        setupNavigation();

        requestNotificationPermission();
    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.fragment_container);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_order, R.id.navigation_profile)
                .build();
        BottomNavigationView navView = findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{POST_NOTIFICATIONS},
                        1);
            }
        }
    }


    // 处理用户的权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationUtil.createNotificationChannel(this);
            } else {
                ToastUtil.showToast(this, "请开启通知权限");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("nav")) {
            int navIndex = intent.getIntExtra("nav", 1);

            // 先更新BottomNavigationView的选中状态
            BottomNavigationView navView = findViewById(R.id.navigation);
            navView.setSelectedItemId(getNavItemId(navIndex));

            // 再执行导航
            switch (navIndex) {
                case 2:
                    navController.navigate(R.id.navigation_order);
                    break;
                case 3:
                    navController.navigate(R.id.navigation_profile);
                    break;
                default:
                    navController.navigate(R.id.navigation_home);
            }
        }
    }

    private int getNavItemId(int navIndex) {
        switch (navIndex) {
            case 2: return R.id.navigation_order;
            case 3: return R.id.navigation_profile;
            default: return R.id.navigation_home;
        }
    }
}