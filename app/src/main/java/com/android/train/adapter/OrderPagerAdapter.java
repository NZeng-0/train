package com.android.train.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.train.ui.order.AllOrdersFragment;
import com.android.train.ui.order.CompletedOrdersFragment;
import com.android.train.ui.order.UpcomingOrdersFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CompletedOrdersFragment(); // 已出行
            case 2:
                return new UpcomingOrdersFragment(); // 待出行
            default:
                return new AllOrdersFragment(); // 全部
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 3 个 Tab
    }
}

