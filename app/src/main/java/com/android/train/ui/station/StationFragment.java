package com.android.train.ui.station;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.train.R;
import com.android.train.adapter.StationAdapter;
import com.android.train.api.RetrofitClient;
import com.android.train.databinding.FragmentStationBinding;
import com.android.train.api.service.StationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private StationViewModel viewModel;
    private FragmentStationBinding binding;
    private RecyclerView recyclerViewStations;
    private TextView tvCurrentLocation, tvRefreshLocation;
    private StationAdapter stationAdapter;
    private FusedLocationProviderClient fusedLocationClient;

    public static StationFragment newInstance() {
        return new StationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Retrofit
        Retrofit retrofit = RetrofitClient.getClient(requireContext());

        StationService stationService = retrofit.create(StationService.class);

        // 使用 ViewModelFactory
        StationViewModelFactory factory = new StationViewModelFactory(stationService);
        viewModel = new ViewModelProvider(this, factory).get(StationViewModel.class);

        // 修改状态栏颜色
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentStationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        stationAdapter = new StationAdapter(new ArrayList<>(), station -> {
            // 选择站点后返回上一个 Fragment
            ((StationActivity) requireActivity()).returnSelectedStation(station);
        });

        initViews();
        viewModel.loadStationList();
        loadData();

        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        // 初始化定位客户端
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // 申请权限并获取定位
        requestLocationPermission();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    viewModel.search(query); // 触发 ViewModel 进行搜索
                }

                hideKeyboard(binding.searchView); // 隐藏键盘
                binding.searchView.clearFocus(); // 清除焦点，避免二次触发
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)) {
                    viewModel.loadStationList();
                }
                return false;
            }
        });

        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.searchView.setIconifiedByDefault(false);

//        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                viewModel.loadStationList();
//            }
//        });

        return root;
    }

    private void initViews() {
        tvCurrentLocation = binding.tvCurrentLocation;
        tvRefreshLocation = binding.tvRefreshLocation;

        recyclerViewStations = binding.recyclerViewStations;
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewStations.setAdapter(stationAdapter);

        // 绑定点击刷新位置事件
        tvRefreshLocation.setOnClickListener(v -> {
            Log.e("addr", "点击了");
            getCurrentLocation();
        });
        // 点击当前定位
//        tvCurrentLocation.setOnClickListener(v ->
//                ((StationActivity) requireActivity()).returnSelectedStation(
//                        (String) tvCurrentLocation.getText())
//        );
    }

    private void loadData() {
        viewModel.getStationList().observe(getViewLifecycleOwner(), stations -> {
            stationAdapter.updateData(stations);
        });
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 申请定位权限
     */
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    /**
     * 获取当前位置
     */
    private void getCurrentLocation() {
        Log.e("addr", "进入了");
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("addr", "没有定位权限！");
            return;
        }
        Log.e("addr", "定位中");
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = getAddress(latitude, longitude);
                Log.e("addr", address);
                tvCurrentLocation.setText(address);
            } else {
                tvCurrentLocation.setText("无法获取位置信息");
            }
        });
    }

    /**
     * 根据经纬度获取地理位置信息
     */
    private String getAddress(double latitude, double longitude) {
        String cityName = "未知位置";
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                cityName = address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(requireContext(), "需要位置权限以获取当前位置", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
