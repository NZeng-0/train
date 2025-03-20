package com.android.train.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.model.DateItem;
import com.android.train.viewmodel.UtilViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private final List<DateItem> dateList;
    private final Context context;

    private final UtilViewModel utilViewModel;

    // 选中索引
    private int selectedPosition = RecyclerView.SCROLLBAR_POSITION_DEFAULT;

    public DateAdapter(Context context, List<DateItem> dateList, UtilViewModel utilViewModel) {
        this.context = context;
        this.dateList = dateList;
        this.utilViewModel = utilViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateItem dateItem = dateList.get(position);
        holder.dayOfWeek.setText(dateItem.getDayOfWeek());
        holder.date.setText(dateItem.getDate());

        int adapterPosition = holder.getAdapterPosition();
        boolean isSelected = (adapterPosition == selectedPosition);

        holder.itemView.setBackgroundResource(isSelected ? R.drawable.item_date_bg : android.R.color.transparent);

        int textColor = ContextCompat.getColor(context,
                isSelected ? android.R.color.holo_blue_dark : android.R.color.white);
        holder.dayOfWeek.setTextColor(textColor);
        holder.date.setTextColor(textColor);

        // 处理选择日期
        holder.itemView.setOnClickListener(v -> {
            int newPosition = holder.getAdapterPosition();
            if (newPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition);
                selectedPosition = newPosition;
                notifyItemChanged(selectedPosition);
                utilViewModel.setSelectedDate(convertToFullDate(dateList.get(selectedPosition).getDate()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public String convertToFullDate(String shortDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM.dd", Locale.CHINA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        try {
            Date date = inputFormat.parse(shortDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // 需要补充年份，假设使用当前年份
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            calendar.set(Calendar.YEAR, currentYear);

            return outputFormat.format(calendar.getTime()); // 转换为 yyyy-MM-dd
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // 解析失败返回 null
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeek, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            date = itemView.findViewById(R.id.tv_date);
        }
    }
}

