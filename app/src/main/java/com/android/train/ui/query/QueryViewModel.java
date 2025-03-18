package com.android.train.ui.query;

import androidx.lifecycle.ViewModel;

import com.android.train.model.DateItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QueryViewModel extends ViewModel {

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
}