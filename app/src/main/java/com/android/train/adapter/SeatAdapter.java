package com.android.train.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.model.SeatOption;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<SeatOption> seatList;
    private int selectedPosition = RecyclerView.SCROLLBAR_POSITION_DEFAULT;

    public interface OnSeatClickListener {
        void onSeatSelected(SeatOption seatOption);
    }

    private final OnSeatClickListener listener;

    public SeatAdapter(List<SeatOption> seatList, OnSeatClickListener listener) {
        this.seatList = seatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat_option, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatOption seat = seatList.get(position);
        holder.tvSeatType.setText(seat.getSeatType());
        holder.tvPrice.setText("¥" + seat.getPrice());
        holder.tvHave.setText(seat.isAvailable() ? "有": "无");

        // 设置可选状态
        if (seat.isAvailable()) {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setOnClickListener(v -> {
                int newPosition = holder.getAdapterPosition();
                if (newPosition == RecyclerView.NO_POSITION) return;

                selectedPosition = newPosition;
                notifyDataSetChanged();
                listener.onSeatSelected(seatList.get(newPosition));
            });
        } else {
            holder.itemView.setAlpha(0.5f);
            holder.itemView.setClickable(false);
        }

        // 选中高亮
        holder.itemView.setBackgroundResource(selectedPosition == holder.getAdapterPosition() ?
                R.drawable.bg_selected : R.drawable.bg_unselected);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeatType, tvPrice, tvHave;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeatType = itemView.findViewById(R.id.tv_seat_type);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvHave = itemView.findViewById(R.id.tv_hava);
        }
    }
}

