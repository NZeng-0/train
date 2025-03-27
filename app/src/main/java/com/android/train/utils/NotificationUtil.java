package com.android.train.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.train.R;

public class NotificationUtil {
    private static final String CHANNEL_ID = "default_channel";
    private static final String CHANNEL_NAME = "Default Notifications";

    // 创建通知通道
    public static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH  // 设置为 HIGH 以启用悬浮通知
        );
        channel.setDescription("App 悬浮通知通道");
        channel.enableLights(true);
        channel.enableVibration(true);  // 震动提醒
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);  // 允许锁屏显示

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    // 发送悬浮通知（Heads-up Notification）
    public static void sendNotification(Context context, int id, String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission("android.permission.POST_NOTIFICATIONS")
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return; // 没有权限则不发送通知
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // 设置高优先级，确保 Heads-up
                .setDefaults(Notification.DEFAULT_ALL)  // 震动 + 声音
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }

}

