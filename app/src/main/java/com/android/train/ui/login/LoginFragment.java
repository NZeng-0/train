package com.android.train.ui.login;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.train.R;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.UserService;
import com.android.train.databinding.FragmentLoginBinding;
import com.android.train.model.UserRequest;
import com.android.train.repository.AuthRepository;
import com.android.train.ui.profile.ProfileFragment;
import com.android.train.ui.register.RegisterActivity;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.ToastUtil;
import com.android.train.viewmodel.AuthViewModel;
import com.android.train.viewmodel.AuthViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;
    private AuthViewModel authViewModel;

    private FragmentLoginBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = RetrofitClient.getClient(requireContext());

        UserService userService = retrofit.create(UserService.class);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // 创建 Repository
        AuthRepository authRepository = new AuthRepository(userService);

        // 通过 Factory 创建 ViewModel
        AuthViewModelFactory factory = new AuthViewModelFactory(authRepository);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        // 返回按钮
        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // 注册
        TextView register = binding.toRegister;
        register.setOnClickListener(v -> goRegister());

        // 登录
        binding.btnLogin.setOnClickListener(v -> collectFormData());

        observeData();

        return root;
    }

    private void observeData() {
        // 监听消息
        authViewModel.getMessage().observe(getViewLifecycleOwner(), msg ->
                ToastUtil.showToast(requireContext(), msg));

        // 监听 token
        authViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            if (token != null) {
                // 这里可以存储 Token
                boolean b = PreferencesUtil.putString(requireContext(), "token", token);
            }
        });
        authViewModel.getNavigateLiveData().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_view);
                navController.navigate(R.id.navigation_profile);
            }
        });
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                PreferencesUtil.putString(requireContext(),"username", user.getUsername());
                PreferencesUtil.putString(requireContext(),"id", user.getId());
                PreferencesUtil.putString(requireContext(),"phone", user.getPhone());
                PreferencesUtil.putString(requireContext(),"email", user.getMail());
                PreferencesUtil.putBoolean(requireContext(),"isLogin", true);
            }
        });
    }

    private void collectFormData() {
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        if (!validateForm()) return;
        UserRequest userRequest = viewModel.collectFormData(username, password);
        authViewModel.login(userRequest);
    }

    private boolean validateForm() {
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();


        if (!viewModel.validateUsername(username)) {
            ToastUtil.showToast(requireActivity(), "账号不能为空");
            return false;
        }

        if (!viewModel.validatePassword(password)) {
            ToastUtil.showToast(requireActivity(), "密码不能为空");
            return false;
        }

        return true;
    }

    public void goRegister() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}