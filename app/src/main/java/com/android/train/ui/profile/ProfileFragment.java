package com.android.train.ui.profile;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.train.MainActivity;
import com.android.train.R;
import com.android.train.databinding.FragmentProfileBinding;
import com.android.train.ui.account.AccountActivity;
import com.android.train.ui.login.LoginActivity;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.To;

import eightbitlab.com.blurview.BlurView;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ImageView avatar;
    private TextView username, description;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter(root);

        initData();

//        binding.all.setOnClickListener(v -> {
//           Intent intent = new Intent(requireActivity(), MainActivity.class);
//           intent.putExtra("nav", 2);
//           requireActivity().startActivity(intent);
//        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        avatar = binding.profileImage;
        username = binding.tvUsername;
        description = binding.tvDescription;

        username.setText(PreferencesUtil.getString(requireContext(), "username", "未登录"));
        description.setText(PreferencesUtil.getString(requireContext(), "phone", "便捷出行就在12306"));

        goFragment();
    }

    private void adapter(View root) {
        // 修改状态栏颜色，确保它不是透明的
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));

        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // 将 20dp 转换为 px
            int paddingHorizontalPx = To.dpToPx(requireContext(), 20);

            // 让 header_layout 适配状态栏，并使用 dp 计算左右边距
            binding.headerLayout.setPadding(paddingHorizontalPx, systemBarsInsets.top, paddingHorizontalPx, 0);

            return insets;
        });

        // 动态模糊
        float radius = 20f;

        View decorView = window.getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        Drawable windowBackground = decorView.getBackground();
        BlurView blurView = binding.blurView;
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true);
    }

    private void goFragment() {
        boolean isLogin = PreferencesUtil.getBoolean(requireContext(), "isLogin", false);

        if (isLogin) {
            avatar.setOnClickListener(v -> toAccount());
            username.setOnClickListener(v -> toAccount());
            description.setOnClickListener(v -> toAccount());
        } else {
            avatar.setOnClickListener(v -> toLogin());
            username.setOnClickListener(v -> toLogin());
            description.setOnClickListener(v -> toLogin());
        }
    }

    private void toLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void toAccount() {
        Intent intent = new Intent(requireContext(), AccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}