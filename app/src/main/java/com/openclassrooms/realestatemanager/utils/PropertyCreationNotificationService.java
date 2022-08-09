package com.openclassrooms.realestatemanager.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.MainActivity;

public class PropertyCreationNotificationService {

    private Context mContext;
    private NotificationManager notificationManager;

    public PropertyCreationNotificationService(Context context) {
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static String PROPERTY_CREATION_CHANNEL_ID = "property_creation_channel";

    public void showNotification(long propertyId) {
        System.out.println("TestId4 :" + propertyId);
        Intent activityIntent = new Intent(mContext, MainActivity.class);
        activityIntent.putExtra("PropertyId", propertyId);
        int flags;
        PendingIntent activityPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activityPendingIntent = PendingIntent.getActivity(
                    mContext,
                    1,
                    activityIntent,
                    PendingIntent.FLAG_IMMUTABLE+PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            activityPendingIntent = PendingIntent.getActivity(
                    mContext,
                    1,
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String contentTitle = mContext.getString(R.string.property_creation_notification_title);
        String contentText = mContext.getString(R.string.property_creation_notification_text);
        Notification notification = new NotificationCompat.Builder(mContext, PROPERTY_CREATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(activityPendingIntent)
                .build();
        notificationManager.notify(1, notification);
    }
}
