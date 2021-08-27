package com.appsereno.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent notification= new Intent(context,NotificacionService.class);
            context.startService(notification);
    }
}