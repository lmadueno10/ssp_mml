package com.appsereno.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.appsereno.config.GlobalVariables;

import net.gotev.uploadservice.data.UploadInfo;
import net.gotev.uploadservice.network.ServerResponse;
import net.gotev.uploadservice.observer.request.RequestObserverDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.annotations.NonNull;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RequestHelper implements RequestObserverDelegate {
    private String nameFile;
    private Date date;
    private Context context;
    private boolean isNotified=false;

    public RequestHelper(Context context,String fileNale,Date fechaHora){
        super();
        this.date=fechaHora;
        this.nameFile=fileNale;
        this.context=context;

    }
    @Override
    public void onCompleted(@NonNull Context context, @NonNull UploadInfo uploadInfo) {
        Log.i("OBSERVER",date.toString()+" "+nameFile+" complete");
    }

    @Override
    public void onCompletedWhileNotObserving() {
        Log.i("OBSERVER",date.toString()+" "+nameFile+"| completed not observer");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        notifiComplete(sd.format(date),nameFile);
    }

    @Override
    public void onError(@NonNull Context context, @NonNull UploadInfo uploadInfo, @NonNull Throwable throwable) {
        Log.i("OBSERVER_ERROR",throwable.getMessage());
        try {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(this.context);
            Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
            aBuilder.setTitle("Error al subir Archivo");
            aBuilder.setMessage(throwable.getMessage());
            aBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }catch (Exception e){
            Log.e("ERROR_DIALOG",e.getMessage());
        }
    }

    @Override
    public void onProgress(@NonNull Context context, @NonNull UploadInfo uploadInfo) {

    }

    @Override
    public void onSuccess(@NonNull Context context, @NonNull UploadInfo uploadInfo, @NonNull ServerResponse serverResponse) {
        Toast.makeText(context,nameFile +" Uploaded",Toast.LENGTH_LONG).show();
        Log.i("OBSERVER",date.toString()+" "+nameFile+"|onSuccess");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        notifiComplete(sd.format(date),nameFile);
    }

    private void notifiComplete(String fecha,String file){
        try {
            if(!isNotified){
                Socket socket= IO.socket(GlobalVariables.getSocketUri());

                JSONObject message= new JSONObject();
                try {
                    message.put("fileName",file);
                    message.put("fecha",fecha);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(message!=null){
                    socket.emit("ftp_complete",message);
                    socket.close();
                }
                socket.connect();
                isNotified=true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
