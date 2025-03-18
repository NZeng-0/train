package com.android.train.adapter;

import android.content.Context;
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
        setTicketStatus(holder.businessClassTextView, train.getBusinessClassStatus());

        if (train.getTrainNumber().contains("G")) {
            holder.trainNumberTextView.setText(train.getTrainNumber());
        }

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            String trainInfo = train.getTrainNumber() + " " +
                    train.getDepartureStation() + " → " +
                    train.getArrivalStation();
            Toast.makeText(context, "选择了列车: " + trainInfo, Toast.LENGTH_SHORT).show();
        });
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

    // Filter methods
    public void filterByTrainType(boolean highSpeedOnly, boolean normalOnly) {
        // Implementation of filtering logic
        // This would typically involve creating a filtered list and calling notifyDataSetChanged()
    }

    public void filterByAvailability(boolean availableOnly) {
        // Implementation of filtering logic
        // This would typically involve creating a filtered list and calling notifyDataSetChanged()
    }

    public void sortByDepartureTime() {
        // Sort trains by departure time
        // This would typically involve sorting the list and calling notifyDataSetChanged()
    }

    public void sortByDuration() {
        // Sort trains by duration
        // This would typically involve sorting the list and calling notifyDataSetChanged()
    }

    public void sortByPrice() {
        // Sort trains by price
        // This would typically involve sorting the list and calling notifyDataSetChanged()
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