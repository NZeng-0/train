package com.android.train.ui.profile;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.train.R;
import com.android.train.databinding.FragmentHomeBinding;
import com.android.train.databinding.FragmentProfileBinding;
import com.android.train.utils.BlurUtils;

public class ProfileFragment extends Fragment {

    private LinearLayout header_layout;
    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        header_layout = binding.headerLayout;

        // 修改状态栏颜色，确保它不是透明的
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));

        // 适配刘海屏 & 状态栏安全区域
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // 将 20dp 转换为 px
            int paddingHorizontalPx = dpToPx(requireContext(), 20);

            // 让 header_layout 适配状态栏，并使用 dp 计算左右边距
            binding.headerLayout.setPadding(paddingHorizontalPx, systemBarsInsets.top, paddingHorizontalPx, 0);

            return insets;
        });

        return root;
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}