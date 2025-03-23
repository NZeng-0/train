package com.android.train.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.model.TrainModel;
import com.android.train.ui.booking.BookingActivity;

import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {

    private final Context context;
    private final List<TrainModel> trainList;

    public TrainAdapter(Context context, List<TrainModel> trainList) {
        this.context = context;
        this.trainList = trainList;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_train, parent, false);
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        TrainModel train = trainList.get(position);

        // Set train times and number
        holder.departureTimeTextView.setText(train.getDepartureTime());
        holder.trainNumberTextView.setText(train.getTrainNumber());
        holder.arrivalTimeTextView.setText(train.getArrivalTime());

        // Set station info
        holder.departureStationTextView.setText(train.getDepartureStation());
        holder.durationTextView.setText(train.getDuration());
        holder.arrivalStationTextView.setText(train.getArrivalStation());

        // Set ticket status
        setTicketStatus(holder.secondClassTextView, train.getSecondClassStatus());
        setTicketStatus(holder.firstClassTextView, train.getFirstClassStatus());
        setTicketStatus(holder.businessClassTextView, train.getBusinessClass());

//        if (train.getTrainNumber().contains("G")) {
            holder.trainNumberTextView.setText(train.getTrainNumber());
//        }

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("trainNumber", train.getTrainNumber());
            intent.putExtra("departureStation", train.getDepartureStation());
            intent.putExtra("arrivalStation", train.getArrivalStation());
            intent.putExtra("departureTime", train.getDepartureTime());
            intent.putExtra("arrivalTime", train.getArrivalTime());
            intent.putExtra("durationTime", train.getDuration());
            int[] pricesArray = new int[]{
                    train.getSecondPrice(),
                    train.getFirstPrice(),
                    train.getBusinessPrice(),
            };
            intent.putExtra("prices", pricesArray);

            context.startActivity(intent);
        });
    }

    public void updateData(List<TrainModel> newData) {
        this.trainList.clear();
        this.trainList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    private void setTicketStatus(TextView textView, String status) {
        textView.setText(status);

        // Set different colors based on ticket status
        if (status.equals("有票")) {
            textView.setTextColor(Color.parseColor("#4CAF50"));
        } else if (status.contains("张")) {
            textView.setTextColor(Color.parseColor("#4CAF50"));
        } else if (status.equals("候补")) {
            textView.setTextColor(Color.parseColor("#3399FF"));
        } else if (status.equals("售罄")) {
            textView.setTextColor(Color.parseColor("#999999"));
        }
    }

    // ViewHolder class
    static class TrainViewHolder extends RecyclerView.ViewHolder {
        TextView departureTimeTextView;
        TextView trainNumberTextView;
        TextView arrivalTimeTextView;
        TextView departureStationTextView;
        TextView durationTextView;
        TextView arrivalStationTextView;
        TextView secondClassTextView;
        TextView firstClassTextView;
        TextView businessClassTextView;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            departureTimeTextView = itemView.findViewById(R.id.tv_departure_time);
            trainNumberTextView = itemView.findViewById(R.id.tv_train_number);
            arrivalTimeTextView = itemView.findViewById(R.id.tv_arrival_time);
            departureStationTextView = itemView.findViewById(R.id.tv_departure_station);
            durationTextView = itemView.findViewById(R.id.tv_duration);
            arrivalStationTextView = itemView.findViewById(R.id.tv_arrival_station);
            secondClassTextView = itemView.findViewById(R.id.tv_second_class);
            firstClassTextView = itemView.findViewById(R.id.tv_first_class);
            businessClassTextView = itemView.findViewById(R.id.tv_business_class);
        }
    }
}