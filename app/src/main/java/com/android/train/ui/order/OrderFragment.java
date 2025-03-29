package com.android.train.ui.order;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.train.R;
import com.android.train.adapter.OrderPagerAdapter;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.databinding.FragmentOrderBinding;
import com.android.train.ui.booking.BookingViewModel;
import com.android.train.ui.booking.BookingViewModelFactory;
import com.android.train.ui.login.LoginActivity;
import com.android.train.utils.AuthUtil;
import com.android.train.utils.To;
import com.android.train.utils.ToastUtil;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Retrofit;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(!AuthUtil.isLoggedIn(requireContext())) {
            ToastUtil.showToast(requireContext(), "请先登录!");
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            requireActivity().startActivity(intent);
        }

        adapter(root);
        init();

        return root;
    }

    private void adapter(View root) {
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));
        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

    }

    private void init(){
        if(!AuthUtil.isLoggedIn(requireContext()))
            return;
        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;
        OrderPagerAdapter pagerAdapter = new OrderPagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);

        // 监听 Tab 变化
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 监听 ViewPager 滑动，切换 Tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}