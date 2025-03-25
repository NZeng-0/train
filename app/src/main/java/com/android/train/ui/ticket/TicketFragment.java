package com.android.train.ui.ticket;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.train.R;
import com.android.train.databinding.FragmentStationBinding;
import com.android.train.databinding.FragmentTicketBinding;
import com.android.train.utils.DateUtils;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.ToastUtil;

public class TicketFragment extends Fragment {

    private TicketViewModel viewModel;
    private FragmentTicketBinding binding;
    private Intent intent;
    private String trainNumber, departureStation, arrivalStation, departureTime,
            arrivalTime, durationTime, level,carriage,trainSeat, price;

    public static TicketFragment newInstance() {
        return new TicketFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTicketBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.getRoot().setPadding(16, systemBarsInsets.top, 16, systemBarsInsets.bottom);
            return insets;
        });

        viewModel.getRemainingTime().observe(getViewLifecycleOwner(), binding.tvRemainingTime::setText);

        intent = requireActivity().getIntent();
        trainNumber = intent.getStringExtra("trainNumber");
        departureStation = intent.getStringExtra("departureStation");
        arrivalStation = intent.getStringExtra("arrivalStation");
        departureTime = intent.getStringExtra("departureTime");
        arrivalTime = intent.getStringExtra("arrivalTime");
        durationTime = intent.getStringExtra("durationTime");
        level = intent.getStringExtra("level");
        carriage = intent.getStringExtra("carriage");
        trainSeat = intent.getStringExtra("trainSeat");
        price = intent.getStringExtra("price");

        setupListeners();
        init();

        return root;
    }


    private void init() {
        binding.tvDepartureTime.setText(departureTime);
        binding.tvArrivalTime.setText(arrivalTime);
        binding.tvDepartureStation.setText(departureStation);
        binding.tvArrivalStation.setText(arrivalStation);
        binding.tvTrainNumber.setText(trainNumber);
        binding.tvDuration.setText(String.format("历时%s", durationTime));
        binding.tvPassengerName.setText(PreferencesUtil.getString(requireContext(),"realName"));
        binding.tvSeatInfo.setText(String.format("%s座 %s车 %s号", level, carriage, trainSeat));
        binding.tvTicketPrice.setText(String.format("￥%s", price));
        binding.tvTotalPrice.setText(String.format("￥%s", price));

        String date = PreferencesUtil.getString(requireContext(), "selectDate");

        binding.tvDepartureDate.setText(String.format("发车时间: %s", DateUtils.convertToCN(date)));
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.btnPay.setOnClickListener(v -> {
            ToastUtil.showToast(requireContext(), "开始支付流程");
        });

        binding.btnCancel.setOnClickListener(v -> {
            ToastUtil.showToast(requireContext(), "取消订单");
        });
    }
}