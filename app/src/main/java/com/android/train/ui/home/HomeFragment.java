package com.android.train.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.R;
import com.android.train.databinding.FragmentHomeBinding;
import com.android.train.utils.AddressPickerUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TextView tvDeparture, tvDestination;
    private ImageView swap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        tvDeparture = binding.tvDeparture;
        tvDestination = binding.tvDestination;
        swap = binding.swap;
        View root = binding.getRoot();
        // 观察 LiveData，并更新 UI
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), departure -> tvDeparture.setText(departure));

        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), destination -> tvDestination.setText(destination));

        // 设置 swap 按钮点击事件
        swap.setOnClickListener(v -> {
            // 调用 ViewModel 中的方法交换文本
            viewModel.swapText();
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDeparture = view.findViewById(R.id.tv_departure);
        tvDestination = view.findViewById(R.id.tv_destination);

        // 绑定 ViewModel 数据
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), tvDeparture::setText);
        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), tvDestination::setText);

        // 点击选择出发地
        tvDeparture.setOnClickListener(v ->
                AddressPickerUtil.showAddressPicker(requireContext(), address ->
                        viewModel.setDepartureCity(address)
                )
        );

        // 点击选择目的地
        tvDestination.setOnClickListener(v ->
                AddressPickerUtil.showAddressPicker(requireContext(), address ->
                        viewModel.setDestinationCity(address)
                )
        );
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}