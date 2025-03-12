package com.android.train.ui.profile;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
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

import com.android.train.R;
import com.android.train.databinding.FragmentHomeBinding;
import com.android.train.databinding.FragmentProfileBinding;
import com.android.train.utils.BlurUtils;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ProfileFragment extends Fragment {

    private LinearLayout header_layout;
    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

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

        float radius = 20f;

        View decorView = window.getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        Drawable windowBackground = decorView.getBackground();
        BlurView blurView = binding.blurView;
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true);
        initServiceList();
        return root;
    }

    private void initServiceList(){
        GridLayout gridLayout = binding.serviceList;
        gridLayout.setColumnCount(4); // 设置列数

        // 定义图标和文本
        int[] icons = {R.drawable.order, R.drawable.payment, R.drawable.travel, R.drawable.peopel,
                R.drawable.order, R.drawable.payment, R.drawable.travel, R.drawable.peopel};

        String[] labels = {
                "全部订单", "待付款", "未出行", "乘车人",
                "全部订单", "待付款", "未出行", "乘车人"
        };

        int itemCount = icons.length; // 项目总数

        for (int i = 0; i < itemCount; i++) {
            // 创建 LinearLayout
            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setGravity(Gravity.CENTER);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(dpToPx(requireContext(),5), dpToPx(requireContext(),5), dpToPx(requireContext(),5), dpToPx(requireContext(),5));
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // 设置权重填充
            itemLayout.setLayoutParams(params);

            // 创建 ImageView
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(requireContext(),32), dpToPx(requireContext(),32)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(icons[i]);

            // 创建 TextView
            TextView textView = new TextView(getContext());
            textView.setText(labels[i]);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.font_label));
            textView.setGravity(Gravity.CENTER);

            // 添加子视图到 LinearLayout
            itemLayout.addView(imageView);
            itemLayout.addView(textView);

            // 添加 LinearLayout 到 GridLayout
            gridLayout.addView(itemLayout);
        }

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