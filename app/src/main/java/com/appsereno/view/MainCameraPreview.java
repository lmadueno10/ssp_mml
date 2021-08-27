package com.appsereno.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appsereno.R;
import com.appsereno.view.fragments.ImageGalleryFragment;
import com.appsereno.viewmodel.adapter.BucketsAdapter;
import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.appsereno.config.GlobalVariables.ruta_evidencia;

public class MainCameraPreview extends AppCompatActivity {


    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout bottom_sheet;
    RecyclerView peekRecyclerView , MainRecyclerView ;
    CameraKitView cameraKitView;
    RelativeLayout peekView, collapsedView;

    //cargar media from device
    private final String[] projection = new String[]{ MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA };
    private final String[] projection2 = new String[]{MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.DATA };
    private List<String> bucketNames= new ArrayList<>();
    private List<String> bitmapList=new ArrayList<>();
    public static List<String> imagesList= new ArrayList<>();
    public static List<Boolean> selected=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_camera_preview);

        cameraKitView = findViewById(R.id.camera);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        peekRecyclerView = findViewById(R.id.peekRecyclerView);
        peekView = findViewById(R.id.peekView);
        collapsedView = findViewById(R.id.collapsedView);
        MainRecyclerView = findViewById(R.id.MainRecyclerView);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        //load media from device
        bitmapList.clear();
        imagesList.clear();
        bucketNames.clear();
        getPicBuckets();
        populateRecyclerView();
        initButtons();
        initRecordButton();
        /*
        ImageButton imgBtn=findViewById(R.id.clickImage);
        imgBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"Recording video...",Toast.LENGTH_LONG).show();
                cameraKitView.captureVideo(new CameraKitView.VideoCallback() {
                    @Override
                    public void onVideo(CameraKitView cameraKitView, Object o) {
                        cameraKitView.startVideo();

                    }
                });
                return true;
            }
        });*/
        /*
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        Date d= new Date();
                        File savedPhoto = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/evidencia/img_"+d.getTime()+".jpg");
                        try {
                            Toast.makeText(getApplicationContext(),"Ruta "+savedPhoto.getPath(),Toast.LENGTH_LONG).show();
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                            Toast.makeText(getApplicationContext(),"Imagen Capturada",Toast.LENGTH_LONG).show();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                /*
                if(selectedData!=null&&selectedData.size()>0){
                    //uploadFile(selectedData.get(0).toString());
                    File f=new File(selectedData.get(0).toString());
                    FTPUploadRequest ftp= new FTPUploadRequest(getApplicationContext(), ConfigFtp.SERVER, 21);
                    try {
                        ftp.setUsernameAndPassword(ConfigFtp.USER, ConfigFtp.PASSWORD)
                                .addFileToUpload(selectedData.get(0).toString(), "/files/"+f.getName())
                                .startUpload();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Image Captured.",Toast.LENGTH_LONG).show();
                }
                *//*
            }
        });

        */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fabOk=findViewById(R.id.fabOk);
        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("result", selectedData);
                returnIntent.putExtra("url_image",url_image);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        //

        //peekRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
        //peekRecyclerView.setAdapter(new HomeAdapter(MainActivity.this));

        //MainRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        //MainRecyclerView.setAdapter(new HomeAdapter(MainActivity.this));
        //MainRecyclerView.scrollToPosition(0);


        MainRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!MainRecyclerView.canScrollVertically(-1)){
                    if(sheetBehavior.isDraggable()== false){
                        sheetBehavior.setDraggable(true);
                    }
                }else{
                    if(sheetBehavior.isDraggable()==true){
                        sheetBehavior.setDraggable(false);
                    }
                }
            }
        });



        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e( "onStateChanged: ","hidden" );
                        //sheetBehavior.setDraggable(true);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.e("onStateChanged: ","expanded" );
                        MainRecyclerView.suppressLayout(false);
                        sheetBehavior.setDraggable(false);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Log.e( "onStateChanged: ","collapse" );
                        sheetBehavior.setDraggable(true);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        MainRecyclerView.suppressLayout(true);
                        Log.e( "onStateChanged: ", "dragging" );
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        Log.e( "onStateChanged: ",sheetBehavior.isDraggable()+" settled " );

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                peekView.setAlpha(1.0f-v);
                collapsedView.setAlpha(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    private boolean cancelLongClick = false;
    private boolean recordingVideo=false;
    private String url_image;
    private void initRecordButton(){
        ImageButton imgBtn=findViewById(R.id.clickImage);

        imgBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){


                    cancelLongClick = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!cancelLongClick) {
                                //Toast.makeText(getApplicationContext(),"Iniciando grabacion de video",Toast.LENGTH_LONG).show();
                                recordingVideo=true;
                                cameraKitView.startVideo();
                                imgBtn.setImageResource(R.drawable.camera_back);
                                imgBtn.setBackgroundResource(R.drawable.ic_baseline_album_80);
                                cameraKitView.captureVideo(new CameraKitView.VideoCallback() {
                                    @Override
                                    public void onVideo(CameraKitView cameraKitView, Object o) {
                                        File f =(File)o;
                                        Toast.makeText(getApplicationContext(),f.getPath(),Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else{

                            }
                        }
                    }, 3000);


                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("TOUCH_EVENT", "MotionEvent.ACTION_UP");
                    //Si se deja de tocar antes de 5 segundos cancela el evento.
                    cancelLongClick = true;
                    if(recordingVideo){
                        Toast.makeText(getApplicationContext(),"Finalizo grabacion de  video",Toast.LENGTH_LONG).show();
                        recordingVideo=false;
                        cameraKitView.stopVideo();
                        imgBtn.setBackgroundResource(R.drawable.camera_back);
                        imgBtn.setImageResource(R.drawable.camera_back);

                    }else{

                        imgBtn.setImageResource(R.drawable.ic_camera_alt_24);
                        imgBtn.setBackgroundResource(R.drawable.camera_back);
                        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                            @Override
                            public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                                Date d= new Date();
                                File savedPhoto = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/evidencia/img_"+d.getTime()+".jpg");
                                try {
                                    url_image=savedPhoto.getPath();
                                    FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                                    outputStream.write(capturedImage);
                                    outputStream.close();
                                    imgBtn.setImageResource(R.drawable.camera_back);
                                    getPicBuckets();
                                    BucketsAdapter mAdapter = new BucketsAdapter(bucketNames,bitmapList,getApplicationContext());

                                    peekRecyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    Intent returnIntent = new Intent();
                                    returnIntent.putStringArrayListExtra("result", selectedData);
                                    returnIntent.putExtra("url_image",url_image);
                                    setResult(RESULT_OK, returnIntent);
                                    finish();
                                    //Toast.makeText(getApplicationContext(),"Imagen Capturada",Toast.LENGTH_LONG).show();
                                } catch (java.io.IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }


                return true;
            }
        });

    }

    private void initButtons(){
        ImageView imgFlash=findViewById(R.id.imgFlash);
        ImageView imgCameraChange=findViewById(R.id.imgCameraChange);
        imgFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgFlash.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_flash_off_24).getConstantState()){
                    cameraKitView.setFlash(CameraKit.FLASH_ON);
                    imgFlash.setImageResource(R.drawable.ic_flash_on_24);
                }else{
                    cameraKitView.setFlash(CameraKit.FLASH_OFF);
                    imgFlash.setImageResource(R.drawable.ic_flash_off_24);
                }

            }
        });
        imgCameraChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgCameraChange.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_camera_front_24).getConstantState()){
                    cameraKitView.toggleFacing();
                    imgCameraChange.setImageResource(R.drawable.ic_camera_rear_24);
                }else{
                    cameraKitView.toggleFacing();
                    imgCameraChange.setImageResource(R.drawable.ic_camera_front_24);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //load media from device
    public void getPicBuckets(){
        Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Images.Media.DATE_ADDED);
        ArrayList<String> bucketNamesTEMP = new ArrayList<>(cursor.getCount());
        ArrayList<String> bitmapListTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                String image = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(image);
                if (file.exists() && !albumSet.contains(album)) {
                    bucketNamesTEMP.add(album);
                    bitmapListTEMP.add(image);
                    albumSet.add(album);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (bucketNamesTEMP == null) {
            bucketNames = new ArrayList<>();
        }
        bucketNames.clear();
        bitmapList.clear();
        bucketNames.addAll(bucketNamesTEMP);
        bitmapList.addAll(bitmapListTEMP);
    }

    private void populateRecyclerView() {
        BucketsAdapter mAdapter = new BucketsAdapter(bucketNames,bitmapList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        //peekRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
        //peekRecyclerView.setAdapter(new HomeAdapter(MainActivity.this));

        peekRecyclerView.setLayoutManager(mLayoutManager);
        peekRecyclerView.setItemAnimator(new DefaultItemAnimator());
        peekRecyclerView.setAdapter(mAdapter);
        peekRecyclerView.addOnItemTouchListener(new ImageGalleryFragment.RecyclerTouchListener(this, peekRecyclerView, new ImageGalleryFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent= new Intent(getApplicationContext(), Gallery.class);
                // Set the title
                intent.putExtra("title","Select media");
                // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                intent.putExtra("mode",1);
                intent.putExtra("maxSelection",5); // Optional
                startActivityForResult(intent,1);
                /*
                getPictures(bucketNames.get(position));
                Intent intent=new Intent(getApplicationContext(), OpenGallery.class);
                intent.putExtra("FROM","Images");
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mAdapter.notifyDataSetChanged();

        MainRecyclerView.setLayoutManager(new GridLayoutManager(MainCameraPreview.this, 3));
        MainRecyclerView.setAdapter(mAdapter);
        MainRecyclerView.scrollToPosition(0);
        MainRecyclerView.addOnItemTouchListener(new ImageGalleryFragment.RecyclerTouchListener(this, peekRecyclerView, new ImageGalleryFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent= new Intent(getApplicationContext(), Gallery.class);
                // Set the title
                intent.putExtra("title","Select media");
                // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                intent.putExtra("mode",1);
                intent.putExtra("maxSelection",5); // Optional
                startActivityForResult(intent,1);
                /*
                getPictures(bucketNames.get(position));
                Intent intent=new Intent(getApplicationContext(), OpenGallery.class);
                intent.putExtra("FROM","Images");
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ImageGalleryFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ImageGalleryFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void getPictures(String bucket){
        selected.clear();
        Cursor cursor = this.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection2,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?",new String[]{bucket},MediaStore.Images.Media.DATE_ADDED);
        ArrayList<String> imagesTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
                file = new File(path);
                if (file.exists() && !albumSet.contains(path)) {
                    imagesTEMP.add(path);
                    albumSet.add(path);
                    selected.add(false);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (imagesTEMP == null) {
            imagesTEMP = new ArrayList<>();
        }
        imagesList.clear();
        imagesList.addAll(imagesTEMP);
    }

    private ArrayList selectedData;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
                selectedData=data.getStringArrayListExtra("result");
                if(selectedData!=null&&selectedData.size()>0){
                    Toast.makeText(this,selectedData.size()+" Archivos seleccionados",Toast.LENGTH_LONG).show();

                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("result", selectedData);
                returnIntent.putExtra("url_image",url_image);
                setResult(RESULT_OK, returnIntent);
                finish();
                }
            }
            finish();
        }
    }

    public void uploadFile(String routeFile){
        /*
                    FTPClient con = null;
                    try
                    {
                        con = new FTPClient();
                        con.connect("104.236.56.10");

                        if (con.login("ftpuser", "userftp"))
                        {
                            con.enterLocalPassiveMode(); // important!
                            con.setFileType(FTP.BINARY_FILE_TYPE);
                            //String data = "/sdcard/vivekm4a.m4a";
                            File f=new File(routeFile);

                            FileInputStream in = new FileInputStream(f);

                            boolean result = con.storeFile("/files/"+f.getName(), in);
                            in.close();
                            if (result) {Log.v("upload result", "succeeded");
                                Toast.makeText(getApplicationContext(),"File Uploaded.",Toast.LENGTH_LONG).show();
                            }
                            con.logout();
                            con.disconnect();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Error al conectar",Toast.LENGTH_LONG).show();
                    }*/



    }

/*
    static final int OPEN_MEDIA_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.btn);
        btn.setOnClickListener(this::abrirGaleria);

    }

    public void abrirGaleria(View v){
        Intent intent= new Intent(this, Gallery.class);
        // Set the title
        intent.putExtra("title","Select media");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode",1);
        intent.putExtra("maxSelection",3); // Optional
        startActivityForResult(intent,OPEN_MEDIA_PICKER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
            }
        }
    }*/
}