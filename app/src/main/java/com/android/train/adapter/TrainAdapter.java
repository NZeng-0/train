package com.android.train.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.train.R;
import com.android.train.api.AjaxResult;
import com.android.train.api.RetrofitClient;
import com.android.train.api.service.RelationService;
import com.android.train.model.TrainModel;
import com.android.train.ui.booking.BookingActivity;
import com.android.train.ui.login.LoginActivity;
import com.android.train.utils.AuthUtil;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        holder.trainNumberTextView.setText(train.getTrainNumber());

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (!AuthUtil.isLoggedIn(context)) {
                ToastUtil.showToast(context, "请先登录");
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            } else {
                // 获取当前用户 id
                String userId = PreferencesUtil.getString(context, "id");

                String ridingDate = PreferencesUtil.getString(context, "selectDate");
                String departure = train.getDepartureStation();
                String arrival = train.getArrivalStation();

                RelationService service = RetrofitClient.getClient(context).create(RelationService.class);

                service.isExists(userId, ridingDate, departure, arrival).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<AjaxResult<String>> call, @NonNull Response<AjaxResult<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AjaxResult<String> result = response.body();

                            // 检查响应码是否为 200
                            if (result.getCode() == 200) {
                                // 没有冲突，跳转到详情页
                                Intent intent = toItem(train);
                                context.startActivity(intent);
                            } else {
                                // 存在冲突，显示提示信息
                                ToastUtil.showToast(context, "您在该日期已有相似行程，请检查");
                            }
                        } else {
                            // 响应失败
                            ToastUtil.showToast(context, "行程检查失败，请稍后重试");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AjaxResult<String>> call, @NonNull Throwable t) {
                        // 网络请求失败
                        ToastUtil.showToast(context, "网络错误，请检查网络连接");
                    }
                });
            }

        });
    }

    private Intent toItem(TrainModel train) {
        Intent intent = new Intent(context, BookingActivity.class);
        intent.putExtra("trainId", train.getId());
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
        return intent;
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