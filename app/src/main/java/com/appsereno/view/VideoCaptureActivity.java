package com.appsereno.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsereno.R;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class VideoCaptureActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private ImageView capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM = 3;
    private static final int RC_HANDLE_RECORD_AUDIO_PERM = 4;
    private String TAG = "test";
    private TextView mTimerTv;
    private String filePath;
    private Intent intent;
    private long mCountDownTimer = 60000;
    public static int cameraId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);
        if (ContextCompat.checkSelfPermission(VideoCaptureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VideoCaptureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoCaptureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        mTimerTv = (TextView) findViewById(R.id.tvTimer);
        String uriVideo=getIntent().getStringExtra("uri_video");

        File mediaFile =
                new File(uriVideo);
        filePath = Uri.fromFile(mediaFile).getPath();
        intent   = getIntent();

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int sdCard = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (sdCard == PackageManager.PERMISSION_GRANTED) {
            int recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (recordAudio == PackageManager.PERMISSION_GRANTED) {
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    initialize();
                    initializeCamera();
                } else {
                    requestCameraPermission();
                }
            } else {
                requestRecordAudioPermission();
            }
        } else {
            requestSDCardPermission();
        }

    }

    private int findFrontFacingCamera() {
        cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "El dispositivo no tiene camara!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED)
            initializeCamera();
    }

    private void initializeCamera() {
        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "Camara frontal no encontrada.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);

        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        capture = (ImageView) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (ImageView) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);
        capture.setSelected(true);
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {
                    Toast toast = Toast.makeText(myContext, "Su dispositivo solo tiene una camara!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };

    public void chooseCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                mPreview.refreshCamera(mCamera);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    boolean recording = false;
    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {
                // stop recording and release camera
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                Toast.makeText(VideoCaptureActivity.this, "Video Listo para enviar!", Toast.LENGTH_LONG).show();
                recording = false;
                capture.setSelected(true);
                lanzarPreview();
            } else {
                if (!prepareMediaRecorder()) {
                    Toast.makeText(VideoCaptureActivity.this, "Fallo al grabar video", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table

                        try {
                            capture.setSelected(false);
                            mediaRecorder.start();
                            startCountDownTimer();
                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });

                recording = true;
            }
        }
    };

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    private boolean prepareMediaRecorder() {

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOrientationHint(CameraPreview.rotate);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(10000000); // Set max file size 1M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * Handles the requesting of the camera permission.
     */
    private void requestCameraPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_CAMERA_PERM);

    }

    private void requestSDCardPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "No tiene permisos para guardar archivos");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM);

    }

    private void requestRecordAudioPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "No tiene permisos para usar el microfono");

        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_RECORD_AUDIO_PERM);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RC_HANDLE_CAMERA_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //cameraView.setVisibility(View.VISIBLE);
                    initialize();
                    initializeCamera();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("No se puede grabar el video porque no tiene permisos para usar la camara.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("No se puede grabar video no tiene permisos para grabar video.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_RECORD_AUDIO_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("No se puede grabar video, no tiene permiso de grabar audio.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;

        }
    }


    private void startCountDownTimer(){
        new CountDownTimer(mCountDownTimer, 1000) {

            public void onTick(final long millisUntilFinished) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTv.setText("" + millisUntilFinished / 1000);
                    }
                });

            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTv.setText("Listo!");
                        if (recording) {
                            // stop recording and release camera
                            mediaRecorder.stop(); // stop the recording
                            releaseMediaRecorder(); // release the MediaRecorder object
                            Toast.makeText(VideoCaptureActivity.this, "Video Listo para enviar!", Toast.LENGTH_LONG).show();
                            capture.setSelected(true);
                            recording = false;
                            lanzarPreview();
                        }
                    }
                });

            }
        }.start();
    }

    private void lanzarPreview(){
        Intent intent= new Intent(this,VideoPreviewActivity.class);
        intent.putExtra("uri_video",filePath);
        startActivity(intent);
        finish();
    }
}