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
import com.android.train.utils.DateUtils;
import com.android.train.utils.PreferencesUtil;
import com.android.train.viewmodel.UtilViewModel;

import java.util.List;

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
                String date = DateUtils.convertToFullDate(dateList.get(selectedPosition).getDate());
                utilViewModel.setSelectedDate(date);
                PreferencesUtil.putString(context,"selectDate", date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
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

