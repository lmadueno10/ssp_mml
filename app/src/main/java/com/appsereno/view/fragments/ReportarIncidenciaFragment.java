package com.appsereno.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.appsereno.R;
import com.appsereno.config.ConfigFtp;
import com.appsereno.helper.RequestHelper;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.ClasificacionIncidencia;
import com.appsereno.model.entities.ClasificacionWraper;
import com.appsereno.model.entities.IncidenciaWraper;
import com.appsereno.model.entities.SubtipoIncidencia;
import com.appsereno.model.entities.SubtipoIncidenciaWraper;
import com.appsereno.model.entities.TipoIncidencia;
import com.appsereno.model.entities.TipoIncidenciaWraper;
import com.appsereno.model.webservice.IncidenciaService;
import com.appsereno.view.GalleryAndCameraActvity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;


import net.gotev.uploadservice.ftp.FTPUploadRequest;
import net.gotev.uploadservice.ftp.UnixPermissions;

import java.io.File;
import java.io.FileNotFoundException;
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
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

import static com.appsereno.helper.HelperFtp.getMediaRealPathFromURI;

/**
 * ReportarIncidenciaFragment is the class that is bound to the fragment_reportar_incidencia.xml view
 */
public class ReportarIncidenciaFragment extends Fragment {

    private final String ruta_evidencia= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/evidencia/";
    private File file = new File(ruta_evidencia);
    private File imgCapturada;
    private File videoCapturado;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton btnImage;
    private ImageButton btnVideo;
    private Button btnSaveIncidencia;
    private LinearLayout linOpacity;
    private Disposable disposable;
    private String pathVideo;
    @Inject
    IncidenciaService incidenciaService;
    private List<ClasificacionIncidencia> clasificacionIncidenciaList;
    private List<TipoIncidencia> tipoIncidenciaList;
    private List<SubtipoIncidencia> subtipoIncidenciaList;
    private List<String> clasificacionList;
    private List<String> tipoList;
    private List<String>  subtipoList;
    private Integer idClasificacion;
    private Integer idTipo;
    private Integer idSubtipo;
    private double lat=0;
    private double lng=0;
    private int REQUEST_CODE_GPS=200;
    private String pathImage;

