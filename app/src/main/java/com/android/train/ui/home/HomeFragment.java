package com.android.train.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.R;
import com.android.train.databinding.FragmentHomeBinding;
import com.android.train.pojo.StationInfo;
import com.android.train.service.StationService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TextView tvDeparture, tvDestination, date;
    private ImageView swap;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // 初始化 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.128.83.81:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StationService stationService = retrofit.create(StationService.class);

        // 使用 Factory 创建 ViewModel
        viewModel = new ViewModelProvider(this, new HomeViewModelFactory(stationService))
                .get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        tvDeparture = binding.tvDeparture;
        tvDestination = binding.tvDestination;
        swap = binding.swap;
        date = binding.date;

        View root = binding.getRoot();

        // 观察 LiveData，并更新 UI
        observeViewModel();

        // 设置 swap 按钮点击事件
        swap.setOnClickListener(v -> viewModel.swapText());

        // 月日选择
        date.setOnClickListener(v -> viewModel.showDatePicker(getContext()));

        return root;
    }

    private void observeViewModel() {
        // 观察出发地
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), departure ->
                tvDeparture.setText(departure));

        // 观察目的地
        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), destination ->
                tvDestination.setText(destination));

        // 观察日期
        viewModel.getFormattedDate().observe(getViewLifecycleOwner(), formattedDate ->
                date.setText(formattedDate));

        // 观察车站列表
        viewModel.getStationList().observe(getViewLifecycleOwner(), stationList -> {
            if (stationList != null && !stationList.isEmpty()) {
                Log.d("HomeFragment", "车站数据已更新，数量: " + stationList.size());
            } else {
                Log.e("HomeFragment", "车站列表为空或加载失败");
            }
        });

        // 观察错误信息
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDeparture = view.findViewById(R.id.tv_departure);
        tvDestination = view.findViewById(R.id.tv_destination);

        // 绑定 ViewModel 数据
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), tvDeparture::setText);
        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), tvDestination::setText);

        viewModel.loadStationList();

        // 点击选择出发地
        tvDeparture.setOnClickListener(v -> {
            List<StationInfo> stationList = viewModel.getStationList().getValue();
        });


        // 点击选择目的地
        tvDestination.setOnClickListener(v -> {
            List<StationInfo> stationList = viewModel.getStationList().getValue();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}