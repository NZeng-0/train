package com.android.train.ui.query;

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
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.android.train.MainActivity;
import com.android.train.R;
import com.android.train.adapter.DateAdapter;
import com.android.train.adapter.TrainAdapter;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.databinding.FragmentQueryBinding;
import com.android.train.model.DateItem;
import com.android.train.model.TrainModel;
import com.android.train.utils.PreferencesUtil;
import com.android.train.viewmodel.UtilViewModel;

import java.util.List;

import retrofit2.Retrofit;

public class QueryFragment extends Fragment {

    private QueryViewModel viewModel;
    private FragmentQueryBinding binding;
    private TrainAdapter trainAdapter;
    private ImageView noDataImage;
    private ViewSwitcher viewSwitcher;
    private RecyclerView trainListView;
    private UtilViewModel utilViewModel;
    private String saleTime;
    private int page = 1;
    private int size = 20;
    private String start, end;

    public static QueryFragment newInstance() {
        return new QueryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Retrofit
        Retrofit retrofit = RetrofitClient.getClient(requireContext());
        RelationService relationService = retrofit.create(RelationService.class);
        QueryViewModelFactory factory = new QueryViewModelFactory(requireContext(), relationService);
        viewModel = new ViewModelProvider(this, factory).get(QueryViewModel.class);
        utilViewModel = new ViewModelProvider(this).get(UtilViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQueryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        // 返回
        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra("nav", 1);
            requireActivity().startActivity(intent);
        });

        noDataImage = binding.noDataImage;
        viewSwitcher = binding.viewSwitcher;
        trainListView = binding.trainList;

        String saveStart = PreferencesUtil.getString(requireContext(), "departureCity");
        String saveEnd = PreferencesUtil.getString(requireContext(), "destinationCity");
        start = saveStart;
        end = saveEnd;
        if (saveStart == null) start = "北京";
        if (saveEnd == null) end = "上海";

        initDate();

        Intent intent = requireActivity().getIntent();
        saleTime = intent.getStringExtra("saleTime");

        viewModel.loadStationList(page, size, start, end, saleTime);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.loadStationList(page, size, start, end, saleTime);
            binding.swipeRefresh.setRefreshing(false);
        });

        observe();

        return root;
    }

    private void observe() {

        viewModel.getTrainModels().observe(getViewLifecycleOwner(), models -> {
            // 获取当前显示的 View
            View currentView = viewSwitcher.getCurrentView();

            if (models.isEmpty()) {
                // 当前显示的不是 "无数据" 图片时，切换到 "无数据"
                if (currentView.getId() != R.id.no_data_image) {
                    viewSwitcher.setDisplayedChild(1); // 切换到 noDataImage
                }
            } else {
                // 当前显示的不是 "列表" 时，切换到 "列表"
                if (currentView.getId() != R.id.swipe_refresh) {
                    viewSwitcher.setDisplayedChild(0); // 切换到 RecyclerView
                }

                // 更新 RecyclerView 数据
                if (trainAdapter != null) {
                    trainAdapter.updateData(models);
                } else {
                    setupTrainList(models);
                }
            }
        });


        utilViewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            saleTime = date;
            viewModel.loadStationList(page, size, start, end, date);
        });

        utilViewModel.getDeparture().observe(getViewLifecycleOwner(), city -> {
            start = city;
            viewModel.loadStationList(page, size, city, end, saleTime);
        });

        utilViewModel.getDestination().observe(getViewLifecycleOwner(), city -> {
            end = city;
            viewModel.loadStationList(page, size, start, city, saleTime);
        });
    }

    private void initDate() {
        RecyclerView dateRecyclerView = binding.dateRecyclerView;
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<DateItem> dateList = viewModel.generateDateList();
        DateAdapter dateAdapter = new DateAdapter(requireContext(), dateList, utilViewModel);
        dateRecyclerView.setAdapter(dateAdapter);
    }

    private void setupTrainList(List<TrainModel> trainModels) {
        // Create adapter with the received models
        trainAdapter = new TrainAdapter(requireContext(), trainModels);

        // Setup RecyclerView
        trainListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        trainListView.setAdapter(trainAdapter);
    }
}
