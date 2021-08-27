package com.appsereno.helper;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificacionHelper {
    public static final String ID_CHANEL_1="chanel_1";
    public static final String ID_CHANEL_2="chanel_2";
    private static final String CHANEL_NAME_1="chanel 1";
    private static final String CHANEL_NAME_2="chanel 2";
    private static final String CHANEL_NAME_DESCRIPTION_1="chanel of notifications by socket.io";
    private static final String CHANEL_NAME_DESCRIPTION_2="chanel of notifications by socket.io";


    public static  void createChanelNotification(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel chanel1=new NotificationChannel(ID_CHANEL_1,CHANEL_NAME_1, NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription(CHANEL_NAME_DESCRIPTION_1);

            NotificationChannel chanel2=new NotificationChannel(ID_CHANEL_2,CHANEL_NAME_2, NotificationManager.IMPORTANCE_LOW);
            chanel1.setDescription(CHANEL_NAME_DESCRIPTION_2);

            NotificationManager manager =context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel1);
            manager.createNotificationChannel(chanel2);
        }
    }
}
