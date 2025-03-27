package com.android.train.ui.account;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.train.R;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.UserService;
import com.android.train.databinding.FragmentAccountBinding;
import com.android.train.repository.AuthRepository;
import com.android.train.ui.login.LoginViewModel;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.ToastUtil;
import com.android.train.viewmodel.AuthViewModel;
import com.android.train.viewmodel.AuthViewModelFactory;

import retrofit2.Retrofit;

public class AccountFragment extends Fragment {

    private AccountViewModel viewModel;
    private AuthViewModel authViewModel;
    private FragmentAccountBinding binding;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        Retrofit retrofit = RetrofitClient.getClient(requireContext());

        UserService userService = retrofit.create(UserService.class);
        // 创建 Repository
        AuthRepository authRepository = new AuthRepository(userService);
        // 通过 Factory 创建 ViewModel
        AuthViewModelFactory factory = new AuthViewModelFactory(authRepository);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(16, systemBarsInsets.top, 16, 0);
            return insets;
        });

        binding.logoutButton.setOnClickListener( v -> onLogout());

        return root;
    }
    private void onLogout(){
        authViewModel.logout();
        PreferencesUtil.removePreferenceByKey(requireContext(),"token");
        PreferencesUtil.removePreferenceByKey(requireContext(),"username");
        PreferencesUtil.removePreferenceByKey(requireContext(),"realName");
        PreferencesUtil.removePreferenceByKey(requireContext(),"id");
        PreferencesUtil.removePreferenceByKey(requireContext(),"phone");
        PreferencesUtil.removePreferenceByKey(requireContext(),"email");
        PreferencesUtil.removePreferenceByKey(requireContext(),"isLogin");
        PreferencesUtil.removePreferenceByKey(requireContext(),"idCard");
        ToastUtil.showToast(requireContext(), "退出成功");

        requireActivity().onBackPressed();
    }
}