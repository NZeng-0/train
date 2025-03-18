package com.android.train.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.R;
import com.android.train.databinding.FragmentHomeBinding;
import com.android.train.ui.query.QueryActivity;
import com.android.train.ui.station.StationActivity;
import com.android.train.utils.PreferenceUtils;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TextView tvDeparture, tvDestination, date;
    private ImageView swap;
    private static final int REQUEST_CODE_STATION = 1001;
    private ActivityResultLauncher<Intent> stationResultLauncher;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        tvDeparture = binding.tvDeparture;
        tvDestination = binding.tvDestination;
        swap = binding.swap;
        date = binding.date;

        View root = binding.getRoot();
        // 读取已保存的站点信息
        String savedDeparture = PreferenceUtils.getFromPreferences(requireContext(), "departure");
        String savedDestination = PreferenceUtils.getFromPreferences(requireContext(), "destination");

        if (savedDeparture == null) {
            savedDeparture = "北京";
        }

        if (savedDestination == null) {
            savedDestination = "上海";
        }

        // 设置到 ViewModel，触发 UI 更新
        viewModel.setDepartureCity(savedDeparture);
        viewModel.setDestinationCity(savedDestination);

        // 观察 LiveData，并更新 UI
        observeViewModel();
        // 设置 swap 按钮点击事件
        swap.setOnClickListener(v -> viewModel.swapText());
        // 月日选择
        date.setOnClickListener(v -> viewModel.showDatePicker(getContext()));
        // 选择出发地
        tvDeparture.setOnClickListener(v -> openStationFragment("departure"));
        // 选择目的地
        tvDestination.setOnClickListener(v -> openStationFragment("destination"));

        stationResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handelResult
        );

        // 查询车次
        binding.btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QueryActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void observeViewModel() {
        // 观察出发点
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), city -> {
            tvDeparture.setText(city);
            PreferenceUtils.saveToPreferences(requireContext(), "departure", city);
        });

        // 观察到达地
        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), city -> {
            tvDestination.setText(city);
            PreferenceUtils.saveToPreferences(requireContext(), "destination", city);
        });

        // 观察日期
        viewModel.getFormattedDate().observe(getViewLifecycleOwner(), formattedDate ->
                date.setText(formattedDate));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDeparture = view.findViewById(R.id.tv_departure);
        tvDestination = view.findViewById(R.id.tv_destination);

        // 绑定 ViewModel 数据
        viewModel.getDepartureCity().observe(getViewLifecycleOwner(), tvDeparture::setText);
        viewModel.getDestinationCity().observe(getViewLifecycleOwner(), tvDestination::setText);
    }

    private void openStationFragment(String type) {
        Intent intent = new Intent(getContext(), StationActivity.class);
        intent.putExtra("type", type);
        stationResultLauncher.launch(intent);
    }

    private void handelResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                String stationName = data.getStringExtra("selected_station");
                String type = data.getStringExtra("type");
                // 更新数据，然后驱动ui更新
                if ("departure".equals(type)) {
                    viewModel.setDepartureCity(stationName);
                } else if ("destination".equals(type)) {
                    viewModel.setDestinationCity(stationName);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}