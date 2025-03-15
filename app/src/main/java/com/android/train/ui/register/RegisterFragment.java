package com.android.train.ui.register;


import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.train.api.RetrofitClient;
import com.android.train.api.service.StationService;
import com.android.train.api.service.UserService;
import com.android.train.databinding.FragmentRegisterBinding;
import com.android.train.model.UserRequest;
import com.android.train.ui.station.StationViewModel;
import com.android.train.ui.station.StationViewModelFactory;
import com.android.train.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {

    private RegisterViewModel viewModel;

    private FragmentRegisterBinding binding;

    private Integer type = 1;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Use the ViewModel
        Retrofit retrofit = RetrofitClient.getClient();

        UserService stationService = retrofit.create(UserService.class);

        // 使用 ViewModelFactory
        RegisterViewModelFactory factory = new RegisterViewModelFactory(stationService);
        viewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        initSpinner();

        // 观察注册结果
        viewModel.registerResult.observe(getViewLifecycleOwner(), isSuccess -> ToastUtil
                .showToast(requireActivity(), viewModel.getMsg()));

        binding.btnNext.setOnClickListener(v -> collectFormData());

        return root;
    }

    private void initSpinner() {
        Spinner spinnerCertType = binding.spinnerCertType;
        spinnerCertType.setBackground(null);
        Map<String, Integer> certTypeMap = new HashMap<>();
        certTypeMap.put("中国居民身份证", 1);

        // 定义显示的选项
        String[] certTypes = {"中国居民身份证"};
        // 设置数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, certTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCertType.setAdapter(adapter);

        // 设置选择监听器
        spinnerCertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCertType = parent.getItemAtPosition(position).toString();
                type = certTypeMap.get(selectedCertType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选择任何项时的处理
            }
        });
    }

    private void collectFormData() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String realName = binding.etRealValue.getText().toString().trim();
        String cardNumber = binding.etCardValue.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        if (!validateForm()) return;
        UserRequest userRequest = viewModel.collectFormData(
                username,
                password,
                type,
                realName,
                cardNumber,
                phone,
                email
        );
        viewModel.register(userRequest);
    }

    private boolean validateForm() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        String realName = binding.etRealValue.getText().toString().trim();
        String cardNumber = binding.etCardValue.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        boolean isAgreed = binding.cbAgree.isChecked();

        if (!viewModel.validateUsername(username)) {
            ToastUtil.showToast(requireActivity(), "用户名必须为6-30位");
            return false;
        }

        if (!viewModel.validatePassword(password)) {
            ToastUtil.showToast(requireActivity(), "密码必须为6-30位");
            return false;
        }

        if (!viewModel.validateConfirmPassword(password, confirmPassword)) {
            ToastUtil.showToast(requireActivity(), "两次输入的密码不一致");
            return false;
        }

        if (!viewModel.validateRealName(realName)) {
            ToastUtil.showToast(requireActivity(), "请输入真实姓名");
            return false;
        }

        if (!viewModel.validateCardNumber(cardNumber)) {
            ToastUtil.showToast(requireActivity(), "请输入证件号码");
            return false;
        }

        if (!viewModel.validatePhone(phone)) {
            ToastUtil.showToast(requireActivity(), "请输入正确的手机号码");
            return false;
        }

        if (!viewModel.validateEmail(email)) {
            ToastUtil.showToast(requireActivity(), "请输入正确的电子邮箱");
            return false;
        }

        if (!viewModel.validateAgreement(isAgreed)) {
            ToastUtil.showToast(requireActivity(), "请阅读并同意服务条款和隐私权政策");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
