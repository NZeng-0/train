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
import com.android.train.databinding.FragmentCompletedOrdersBinding;

import retrofit2.Retrofit;

public class CompletedOrdersFragment extends Fragment {
    private FragmentCompletedOrdersBinding binding;
    private OrderViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedOrdersBinding.inflate(inflater, container, false);

        Retrofit retrofit = RetrofitClient.getClient(requireContext());

        RelationService relationService = retrofit.create(RelationService.class);
        OrderViewModelFactory factory = new OrderViewModelFactory(requireContext(), relationService);
        viewModel = new ViewModelProvider(this, factory).get(OrderViewModel.class);

        observe();

        return binding.getRoot();
    }


    private void observe() {
        viewModel.getOrderList().observe(getViewLifecycleOwner(), list -> {
            OrderAdapter adapter = new OrderAdapter(list, null);

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
                binding.recyclerViewCompleted.setAdapter(adapter);
                binding.recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadOrderList(3);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

