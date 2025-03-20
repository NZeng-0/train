package com.android.train.ui.query;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.api.ApiResponse;
import com.android.train.api.service.RelationService;
import com.android.train.api.service.StationService;
import com.android.train.model.DateItem;
import com.android.train.model.TrainModel;
import com.android.train.pojo.Relation;
import com.android.train.pojo.StationInfo;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryViewModel extends ViewModel {

    private final RelationService relationService;

    private final MutableLiveData<List<TrainModel>> trainModels = new MutableLiveData<>();

    public QueryViewModel(RelationService relationService) {
        this.relationService = relationService;
    }

    public List<DateItem> generateDateList() {
        List<DateItem> list = new ArrayList<>();
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM.dd", Locale.CHINA);
        SimpleDateFormat sdfDay = new SimpleDateFormat("E", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 15; i++) {
            String dayOfWeek = i == 0 ? "今天" : sdfDay.format(calendar.getTime());
            String date = sdfDate.format(calendar.getTime());
            list.add(new DateItem(dayOfWeek, date));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return list;
    }

    public void loadStationList(int page, int size, String start, String end, String saleTime) {
        relationService.getRelationList(page, size, start,end, saleTime).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ApiResponse<List<Relation>>> call,
                    @NonNull Response<ApiResponse<List<Relation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Relation>> apiResponse = response.body();
                    if (apiResponse.getCode() == 200 && apiResponse.getRows() != null) {
                        Log.d("HomeViewModel", "成功获取车次列表: " + apiResponse.getRows().toString() + " 个车站");
                        // 将API返回数据转换为TrainModel列表
                        trainModels.setValue(convertToTrainModels(apiResponse.getRows()));
                    } else {
                        Log.e("HomeViewModel", "接口返回失败");
                    }
                } else {
                    Log.e("HomeViewModel", "加载车站列表失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Relation>>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "请求失败：" + t.getMessage());
            }
        });
    }
    /**
     * 将API响应数据转换为TrainModel列表
     */
    private List<TrainModel> convertToTrainModels(List<Relation> relations) {
        List<TrainModel> trainModels = new ArrayList<>();

        for (Relation relation : relations) {
            // 从日期时间字符串中提取时间部分
            String departTime = String.valueOf(relation.getDepartureTime());
            String arriveTime = String.valueOf(relation.getArrivalTime());

            // 计算行程时长 (这部分需要根据实际数据格式调整)
            String duration = calculateDuration(departTime, arriveTime);

            // 创建新的TrainModel对象
            TrainModel trainModel = new TrainModel(
                    departTime,                 // 出发时间
                    relation.getTrainNumber(),  // 车次号
                    arriveTime,                 // 到达时间
                    relation.getStartRegion(),  // 出发站
                    relation.getEndStation(),   // 到达站
                    duration,                   // 行程时长
                    "有票",                      // 二等座状态 (示例值，需替换为实际值)
                    "有票",                      // 一等座状态 (示例值，需替换为实际值)
                    "有票"                       // 商务座状态 (示例值，需替换为实际值)
            );

            trainModels.add(trainModel);
        }

        return trainModels;
    }

    /**
     * 计算行程时长
     */
    public String calculateDuration(String departureTime, String arrivalTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US); // 指定 Locale.US 以防止本地化问题

        try {
            Date departure = sdf.parse(departureTime);
            Date arrival = sdf.parse(arrivalTime);

            // 计算时间差（单位：毫秒）
            long diffInMillis = Objects.requireNonNull(arrival).getTime() - Objects.requireNonNull(departure).getTime();

            // 转换为小时和分钟
            long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;

            if (hours > 0) {
                return hours + "小时" + (minutes > 0 ? minutes + "分" : "");
            } else {
                return minutes + "分";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "时间格式错误";
        }
    }

    public LiveData<List<TrainModel>> getTrainModels(){
        return trainModels;
    }
}