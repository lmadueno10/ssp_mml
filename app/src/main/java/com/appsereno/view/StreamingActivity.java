package com.appsereno.view;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appsereno.R;
import com.appsereno.config.GlobalVariables;
import com.appsereno.helper.Constants;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.DefaultWraper;
import com.appsereno.model.webservice.IncidenciaService;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.google.android.material.snackbar.Snackbar;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StreamingActivity extends AppCompatActivity implements RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {
    @Inject
    IncidenciaService incidenciaService;
    private static final String TAG = Constants.TAG_STREAMING;
    public final static int RC_CAMERA = 100;

    private Button btnPublish;
    private Button btnSwitchCamera;
    private Button btnRecord;
    private Button btnSwitchEncoder;
    private Button btnPause;
    private boolean isPublishing=false;
    private SharedPreferences sp;
    private SharedPreferences sp_user;
    private String rtmpUrl = GlobalVariables.PATH_BASE_STREAMING;
    private String urlVideo;
    private SrsPublisher mPublisher;
    private SrsCameraView mCameraView;

    private Integer idUsuario;
    private String userInfo;

    private Socket socket;
    private Disposable disposable;

    private int mWidth = 640;
    private int mHeight = 480;
    private boolean isPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.streaming_layout);
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        userInfo = preferences.getString("user_info", "-");
        idUsuario=preferences.getInt("id_personal",0);
        // response screen rotation event
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initUrlStreaming();
        requestPermission();
        if(socket==null) {
            initSocket();
            socket.connect();
        }
        setUpDagger();
    }
    private void setUpDagger(){
        ((BaseApplication)getApplication()).getRetrofitComponent().inject(this);
    }
    private void requestPermission() {
        //1. checking permissions
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            //2. request permits
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_CAMERA);
        }else{
            //if permissions are granted, start the app.
            isPermissionGranted = true;
            init();
        }
    }

    //3. successful or unsuccessful application callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permissions are granted by the user
                isPermissionGranted = true;
                init();
            } else {
                //if the user denies the permissions, the app will terminate.
                finish();
            }
        }
    }
    private void initSocket(){
        try {
            socket = IO.socket(GlobalVariables.getSocketUri());
            socket.on("usuario_" + idUsuario, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                }
            });
        }catch (Exception e) {
            Toast.makeText(this,"Ocurrio un erro al conectar al servidor",Toast.LENGTH_LONG).show();
            Log.e("SOCKET_ERROR",e.getMessage());
        }
    }
    private void initUrlStreaming(){
        sp_user=getSharedPreferences("credenciales",MODE_PRIVATE);
        Integer idUsuario=sp_user.getInt("id_personal",0);
        rtmpUrl+="video_"+idUsuario;
    }
    private void init() {
        // restore data.
        btnPublish = (Button) findViewById(R.id.publish);
        btnSwitchCamera =(Button)findViewById(R.id.swCam);
        btnRecord = (Button) findViewById(R.id.record);
        btnSwitchEncoder = (Button) findViewById(R.id.swEnc);
        btnPause = (Button) findViewById(R.id.pause);
        btnPause.setEnabled(false);
        mCameraView = (SrsCameraView) findViewById(R.id.glsurfaceview_camera);

        mPublisher = new SrsPublisher(mCameraView);
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        mPublisher.setPreviewResolution(mWidth, mHeight);
        //mPublisher.setOutputResolution(mWidth,mHeight);
        mPublisher.setOutputResolution(mHeight, mWidth);
        mPublisher.setVideoHDMode();
        //mPublisher.setVideoSmoothMode();
        mPublisher.startCamera();

        mCameraView.setCameraCallbacksHandler(new SrsCameraView.CameraCallbacksHandler(){
            @Override
            public void onCameraParameters(Camera.Parameters params) {
                //params.setFocusMode("custom-focus");
                //params.setWhiteBalance("custom-balance");
                //etc...
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPublish.getText().toString().contentEquals("transmitir")) {
                    //SharedPreferences.Editor editor = sp.edit();
                    //editor.putString("rtmpUrl", rtmpUrl);
                    //editor.apply();

                    mPublisher.startPublish(rtmpUrl);
                    mPublisher.startCamera();

                    if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
                        Toast.makeText(getApplicationContext(), "Use hard encoder", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Use soft encoder", Toast.LENGTH_SHORT).show();
                    }
                    btnPublish.setText("finalizar");
                    btnSwitchEncoder.setEnabled(false);
                    btnPause.setEnabled(true);
                } else if (btnPublish.getText().toString().contentEquals("finalizar")) {

                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    btnPublish.setText("transmitir");
                    btnRecord.setText("record");
                    btnSwitchEncoder.setEnabled(true);
                    btnPause.setEnabled(false);

                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPause.getText().toString().equals("Pausar")){
                    mPublisher.pausePublish();
                    btnPause.setText("resume");
                }else{
                    mPublisher.resumePublish();
                    btnPause.setText("Pausar");
                }
            }
        });

        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublisher.switchCameraFace((mPublisher.getCameraId() + 1) % Camera.getNumberOfCameras());
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRecord.getText().toString().contentEquals("record")) {
                    Date f=new Date();
                    String recPath = Environment.getExternalStorageDirectory().getPath() + "/vid_"+f.getTime()+".mp4";
                    if (mPublisher.startRecord(recPath)) {
                        btnRecord.setText("Pausar");
                    }
                } else if (btnRecord.getText().toString().contentEquals("Pausar")) {
                    mPublisher.pauseRecord();
                    btnRecord.setText("resume");
                } else if (btnRecord.getText().toString().contentEquals("resume")) {
                    mPublisher.resumeRecord();
                    btnRecord.setText("Pausar");
                }
            }
        });

        btnSwitchEncoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
                    mPublisher.switchToSoftEncoder();
                    btnSwitchEncoder.setText("hard encoder");
                } else if (btnSwitchEncoder.getText().toString().contentEquals("hard encoder")) {
                    mPublisher.switchToHardEncoder();
                    btnSwitchEncoder.setText("soft encoder");
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mPublisher.getCamera() == null && isPermissionGranted){
            //if the camera was busy and available again
            mPublisher.startCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button btn = (Button) findViewById(R.id.publish);
        btn.setEnabled(true);
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
        btnRecord.setText("record");
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (btnPublish.getText().toString().contentEquals("finalizar")) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }

   private void handleException(Exception e) {
        try {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            btnPublish.setText("transmitir");
            btnRecord.setText("record");
            btnSwitchEncoder.setEnabled(true);
        } catch (Exception e1) {
            //
        }
    }

    // Implementation of SrsRtmpListener.

    @Override
    public void onRtmpConnecting(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        JSONObject obj=new JSONObject();
        try {
            obj.put("idUduario",idUsuario);
            obj.put("userInfo",userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("streaming",obj.toString());
            //Toast.makeText(getApplicationContext(),"Socket conectado",Toast.LENGTH_LONG).show();
        if (btnRecord.getText().toString().contentEquals("record")) {
            Date f=new Date();
            String recPath = Environment.getExternalStorageDirectory().getPath() + "/vid_"+f.getTime()+".mp4";
            urlVideo=recPath;
            if (mPublisher.startRecord(urlVideo)) {
                btnRecord.setText("Pausar");
            }
        } else if (btnRecord.getText().toString().contentEquals("Pausar")) {
            mPublisher.pauseRecord();
            btnRecord.setText("resume");
        } else if (btnRecord.getText().toString().contentEquals("resume")) {
            mPublisher.resumeRecord();
            btnRecord.setText("Pausar");
        }
    }

    @Override
    public void onRtmpVideoStreaming() {
    }

    @Override
    public void onRtmpAudioStreaming() {
    }

    @Override
    public void onRtmpStopped() {
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        JSONObject obj=new JSONObject();
        try {
            obj.put("idUduario",idUsuario);
            obj.put("userInfo",userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("streaming_off",obj.toString());
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        Log.i(TAG, String.format("Output Fps: %f", fps));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Audio bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    // Implementation of SrsRecordHandler.

    @Override
    public void onRecordPause() {
        Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordResume() {
        Toast.makeText(getApplicationContext(), "Record resumed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStarted(String msg) {
        Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordFinished(String msg) {
        Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(3*1000);
            Toast.makeText(this,"Enviando video",Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        cargarVideo(urlVideo);
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    // Implementation of SrsEncodeHandler.

    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "Network weak", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {
        Toast.makeText(getApplicationContext(), "Network resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    public void cargarVideo(String url){
        if(url!=null){
            Date fecha = new Date();
            SimpleDateFormat sdFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdHora = new SimpleDateFormat("HH:mm:ss");

        File video= new File(url);
            MultipartBody.Part videoPart = (video!=null)?MultipartBody.Part.createFormData("video", video.getName(), RequestBody.create(MediaType.parse("video/*"), video)):null;

            //lin.setVisibility(View.VISIBLE);
            incidenciaService
                    .guardarVideo(
                            videoPart,
                            RequestBody.create(MediaType.parse("text/plain"), sdFecha.format(fecha)),
                            RequestBody.create(MediaType.parse("text/plain"), sdHora.format(fecha)),
                            RequestBody.create(MediaType.parse("text/plain"), idUsuario.toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Observer<DefaultWraper>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onNext(DefaultWraper subtipoIncidenciaWraper) {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("TAG1", "Error: " + e.getMessage());
                                    Toast.makeText(getApplicationContext(), "Ocurrio un error intente denuevo", Toast.LENGTH_LONG);
                                    //lin.setVisibility(View.GONE);

                                }

                                @Override
                                public void onComplete() {
                                    Log.d("TAG1", "OnComplete: ");

                                    Toast.makeText(getApplicationContext(),"Video registrado con exito",Toast.LENGTH_LONG).show();
                                    //lin.setVisibility(View.GONE);
                                    urlVideo=null;
                                }
                            }
                    );
        }else{
            Toast.makeText(getApplicationContext(),"Especifique una ruta válida",Toast.LENGTH_LONG).show();
        }
    }

}
