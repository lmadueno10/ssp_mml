package com.appsereno.service;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.appsereno.R;
import com.appsereno.config.GlobalVariables;
import com.appsereno.helper.Constants;
import com.appsereno.helper.NotificacionHelper;
import com.appsereno.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NotificacionService extends Service {

     private Socket socket;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String userInfo = preferences.getString("user_info", "-");
        Integer idPersonal=preferences.getInt("id_personal",0);
        Log.i(Constants.LOG_SOCKET,"idUuario:"+idPersonal);
        if(socket==null){
            try {
                socket= IO.socket(GlobalVariables.getSocketUri());
            } catch (URISyntaxException e) {
                Log.e(Constants.LOG_SOCKET,e.getMessage());
            }
        }
        super.onCreate();
        socket.connect();
        socket.on("usuario_"+idPersonal,onNewIncident);
        socket.on("login_"+idPersonal,onUserLogin);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            socket=IO.socket(GlobalVariables.getSocketUri());
        } catch (URISyntaxException e) {
            Log.e(Constants.LOG_SOCKET,e.getMessage());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void setPendingIntenet(){
        Intent intent = new Intent(this, MainActivity.class);
        if(socket!=null){
            intent.putExtra("socket", (Parcelable) socket);
        }
        TaskStackBuilder taskStackBuilder= TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        pendingIntent=taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    Emitter.Listener onNewIncident= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            setPendingIntenet();
            NotificacionHelper.createChanelNotification(getApplicationContext());
            try {
                notifyIncident(args);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Emitter.Listener onUserLogin= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            String userInfo = preferences.getString("user_info", "-");
            Log.i(Constants.LOG_SOCKET,userInfo+" is conected");
        }
    };

    public void notifyIncident(Object... obj) throws JSONException {
        if(obj[0]!=null){
            JSONObject data = (JSONObject) obj[0];
            JSONObject in=data.getJSONObject("incidencia");
            String description=in.getString("descripcion");
            String direccion=in.getString("direccion");
            Log.i(Constants.LOG_SOCKET,in.toString());

            NotificacionHelper.createChanelNotification(this);
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this, NotificacionHelper.ID_CHANEL_1)
                    .setSmallIcon(R.drawable.ic_local_police_24)
                    .setContentTitle(direccion)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.GREEN)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notiManager =NotificationManagerCompat.from(getApplicationContext());
            notiManager.notify(Constants.NOTIFICATION_ID,builder.build());
        }
    }
}