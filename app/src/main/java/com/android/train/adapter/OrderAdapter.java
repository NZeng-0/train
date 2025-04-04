package com.android.train.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.pojo.Order;
import com.android.train.utils.To;

import java.util.List;
import java.util.function.Consumer;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    private Consumer<String> onCancelClick;

    public OrderAdapter(List<Order> orderList, Consumer<String> onCancelClick) {
        this.orderList = orderList;
        this.onCancelClick = onCancelClick;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderNumber.setText(order.getOrderSn());
        holder.tvDepartureTime.setText(order.getDepartureTime());
        holder.tvDepartureStation.setText(order.getDeparture());
        holder.tvArrivalTime.setText(order.getArrivalTime());
        holder.tvArrivalStation.setText(order.getArrival());
        holder.tvTrainNumber.setText(order.getTrainNumber());
        holder.tvDate.setText(order.getRidingDate());
        holder.tvState.setText(getState(order.getStatus()));
        holder.tvPayType.setText(getPayType(order.getPayType()));
        holder.tvSeat.setText(String.format("%s座 %s车 %s号", To.numberToSeat(order.getSeatType()), order.getCarriageNumber(), order.getSeatNumber()));
        holder.tvPrice.setText(String.format("￥%s", order.getAmount().toString()));

        if (order.getStatus().intValue() != 1) {
            holder.btLine.setVisibility(View.GONE);
            holder.llOption.setVisibility(View.GONE);
            holder.tvType.setTextColor(holder.tvType.getResources().getColor(R.color.font_label));
            holder.tvType.setBackgroundResource(R.drawable.tv_type_border);
            holder.tvPrice.setTextColor(holder.tvPrice.getResources().getColor(R.color.font_label));
        } else {
            holder.cancel.setOnClickListener(v -> {
                if (onCancelClick != null) {
                    onCancelClick.accept(order.getId());
                }
            });
        }
    }

    private String getState(Long state) {
        if (state.intValue() == 1) {
            return "已支付";
        } else if (state.intValue() == 2) {
            return "已退票";
        }
        return "已完成";
    }

    private String getPayType(Long state) {
        if (state.intValue() == 1) {
            return "微信支付";
        }
        return "支付宝支付";
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvDepartureTime, tvDepartureStation, tvArrivalTime, tvArrivalStation;
        TextView tvTrainNumber, tvDate, tvState, tvType, tvPayType, tvSeat, tvPrice, cancel;
        View btLine;
        LinearLayout llOption;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvDepartureTime = itemView.findViewById(R.id.tv_departure_time);
            tvDepartureStation = itemView.findViewById(R.id.tv_departure_station);
            tvArrivalTime = itemView.findViewById(R.id.tv_arrival_time);
            tvArrivalStation = itemView.findViewById(R.id.tv_arrival_station);
            tvTrainNumber = itemView.findViewById(R.id.tv_train_number);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvState = itemView.findViewById(R.id.tv_state);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPayType = itemView.findViewById(R.id.tv_pay_type);
            tvSeat = itemView.findViewById(R.id.tv_seat);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btLine = itemView.findViewById(R.id.bt_line);
            llOption = itemView.findViewById(R.id.llOption);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}

