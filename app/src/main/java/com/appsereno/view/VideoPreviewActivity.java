package com.appsereno.view;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.appsereno.R;

import java.io.File;

public class VideoPreviewActivity extends AppCompatActivity {
    VideoView videoView;
    String pathVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel=findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(this::confirmar);
        btnCancel.setOnClickListener(this::cancelar);
        pathVideo=getIntent().getStringExtra("uri_video");
        Uri uri=Uri.parse(pathVideo);
        videoView = findViewById(R.id.videoPreview);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
    public void confirmar(View v){
        Toast.makeText(this,"Video preparado para enviar.",Toast.LENGTH_LONG).show();
        finish();
    }

    public void cancelar(View v){
        File file= new File(pathVideo);
        file.delete();
        Toast.makeText(this,"Cancelo la grabación de video",Toast.LENGTH_LONG).show();
        finish();
    }
}