    private ArrayList selectedData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_reportar_incidencia,container,false);
        clasificacionIncidenciaList= new ArrayList<>();
        btnImage=view.findViewById(R.id.img_btn_image_file);
        btnSaveIncidencia=view.findViewById(R.id.btn_reportar_incidencia);
        btnImage.setOnClickListener(this::selecionarImagen);
        //btnVideo.setOnClickListener(this::capturarVideo);
        btnSaveIncidencia.setOnClickListener(this::guardarInicencia);
        linOpacity=view.findViewById(R.id.opacity_reportar_incidencia);
        setUpDagger();
        fillDataClasificacion();
        file.mkdirs();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requestGPSPermission();
        try {
            requestGPSPosition();
        }catch (Exception ex){}
        return view;
    }

    private String getCodeImage() {
        return "img_"+ new Date().getTime();
    }
    private String getCodeVideo() {
        return "video_"+ new Date().getTime();
    }


    public void requestGPSPermission(){
        int permissionCheck= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_GPS);
            }
        }
    }

    public void requestGPSPosition(){
        LocationManager locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat=location.getLatitude();
                lng=location.getLongitude();
                try {
                    //Toast.makeText(getContext(), "Ubicación registrada.", Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
                }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        int permissionCheck=ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
           Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat=currentLocation.getLatitude();
            lng=currentLocation.getLongitude();
        }

    }

    public void selecionarImagen(View v){
        Toast.makeText(getContext(),"Abriendo galeria fotos y videos",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(),GalleryAndCameraActvity.class);
        startActivityForResult(i,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&resultCode==RESULT_OK) {
            ArrayList<String> sel=data!=null?data.getStringArrayListExtra("result"):new ArrayList<>();
            if(sel!=null&&sel.size()>0){
                selectedData=sel;
                Toast.makeText(getContext(),selectedData.size()+" archivos selecionados",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fillDataClasificacion(){
        linOpacity.setVisibility(View.VISIBLE);
        incidenciaService
                .getClasificacionIncidencia()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<ClasificacionWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }
                            @Override
                            public void onNext(ClasificacionWraper clasificacionWraper) {

                               clasificacionIncidenciaList=clasificacionWraper.getData();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                                Toast.makeText(getContext(),"Error al cargar datos",Toast.LENGTH_LONG).show();
                                linOpacity.setVisibility(View.GONE);
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");
                                clasificacionList= new ArrayList<>();
                                for(ClasificacionIncidencia ci:clasificacionIncidenciaList){
                                    clasificacionList.add(ci.getValor());
                                }
                                AutoCompleteTextView autoCompleteTextView=getActivity().findViewById(R.id.ac_clasificacion_incidencia);
                                Log.i("CLASIFICACION",clasificacionIncidenciaList.toString());
                                ArrayAdapter adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.ac_simple_item,clasificacionList);
                                autoCompleteTextView.setAdapter(adapter);
                                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        idClasificacion=clasificacionIncidenciaList.get(position).getMultitablaId();
                                        AutoCompleteTextView acSubtipo=getActivity().findViewById(R.id.ac_subtipo_incidencia);
                                        acSubtipo.setText("");
                                        AutoCompleteTextView acTipo=getActivity().findViewById(R.id.ac_tipo_incidencia);
                                        acTipo.setText("");
                                        idTipo=null;
                                        idSubtipo=null;
                                        fillDataTipoIncidencia(idClasificacion);
                                    }
                                });
                                autoCompleteTextView.setThreshold(1);
                                linOpacity.setVisibility(View.GONE);
                            }
                        }
                );

    }

    private void setUpDagger(){
        ((BaseApplication)getActivity().getApplication()).getRetrofitComponent().inject(this);
    }


    public void fillDataTipoIncidencia(Integer id){
        linOpacity.setVisibility(View.VISIBLE);
        incidenciaService
                .getTipoIncidenciaByIdClas(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<TipoIncidenciaWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }
                            @Override
                            public void onNext(TipoIncidenciaWraper tipoIncidenciaWraper) {

                                tipoIncidenciaList=tipoIncidenciaWraper.getData();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");
                                tipoList= new ArrayList<>();
                                for(TipoIncidencia ti:tipoIncidenciaList){
                                    tipoList.add(ti.getValor());
                                }
                                AutoCompleteTextView autoCompleteTextView=getActivity().findViewById(R.id.ac_tipo_incidencia);
                                Log.i("CLASIFICACION",clasificacionIncidenciaList.toString());
                                ArrayAdapter adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.ac_simple_item,tipoList);
                                autoCompleteTextView.setAdapter(adapter);
                                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        idTipo=tipoIncidenciaList.get(position).getMultitablaId();
                                        fillDataSubtipoIncidencia(idTipo);
                                    }
                                });
                                autoCompleteTextView.setThreshold(1);
                                AutoCompleteTextView scSubtipo=getActivity().findViewById(R.id.ac_subtipo_incidencia);
                                scSubtipo.setText("");
                                idSubtipo=null;
                                linOpacity.setVisibility(View.GONE);
                            }
                        }
                );
    }

    public void fillDataSubtipoIncidencia(Integer id){
        linOpacity.setVisibility(View.VISIBLE);
        incidenciaService
                .getSubtitpoIncidencia(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<SubtipoIncidenciaWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }
                            @Override
                            public void onNext(SubtipoIncidenciaWraper subtipoIncidenciaWraper) {

                                subtipoIncidenciaList=subtipoIncidenciaWraper.getData();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");
                                subtipoList= new ArrayList<>();
                                for(SubtipoIncidencia si:subtipoIncidenciaList){
                                    subtipoList.add(si.getValor());
                                }
                                AutoCompleteTextView autoCompleteTextView=getActivity().findViewById(R.id.ac_subtipo_incidencia);
                                ArrayAdapter adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.ac_simple_item,subtipoList);
                                autoCompleteTextView.setAdapter(adapter);
                                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        idSubtipo=subtipoIncidenciaList.get(position).getMultitablaId();
                                    }
                                });
                                autoCompleteTextView.setThreshold(1);
                                linOpacity.setVisibility(View.GONE);
                            }
                        }
                );
    }

    public void guardarInicencia(View view){
            TextInputEditText tiDescripcion = getView().findViewById(R.id.ta_descripcion_incidencia);
            if(idClasificacion!=null&& idTipo!=null&&tiDescripcion.getText().toString().length()>1&&tiDescripcion.getText().toString().length()<=250) {
                SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                Integer idPersonal = preferences.getInt("id_personal", 0);
                String ciudadanoInfo = preferences.getString("user_info", "-");
                String telefonoCiudadano = preferences.getString("celular", "-");

                Date fecha = new Date();
                SimpleDateFormat sdFecha = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdHora = new SimpleDateFormat("HH:mm:ss");
                String lati=lat!=0?lat+"":"";
                String longi=lng!=0?lng+"":"";
                ArrayList<String> lstVideo= new ArrayList<>();
                ArrayList<String> lstImage = new ArrayList<>();
                ArrayList<String> lstPathMedia= new ArrayList<>();
                if(selectedData!=null&&selectedData.size()>0){
                    for (Object obj:selectedData) {
                        Uri u=Uri.parse(obj.toString());
                        //String tmp= obj.toString().split("/")[obj.toString().split("/").length-1];
                        String tmpParth=getMediaRealPathFromURI(getActivity(),u);
                        String tmp=idPersonal+"_"+ tmpParth.split("/")[tmpParth.split("/").length-1];
                        if(tmp.contains("mp4")){
                            lstVideo.add(tmp);
                        }else{
                            lstImage.add(tmp);
                        }
                        lstPathMedia.add(tmpParth);
                    }
                }
                String tmpImage="";
                String tmpVideo="";
                for (String item:lstVideo) {
                    tmpVideo+=item+"|";
                }
                for (String item:lstImage) {
                    tmpImage+=item+"|";
                }
                if(lstPathMedia!=null&&lstPathMedia.size()>0){
                    for (String p:lstPathMedia) {
                        try {
                            if(ConfigFtp.isLocal){
                                FTPUploadRequest ftp= new FTPUploadRequest(getContext(), ConfigFtp.LOCAL_SERVER, 21).useSSL(false);
                                File f = new File(p);
                                String remoteName=idPersonal+"_"+f.getName();
                                ftp.setUsernameAndPassword(ConfigFtp.LOCAL_USER, ConfigFtp.LOCAL_PASSWORD)
                                        .addFileToUpload(p, "/files/"+remoteName)
                                        .subscribe(getContext(),getActivity(), new RequestHelper(getContext(),remoteName,fecha));
                            }else{
                                FTPUploadRequest ftp= new FTPUploadRequest(getContext(), ConfigFtp.SERVER, 21).useSSL(false);
                                File f = new File(p);
                                String remoteName=idPersonal+"_"+f.getName();
                                UnixPermissions up= new UnixPermissions();
                                ftp.setUsernameAndPassword(ConfigFtp.USER, ConfigFtp.PASSWORD)
                                        .addFileToUpload(p, "/"+remoteName,up)
                                        .subscribe(getContext(),getActivity(), new RequestHelper(getContext(),remoteName,fecha));
                            }

                                   // ftp.startUpload();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                linOpacity.setVisibility(View.VISIBLE);
                incidenciaService
                        .reportarIncidencia(
                                (tmpVideo!=null&&tmpVideo.trim().length()>0)?RequestBody.create(MediaType.parse("text/plain"), tmpVideo):null,
                                (tmpImage!=null&&tmpImage.trim().length()>0)?RequestBody.create(MediaType.parse("text/plain"), tmpImage):null,
                                null,
                                RequestBody.create(MediaType.parse("text/plain"), sdFecha.format(fecha)),
                                RequestBody.create(MediaType.parse("text/plain"), sdHora.format(fecha)),
                                RequestBody.create(MediaType.parse("text/plain"), idPersonal.toString()),
                                RequestBody.create(MediaType.parse("text/plain"), ciudadanoInfo),
                                RequestBody.create(MediaType.parse("text/plain"), idClasificacion.toString()),
                                RequestBody.create(MediaType.parse("text/plain"), idTipo.toString()),
                                (idSubtipo!=null)?RequestBody.create(MediaType.parse("text/plain"),idSubtipo.toString()):null,
                                null, null, null,
                                RequestBody.create(MediaType.parse("text/plain"), tiDescripcion.getText().toString()),
                                null,
                                RequestBody.create(MediaType.parse("text/plain"), telefonoCiudadano),
                                RequestBody.create(MediaType.parse("text/plain"), "-"),
                                RequestBody.create(MediaType.parse("text/plain"), idPersonal.toString()),
                                (lati.length()>0)?RequestBody.create(MediaType.parse("text/plain"),lati):null,
                                (longi.length()>0)?RequestBody.create(MediaType.parse("text/plain"),longi):null)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Observer<IncidenciaWraper>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        disposable = d;
                                    }

                                    @Override
                                    public void onNext(IncidenciaWraper subtipoIncidenciaWraper) {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("TAG1", "Error: " + e.getMessage());
                                        Toast.makeText(getContext(), "Ocurrio un error intente denuevo", Toast.LENGTH_LONG).show();
                                        linOpacity.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("TAG1", "OnComplete: ");
                                        AutoCompleteTextView acClasificacion = getActivity().findViewById(R.id.ac_clasificacion_incidencia);
                                        acClasificacion.setText("");
                                        AutoCompleteTextView acTipo = getActivity().findViewById(R.id.ac_tipo_incidencia);
                                        acTipo.setText("");
                                        AutoCompleteTextView acSubtipo = getActivity().findViewById(R.id.ac_subtipo_incidencia);
                                        acSubtipo.setText("");
                                        tiDescripcion.setText("");
                                        imgCapturada=null;
                                        videoCapturado=null;
                                        linOpacity.setVisibility(View.GONE);
                                        Snackbar.make(view, "Incidencia reportada", Snackbar.LENGTH_INDEFINITE)
                                                .setAction("ok", v1 -> {

                                                })
                                                .show();
                                    }
                                }
                        );
            }else{
                Toast.makeText(getContext(),"Rellene todos los campos requeridos",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    public void setSelectedData(ArrayList list){
        this.selectedData=list;
    }
}
