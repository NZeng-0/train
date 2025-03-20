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
import com.android.train.model.SeatOption;
import com.android.train.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private BookingViewModel viewModel;

    private FragmentBookingBinding binding;
    private ImageView currentSelectedSeat = null;

    public static BookingFragment newInstance() {
        return new BookingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        initTrain();
        initSeat();
        initSeatService();

        return root;
    }

    private void initTrain(){
        Intent intent = requireActivity().getIntent();
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

    private void initSeat(){
        List<SeatOption> seatList = new ArrayList<>();
        seatList.add(new SeatOption("二等", 97, true));
        seatList.add(new SeatOption("一等", 155, true));
        seatList.add(new SeatOption("无座", 97, false));

        SeatAdapter seatAdapter = new SeatAdapter(seatList, seatOption ->
                ToastUtil.showToast(requireContext(), "选择：" + seatOption.getSeatType())
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.seatLevelList.setLayoutManager(layoutManager);
        binding.seatLevelList.setAdapter(seatAdapter);
    }

    private void initSeatService(){
        binding.seatA.setOnClickListener(v->selectSeat(binding.seatA));
        binding.seatB.setOnClickListener(v->selectSeat(binding.seatB));
        binding.seatC.setOnClickListener(v->selectSeat(binding.seatC));
        binding.seatD.setOnClickListener(v->selectSeat(binding.seatD));
        binding.seatF.setOnClickListener(v->selectSeat(binding.seatF));
    }
    private void selectSeat(ImageView selectedSeat) {
        // 如果点击的是当前已选中的座位，则取消选择
        if (selectedSeat == currentSelectedSeat) {
            resetSeatBackground(selectedSeat);
            currentSelectedSeat = null;
            return;
        }

        // 如果已有选中的座位，重置其背景
        if (currentSelectedSeat != null) {
            resetSeatBackground(currentSelectedSeat);
        }

        // 设置新选中座位的背景为选中状态
        setSeatSelectedBackground(selectedSeat);
        currentSelectedSeat = selectedSeat;
    }

    private void resetSeatBackground(ImageView seat) {
        // 根据座位ID重置为未选中状态的背景
        if (seat.getId() == R.id.seat_a) {
            seat.setBackgroundResource(R.drawable.seat_a);
        } else if (seat.getId() == R.id.seat_b) {
            seat.setBackgroundResource(R.drawable.seat_b);
        } else if (seat.getId() == R.id.seat_c) {
            seat.setBackgroundResource(R.drawable.seat_c);
        } else if (seat.getId() == R.id.seat_d) {
            seat.setBackgroundResource(R.drawable.seat_d);
        } else if (seat.getId() == R.id.seat_f) {
            seat.setBackgroundResource(R.drawable.seat_f);
        }
    }

    private void setSeatSelectedBackground(ImageView seat) {
        // 根据座位ID设置为选中状态的背景
        if (seat.getId() == R.id.seat_a) {
            seat.setBackgroundResource(R.drawable.seat_a_select);
        } else if (seat.getId() == R.id.seat_b) {
            seat.setBackgroundResource(R.drawable.seat_b_select);
        } else if (seat.getId() == R.id.seat_c) {
            seat.setBackgroundResource(R.drawable.seat_c_select);
        } else if (seat.getId() == R.id.seat_d) {
            seat.setBackgroundResource(R.drawable.seat_d_select);
        } else if (seat.getId() == R.id.seat_f) {
            seat.setBackgroundResource(R.drawable.seat_f_select);
        }
    }
}
