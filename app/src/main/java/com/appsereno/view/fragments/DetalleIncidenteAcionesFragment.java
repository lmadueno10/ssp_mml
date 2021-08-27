package com.appsereno.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsereno.R;
import com.appsereno.config.GlobalVariables;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.ClasificacionIncidencia;
import com.appsereno.model.entities.ClasificacionWraper;
import com.appsereno.model.entities.DefaultWraper;
import com.appsereno.model.entities.IncidenciaWraper;
import com.appsereno.model.entities.TipoAccion;
import com.appsereno.model.entities.TipoAccionWraper;
import com.appsereno.model.webservice.IncidenciaService;
import com.appsereno.view.VideoCaptureActivity;
import com.appsereno.viewmodel.adapter.DetalleIncidenteAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class DetalleIncidenteAcionesFragment extends Fragment {

    @Inject
    IncidenciaService incidenciaService;
    private List<TipoAccion> tipoAccionList;
    private List<String> tipoList;

    private LinearLayout lin;
    private File file = new File(GlobalVariables.ruta_evidencia);
    private File imgCapturada;
    private File videoCapturado;

    private Integer idIncidencia;
    private Integer idTipoAccion;

    private ImageButton btnImage;
    private ImageButton btnVideo;
    private Button btnSaveIncidencia;
    private String pathVideo;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE=0;

    private Disposable disposable;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_detalle_incidente_acciones,container,false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("detalle_incidente", Context.MODE_PRIVATE);
        idIncidencia = preferences.getInt("id_incidencia", 0);
        btnImage=view.findViewById(R.id.img_btn_image_file_accion);
        btnVideo=view.findViewById(R.id.img_btn_video_file_accion);
        btnSaveIncidencia=view.findViewById(R.id.btn_save_accion);

        btnImage.setOnClickListener(this::capturarImagen);
        btnVideo.setOnClickListener(this::capturarVideo);
        btnSaveIncidencia.setOnClickListener(this::guardarAccion);

        lin=view.findViewById(R.id.opacity_accion_incidencia);

        file.mkdirs();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setUpDagger();
        getDatTipoAccion();
        return view;
    }
    private void setUpDagger(){
        ((BaseApplication)getActivity().getApplication()).getRetrofitComponent().inject(this);
    }

    private void getDatTipoAccion(){
        incidenciaService.getTipoAccion().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<TipoAccionWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }
                            @Override
                            public void onNext(TipoAccionWraper tipoAccionWraper) {

                                tipoAccionList=tipoAccionWraper.getTipoAccionList();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");
                                tipoList= new ArrayList<>();
                                for(TipoAccion ta:tipoAccionList){
                                    tipoList.add(ta.getNombreAccion());
                                }
                                AutoCompleteTextView autoCompleteTextView=getActivity().findViewById(R.id.ac_tipo_accion_incidencia);
                                ArrayAdapter adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.ac_simple_item,tipoList);
                                autoCompleteTextView.setAdapter(adapter);
                                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        idTipoAccion=tipoAccionList.get(position).getIdTipoAccion();
                                    }
                                });
                                autoCompleteTextView.setThreshold(1);
                                //linOpacity.setVisibility(View.GONE);
                            }
                        }
                );
    }
    public void capturarVideo(View view){
        Intent recordVideo= new Intent(getContext(), VideoCaptureActivity.class);
        pathVideo=GlobalVariables.ruta_evidencia + getCodeVideo() + "_test.mp4";
        recordVideo.putExtra("uri_video",pathVideo);
        startActivity(recordVideo);
        /*
        String fileName = GlobalVariables.ruta_evidencia + getCodeVideo() + ".mp4";
        File video = new File( fileName );
        try {
            video.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile( video );
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE);
            videoCapturado=video;
        }

         */
    }
    public void capturarImagen(View v){
        String fileName =GlobalVariables.ruta_evidencia + getCodeImage() + ".jpg";
        File foto = new File( fileName );
        try {
            foto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile( foto );
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            imgCapturada=foto;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Toast.makeText(getContext(),"imagen lista para enviar",Toast.LENGTH_LONG).show();
        }else if(requestCode==REQUEST_VIDEO_CAPTURE &&resultCode==getActivity().RESULT_OK) {
            Toast.makeText(getContext(),"video listo para enviar",Toast.LENGTH_LONG).show();
        }else if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode!=getActivity().RESULT_OK) {
            try {
                if(imgCapturada.delete()){
                    Toast.makeText(getContext(),"Ha cancelo la captura de imagen",Toast.LENGTH_LONG).show();
                    imgCapturada=null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(requestCode==REQUEST_VIDEO_CAPTURE&&resultCode!=getActivity().RESULT_OK) {
            try {
                if(videoCapturado.delete()){
                    Toast.makeText(getContext(),"Ha cancelo la captura de video",Toast.LENGTH_LONG).show();
                    videoCapturado=null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
        }
    }

    public void guardarAccion(View view){
        TextInputEditText tiDescripcion = getView().findViewById(R.id.ti_descripcion_accion);
        if(idIncidencia!=null&& idIncidencia>0 && tiDescripcion.getText().toString().length()>1&&tiDescripcion.getText().toString().length()<=250) {

            Date fecha = new Date();
            SimpleDateFormat sdFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdHora = new SimpleDateFormat("HH:mm:ss");

            MultipartBody.Part videoPart = null;//(videoCapturado!=null)?MultipartBody.Part.createFormData("video", videoCapturado.getName(), RequestBody.create(MediaType.parse("video/*"), videoCapturado)):null;
            MultipartBody.Part imagePart =(imgCapturada!=null)? MultipartBody.Part.createFormData("image", imgCapturada.getName(), RequestBody.create(MediaType.parse("image/*"), imgCapturada)):null;
            if(pathVideo!=null){
                File videoTemp= new File(pathVideo);
                long sizeVideo =videoTemp.getTotalSpace();
                if(sizeVideo>0){
                    videoPart=MultipartBody.Part.createFormData("video",videoTemp.getName(),RequestBody.create(MediaType.parse("video/*"),videoTemp));
                }else{
                    videoPart=null;
                }
            }
            lin.setVisibility(View.VISIBLE);
            incidenciaService
                    .guardarAccionIncidencia(
                            videoPart, imagePart,null,
                            RequestBody.create(MediaType.parse("text/plain"), sdFecha.format(fecha)),
                            RequestBody.create(MediaType.parse("text/plain"), sdHora.format(fecha)),
                            RequestBody.create(MediaType.parse("text/plain"), idIncidencia.toString()),
                            RequestBody.create(MediaType.parse("text/plain"), idTipoAccion.toString()),
                            RequestBody.create(MediaType.parse("text/plain"), tiDescripcion.getText().toString()))
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
                                    Toast.makeText(getContext(), "Ocurrio un error intente denuevo", Toast.LENGTH_LONG);
                                    lin.setVisibility(View.GONE);

                                }

                                @Override
                                public void onComplete() {
                                    Log.d("TAG1", "OnComplete: ");
                                    AutoCompleteTextView acTipoAccion = getActivity().findViewById(R.id.ac_tipo_accion_incidencia);
                                    acTipoAccion.setText("");
                                    tiDescripcion.setText("");
                                    imgCapturada=null;
                                    videoCapturado=null;
                                    Snackbar.make(view, "Acción registrada", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("ok", v1 -> {
                                                getActivity().finish();
                                            })
                                            .show();
                                    lin.setVisibility(View.GONE);

                                }
                            }
                    );
        }else{
            Toast.makeText(getContext(),"Rellene todos los campos requeridos",Toast.LENGTH_LONG).show();
        }
    }
    private String getCodeImage() {
        return "img_"+ new Date().getTime();
    }
    private String getCodeVideo() {
        return "video_"+ new Date().getTime();
    }
}
