package com.android.train.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.train.R;
import com.android.train.adapter.OrderAdapter;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.databinding.FragmentAllOrdersBinding;
import com.android.train.utils.AuthUtil;
import com.android.train.utils.NotificationUtil;

import retrofit2.Retrofit;

public class AllOrdersFragment extends Fragment {
    private FragmentAllOrdersBinding binding;

    private OrderViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllOrdersBinding.inflate(inflater, container, false);

        Retrofit retrofit = RetrofitClient.getClient(requireContext());

        RelationService relationService = retrofit.create(RelationService.class);
        OrderViewModelFactory factory = new OrderViewModelFactory(requireContext(), relationService);
        viewModel = new ViewModelProvider(this, factory).get(OrderViewModel.class);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadOrderList(-1);
            binding.swipeRefresh.setRefreshing(false);
        });

        observe();

        return binding.getRoot();
    }

    private void observe() {
        viewModel.getOrderList().observe(getViewLifecycleOwner(), list -> {
            OrderAdapter adapter = new OrderAdapter(list, id -> viewModel.cancelOrder(id, -1));

            View currentView = binding.viewSwitcher.getCurrentView();

            if (list.isEmpty()) {
                // 当前显示的不是 "无数据" 图片时，切换到 "无数据"
                if (currentView.getId() != R.id.no_data_image) {
                    binding.viewSwitcher.setDisplayedChild(1); // 切换到 noDataImage
                }
            } else {
                // 当前显示的不是 "列表" 时，切换到 "列表"
                if (currentView.getId() != R.id.swipe_refresh) {
                    binding.viewSwitcher.setDisplayedChild(0); // 切换到 RecyclerView
                }
                binding.recyclerViewStations.setAdapter(adapter);
                binding.recyclerViewStations.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!AuthUtil.isLoggedIn(requireContext()))
            return;
        if (viewModel != null) {
            viewModel.loadOrderList(-1);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

