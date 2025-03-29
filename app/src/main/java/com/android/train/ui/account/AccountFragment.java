package com.android.train.ui.account;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        AuthRepository authRepository = new AuthRepository(requireContext(), userService);
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
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.tvUsername.setText(PreferencesUtil.getString(requireContext(),"username"));
        String phone = PreferencesUtil.getString(requireContext(), "phone");
        if (phone != null && phone.length() >= 7) {
            String maskedPhone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
            binding.tvPhone.setText(maskedPhone);
        } else {
            binding.tvPhone.setText(phone); // 处理异常情况，防止空值或短号码
        }

        String email = PreferencesUtil.getString(requireContext(), "email");
        if (email != null && email.contains("@")) {
            int atIndex = email.indexOf("@");
            if (atIndex > 4) { // 确保有足够的字符隐藏
                String maskedEmail = email.substring(0, 2) + "****" + email.substring(atIndex - 2);
                binding.tvEmail.setText(maskedEmail);
            } else {
                binding.tvEmail.setText(email); // 长度不够，直接显示
            }
        } else {
            binding.tvEmail.setText(email); // 处理空值或格式错误
        }

        binding.resetPwd.setOnClickListener(v -> showResetPasswordDialog());

        return root;
    }
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("修改密码");

        // 创建布局
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        // 旧密码输入框
        EditText oldPwdInput = new EditText(requireContext());
        oldPwdInput.setHint("请输入旧密码");
        oldPwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(oldPwdInput);

        // 新密码输入框
        EditText newPwdInput = new EditText(requireContext());
        newPwdInput.setHint("请输入新密码");
        newPwdInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(newPwdInput);

        builder.setView(layout);

        // 设置按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            String oldPwd = oldPwdInput.getText().toString().trim();
            String newPwd = newPwdInput.getText().toString().trim();

            if (oldPwd.isEmpty() || newPwd.isEmpty()) {
                ToastUtil.showToast(requireContext(), "密码不能为空");
                return;
            }
            // 调用接口修改密码
            authViewModel.changePassword(PreferencesUtil.getString(requireContext(),"id"), oldPwd, newPwd);
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        builder.show();
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