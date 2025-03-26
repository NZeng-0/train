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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.train.R;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.databinding.FragmentTicketBinding;
import com.android.train.pojo.Order;
import com.android.train.utils.DateUtils;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.To;
import com.android.train.utils.ToastUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

import retrofit2.Retrofit;

public class TicketFragment extends Fragment {

    private TicketViewModel viewModel;
    private FragmentTicketBinding binding;
    private Intent intent;
    private String realName, trainNumber, departureStation, arrivalStation, departureTime,
            arrivalTime, durationTime, level, carriage, trainSeat, price, seatId, trainId, date;

    public static TicketFragment newInstance() {
        return new TicketFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = RetrofitClient.getClient(requireContext());
        RelationService relationService = retrofit.create(RelationService.class);
        TicketViewModelFactory factory = new TicketViewModelFactory(requireContext(), relationService);
        viewModel = new ViewModelProvider(this, factory).get(TicketViewModel.class);
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

        realName = PreferencesUtil.getString(requireContext(), "realName");

        viewModel.getRemainingTime().observe(getViewLifecycleOwner(), binding.tvRemainingTime::setText);

        intent = requireActivity().getIntent();
        trainNumber = intent.getStringExtra("trainNumber");
        departureStation = intent.getStringExtra("departureStation");
        arrivalStation = intent.getStringExtra("arrivalStation");
        departureTime = intent.getStringExtra("departureTime");
        arrivalTime = intent.getStringExtra("arrivalTime");
        durationTime = intent.getStringExtra("durationTime");
        seatId = intent.getStringExtra("seatId");
        level = intent.getStringExtra("level");
        carriage = intent.getStringExtra("carriage");
        trainSeat = intent.getStringExtra("trainSeat");
        price = intent.getStringExtra("price");
        trainId = intent.getStringExtra("trainId");

        setupListeners();
        init();

        observeData();

        binding.btnPay.setOnClickListener(v -> showPaymentBottomSheet());

        return root;
    }

    private void observeData() {
        viewModel.getIsTimeout().observe(getViewLifecycleOwner(), timeout -> {
            if (timeout) {
                ToastUtil.showToast(requireContext(), "订单超时请重新提交");
                // 定时超时后返回上一个也没
                requireActivity().onBackPressed();
            }
        });
    }

    private void init() {
        binding.tvDepartureTime.setText(departureTime);
        binding.tvArrivalTime.setText(arrivalTime);
        binding.tvDepartureStation.setText(departureStation);
        binding.tvArrivalStation.setText(arrivalStation);
        binding.tvTrainNumber.setText(trainNumber);
        binding.tvDuration.setText(String.format("历时%s", durationTime));
        binding.tvPassengerName.setText(realName);
        binding.tvSeatInfo.setText(String.format("%s座 %s车 %s号", level, carriage, trainSeat));
        binding.tvTicketPrice.setText(String.format("￥%s", price));
        binding.tvTotalPrice.setText(String.format("￥%s", price));

        date = PreferencesUtil.getString(requireContext(), "selectDate");

        binding.tvDepartureDate.setText(String.format("发车时间: %s", DateUtils.convertToCN(date)));
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> back());

        binding.btnPay.setOnClickListener(v -> {
            ToastUtil.showToast(requireContext(), "开始支付流程");
        });

        binding.btnCancel.setOnClickListener(v -> {
            viewModel.cancelSeat(seatId);
            ToastUtil.showToast(requireContext(), "取消成功");
            back();
        });
    }

    private void back() { requireActivity().onBackPressed();}

    private void showPaymentBottomSheet() {
        // 创建 BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());

        // 加载布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_pay, null);
        bottomSheetDialog.setContentView(view);

        // 绑定按钮
        LinearLayout btnWeChatPay = view.findViewById(R.id.btn_wechat_pay);
        LinearLayout btnAliPay = view.findViewById(R.id.btn_alipay);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        // 处理支付点击事件
        btnWeChatPay.setOnClickListener(v -> {
            ToastUtil.showToast(requireContext(), "微信支付");
            collectData(1L);
            bottomSheetDialog.dismiss(); // 关闭弹窗
        });

        btnAliPay.setOnClickListener(v -> {
            ToastUtil.showToast(requireContext(), "支付宝支付");
            collectData(2L);
            bottomSheetDialog.dismiss();
        });

        // 取消按钮
        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // 显示弹窗
        bottomSheetDialog.show();
    }

    private void collectData(Long pay) {
        String id = PreferencesUtil.getString(requireContext(), "id");
        String phone = PreferencesUtil.getString(requireContext(), "phone");
        Order order = new Order(
                To.toLong(id),
                To.toLong(trainId),
                trainNumber,
                DateUtils.stringToDate(date),
                departureStation,
                arrivalStation,
                DateUtils.stringToHhMm(departureTime),
                DateUtils.stringToHhMm(arrivalTime),
                Long.valueOf(To.seatToNumber(level)),
                carriage,
                trainSeat,
                realName,
                1L,
                phone,
                Long.valueOf(price),
                pay,
                DateUtils.getNowDateTime(),
                1L
        );
    }
}