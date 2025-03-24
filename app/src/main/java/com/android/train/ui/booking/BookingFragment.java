package com.android.train.ui.booking;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.train.R;
import com.android.train.adapter.SeatAdapter;
import com.android.train.databinding.FragmentBookingBinding;
import com.android.train.model.SeatInfo;
import com.android.train.model.SeatOption;
import com.android.train.ui.ticket.TicketActivity;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingFragment extends Fragment {
    private BookingViewModel viewModel;
    private FragmentBookingBinding binding;
    private Intent intent;

    public static BookingFragment newInstance() {
        return new BookingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        viewModel.initSeatInfoMap();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        // 返回
        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        intent = requireActivity().getIntent();

        initTrain();
        initSeat();
        setupSeatClickListeners();
        setupObservers();

        String realName = PreferencesUtil.getString(requireContext(), "realName");
        String idCard = PreferencesUtil.getString(requireContext(), "idCard");
        binding.passengerName.setText(realName);
        binding.idNumber.setText(BookingViewModel.maskIdCard(idCard));

        binding.btnSubmitOrder.setOnClickListener(v -> {
            if(!viewModel.validateOrderSubmission()) {
                ToastUtil.showToast(requireContext(), "请选择座位");
                return;
            }
            ToastUtil.showToast(requireContext(), "订单处理中...");
            Intent intent = new Intent(requireContext(), TicketActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void setupObservers() {
        // Observe seat selection changes
        viewModel.getSelectedSeatNumber().observe(getViewLifecycleOwner(), seatNumber -> {
            // Additional UI updates can be performed here when seat selection changes
        });

        // Observe seat class changes
        viewModel.getSelectedSeatClass().observe(getViewLifecycleOwner(), seatClass -> {
            binding.seatClass.setText(seatClass);
        });

        // Observe seat visibility changes
        viewModel.getSeatVisibility().observe(getViewLifecycleOwner(), this::updateSeatVisibility);
    }

    private void updateSeatVisibility(Map<Integer, Boolean> visibilityMap) {
        Map<ImageView, Integer> seatViewMap = new HashMap<>();
        seatViewMap.put(binding.seatA, R.id.seat_a);
        seatViewMap.put(binding.seatB, R.id.seat_b);
        seatViewMap.put(binding.seatC, R.id.seat_c);
        seatViewMap.put(binding.seatD, R.id.seat_d);
        seatViewMap.put(binding.seatF, R.id.seat_f);

        for (Map.Entry<ImageView, Integer> entry : seatViewMap.entrySet()) {
            ImageView seat = entry.getKey();
            Integer seatId = entry.getValue();
            boolean isVisible = Boolean.TRUE.equals(visibilityMap.getOrDefault(seatId, false));

            seat.setVisibility(isVisible ? View.VISIBLE : View.GONE);

            // Reset background to unselected state for visible seats
            if (isVisible) {
                SeatInfo info = viewModel.getSeatInfo(seatId);
                if (info != null) {
                    seat.setBackgroundResource(info.normalBackground);
                }
            }
        }
    }

    private void initTrain() {
        if (intent != null) {
            String trainNumber = intent.getStringExtra("trainNumber");
            String departureStation = intent.getStringExtra("departureStation");
            String arrivalStation = intent.getStringExtra("arrivalStation");
            String departureTime = intent.getStringExtra("departureTime");
            String arrivalTime = intent.getStringExtra("arrivalTime");
            String durationTime = intent.getStringExtra("durationTime");

            binding.tvDepartureTime.setText(departureTime);
            binding.tvArrivalTime.setText(arrivalTime);
            binding.tvDepartureStation.setText(departureStation);
            binding.tvArrivalStation.setText(arrivalStation);
            binding.tvTrainNumber.setText(trainNumber);
            binding.tvDuration.setText(durationTime);
        }
    }

    private void initSeat() {
        int[] prices = intent.getIntArrayExtra("prices");
        String[] levels = new String[]{"二等", "一等", "商务"};
        List<SeatOption> seatList = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(prices).length; i++) {
            seatList.add(new SeatOption(levels[i], prices[i], true));
        }

        SeatAdapter seatAdapter = new SeatAdapter(seatList, seatOption -> {
            viewModel.updateSeatState(seatOption.getSeatType());
            ToastUtil.showToast(requireContext(), "选择：" + seatOption.getSeatType());
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.seatLevelList.setLayoutManager(layoutManager);
        binding.seatLevelList.setAdapter(seatAdapter);
    }

    private void setupSeatClickListeners() {
        // 座位列表
        Map<ImageView, Integer> seatViewMap = new HashMap<>();
        seatViewMap.put(binding.seatA, R.id.seat_a);
        seatViewMap.put(binding.seatB, R.id.seat_b);
        seatViewMap.put(binding.seatC, R.id.seat_c);
        seatViewMap.put(binding.seatD, R.id.seat_d);
        seatViewMap.put(binding.seatF, R.id.seat_f);

        // Clear any previous listeners and set new ones
        for (Map.Entry<ImageView, Integer> entry : seatViewMap.entrySet()) {
            ImageView seat = entry.getKey();
            Integer seatId = entry.getValue();

            seat.setOnClickListener(v -> {
                // Only process clicks on visible seats
                if (seat.getVisibility() == View.VISIBLE) {
                    String currentSelection = viewModel.getSelectedSeatNumber().getValue();
                    SeatInfo info = viewModel.getSeatInfo(seatId);

                    if (info != null && info.seatNumber.equals(currentSelection)) {
                        // Deselect current seat
                        viewModel.resetSeatSelection();
                        seat.setBackgroundResource(info.normalBackground);
                    } else {
                        // Reset all seats to unselected state
                        for (Map.Entry<ImageView, Integer> resetEntry : seatViewMap.entrySet()) {
                            ImageView resetSeat = resetEntry.getKey();
                            Integer resetSeatId = resetEntry.getValue();

                            if (resetSeat.getVisibility() == View.VISIBLE) {
                                SeatInfo resetInfo = viewModel.getSeatInfo(resetSeatId);
                                if (resetInfo != null) {
                                    resetSeat.setBackgroundResource(resetInfo.normalBackground);
                                }
                            }
                        }

                        // Select new seat
                        viewModel.setSelectedSeat(seatId);
                        if (info != null) {
                            seat.setBackgroundResource(info.selectedBackground);
                        }
                    }
                }
            });
        }
    }
}
