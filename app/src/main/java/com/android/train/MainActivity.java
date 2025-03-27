package com.android.train;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.train.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取NavController
        navController = Navigation.findNavController(this, R.id.fragment_container);

        // 设置AppBarConfiguration，指定哪些Fragment是顶级目标
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_order, R.id.navigation_profile)
                .build();

        // 将BottomNavigationView与NavController关联
        BottomNavigationView navView = findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(navView, navController);

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