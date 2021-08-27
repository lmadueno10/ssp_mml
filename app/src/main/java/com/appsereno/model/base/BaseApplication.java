package com.appsereno.model.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadServiceConfig;

/**
 * BaseApplication is the class that extends from the Application class.
 * This class is where the connection to the rest of the API begins.
 */
public class BaseApplication extends Application {
    private RetrofitComponent retrofitComponent;
    private static final String notificationChannelID="TestChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitComponent = DaggerRetrofitComponent
                .builder()
                .retrofitModule(new RetrofitModule())
                .build();
        createNotificationChannel();

        UploadServiceConfig.initialize(
                this,
                notificationChannelID,
                BuildConfig.DEBUG
        );
    }

    /**
     * This Method always returns a Instance of RetrofitComponnent
     * @return RetrofitComponent Instance of {@link RetrofitComponent}
     */
    public RetrofitComponent getRetrofitComponent(){
        return retrofitComponent;
    }

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannelID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}
