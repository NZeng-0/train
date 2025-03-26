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

import com.android.train.adapter.OrderAdapter;
import com.android.train.api.AjaxResult;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.databinding.FragmentAllOrdersBinding;
import com.android.train.pojo.Order;
import com.android.train.ui.booking.BookingViewModel;
import com.android.train.utils.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        observe();

        return binding.getRoot();
    }

    private void observe() {
        viewModel.getOrderList().observe(getViewLifecycleOwner(), list -> {
            OrderAdapter adapter = new OrderAdapter(list);
            binding.recyclerViewStations.setAdapter(adapter);
            binding.recyclerViewStations.setLayoutManager(new LinearLayoutManager(getContext()));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadOrderList();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

