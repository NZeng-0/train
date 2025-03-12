package com.android.train.utils;

import java.util.Calendar;

/**
 * 日期格式化工具栏
 */
public class DateUtils {

    // 获取日期的文本，今天、明天、后天或显示星期几
    public static String getFormattedDate(int selectedMonth, int selectedDay) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，所以加1
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 比较选中的日期和当前日期，判断显示 "今天", "明天", "后天" 或者具体日期
        String dateText;
        if (selectedMonth == currentMonth && selectedDay == currentDay) {
            dateText = String.format("%s月%s日 %s", selectedMonth, selectedDay, "今天");
        } else if (selectedMonth == currentMonth && selectedDay == currentDay + 1) {
            dateText = String.format("%s月%s日 %s", selectedMonth, selectedDay, "明天");
        } else if (selectedMonth == currentMonth && selectedDay == currentDay + 2) {
            dateText = String.format("%s月%s日 %s", selectedMonth, selectedDay, "后天");
        } else {
            // 超过后天，显示星期几
            calendar.set(Calendar.MONTH, selectedMonth - 1); // 设置月份，注意从0开始
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 获取星期几
            String weekDay = getWeekDay(dayOfWeek);
            dateText = String.format("%s月%s日 %s", selectedMonth, selectedDay, weekDay);
        }

        return dateText;
    }

    // 根据星期几的值返回对应的文字表示
    private static String getWeekDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "周日";
            case Calendar.MONDAY: return "周一";
            case Calendar.TUESDAY: return "周二";
            case Calendar.WEDNESDAY: return "周三";
            case Calendar.THURSDAY: return "周四";
            case Calendar.FRIDAY: return "周五";
            case Calendar.SATURDAY: return "周六";
            default: return "";
        }
    }
}

