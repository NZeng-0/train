package com.android.train.ui.login;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.train.databinding.FragmentLoginBinding;
import com.android.train.ui.register.RegisterActivity;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

    private FragmentLoginBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
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


        return root;
    }

    public void goRegister(){
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}