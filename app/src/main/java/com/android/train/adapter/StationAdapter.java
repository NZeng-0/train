package com.android.train.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.pojo.StationInfo;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {
    private List<StationInfo> stationList;
    private OnItemClickListener onItemClickListener;

    // 构造函数
    public StationAdapter(List<StationInfo> stationList, OnItemClickListener onItemClickListener) {
        this.stationList = stationList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        StationInfo station = stationList.get(position);
        holder.tvStationName.setText(station.getName());
        holder.tvRegionName.setText(station.getRegionName());

        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(station);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList != null ? stationList.size() : 0;
    }

    // 更新数据
    public void updateData(List<StationInfo> newStationList) {
        this.stationList = newStationList;
        notifyDataSetChanged();
    }

    static class StationViewHolder extends RecyclerView.ViewHolder {
        TextView tvStationName, tvRegionName;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationName = itemView.findViewById(R.id.tvStationName);
            tvRegionName = itemView.findViewById(R.id.tvRegionName);
        }
    }

    // 定义点击事件接口
    public interface OnItemClickListener {
        void onItemClick(StationInfo station);
    }
}

