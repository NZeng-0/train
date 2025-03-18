package com.android.train.ui.query;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

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
import com.android.train.databinding.FragmentQueryBinding;
import com.android.train.databinding.FragmentStationBinding;
import com.android.train.model.DateItem;
import com.android.train.model.TrainModel;

import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {

    private QueryViewModel viewModel;
    private FragmentQueryBinding binding;

    public static QueryFragment newInstance() {
        return new QueryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        // TODO: Use the ViewModel
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

        initDate();

        setupTrainList();

        return root;
    }

    private void initDate() {
        RecyclerView dateRecyclerView = binding.dateRecyclerView;
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        List<DateItem> dateList = viewModel.generateDateList();
        DateAdapter dateAdapter = new DateAdapter(requireContext(), dateList);
        dateRecyclerView.setAdapter(dateAdapter);
    }

    private void setupTrainList() {
        RecyclerView trainListView = binding.trainList;
        List<TrainModel> trainList = new ArrayList<>();

        // Add sample data
        trainList.add(new TrainModel("16:22", "G1223", "17:37", "南京南", "杭州东", "1小时15分", "候补", "候补", "候补", false));
        trainList.add(new TrainModel("16:28", "G1677", "18:09", "南京南", "杭州西", "1小时41分", "候补", "候补", "5张", true));
        trainList.add(new TrainModel("16:34", "G1441", "17:59", "南京南", "杭州西", "1小时25分", "有票", "1张", "候补", false));
        trainList.add(new TrainModel("16:44", "G7615", "18:12", "南京南", "杭州东", "1小时28分", "有票", "8张", "7张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));
        trainList.add(new TrainModel("16:52", "G7677", "18:22", "南京南", "杭州东", "1小时30分", "有票", "10张", "4张", false));

        // Setup RecyclerView
        TrainAdapter trainAdapter = new TrainAdapter(requireContext(), trainList);
        trainListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        trainListView.setAdapter(trainAdapter);
    }

}