package com.android.train.ui.query;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.train.R;
import com.android.train.adapter.DateAdapter;
import com.android.train.adapter.TrainAdapter;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.api.service.StationService;
import com.android.train.api.service.UserService;
import com.android.train.databinding.FragmentQueryBinding;
import com.android.train.databinding.FragmentStationBinding;
import com.android.train.model.DateItem;
import com.android.train.model.TrainModel;
import com.android.train.pojo.StationInfo;
import com.android.train.ui.station.StationViewModel;
import com.android.train.ui.station.StationViewModelFactory;
import com.android.train.utils.PreferencesUtil;
import com.android.train.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class QueryFragment extends Fragment {

    private QueryViewModel viewModel;
    private FragmentQueryBinding binding;
    private TrainAdapter trainAdapter;

    public static QueryFragment newInstance() {
        return new QueryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Retrofit
        Retrofit retrofit = RetrofitClient.getClient(requireContext());
        RelationService relationService = retrofit.create(RelationService.class);
        QueryViewModelFactory factory = new QueryViewModelFactory(relationService);
        viewModel = new ViewModelProvider(this, factory).get(QueryViewModel.class);
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
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        initDate();

        Intent intent = requireActivity().getIntent();
        String departure = intent.getStringExtra("departure");
        String destination = intent.getStringExtra("destination");
        String saleTime = intent.getStringExtra("saleTime");
        int page = 1; // 页码，默认为 1
        int size = 20;

        viewModel.loadStationList(page, size, departure, destination, saleTime);

        observe();

        return root;
    }

    private void observe(){
        viewModel.getTrainModels().observe(getViewLifecycleOwner(), models -> {
            if (trainAdapter != null) {
                trainAdapter.updateData(models);
            } else {
                setupTrainList(models);
            }
        });
    }

    private void initDate() {
        RecyclerView dateRecyclerView = binding.dateRecyclerView;
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<DateItem> dateList = viewModel.generateDateList();
        DateAdapter dateAdapter = new DateAdapter(requireContext(), dateList);
        dateRecyclerView.setAdapter(dateAdapter);
    }


    private void setupTrainList(List<TrainModel> trainModels) {
        RecyclerView trainListView = binding.trainList;

        // Create adapter with the received models
        trainAdapter = new TrainAdapter(requireContext(), trainModels);

        // Setup RecyclerView
        trainListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        trainListView.setAdapter(trainAdapter);
    }
}