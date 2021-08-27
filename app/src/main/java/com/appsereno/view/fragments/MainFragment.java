package com.appsereno.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsereno.R;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.Dashboard;
import com.appsereno.model.entities.TipoAccion;
import com.appsereno.model.entities.TipoAccionWraper;
import com.appsereno.model.webservice.IncidenciaService;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * MainFragment is the class that is bound to the fragment_main.xml view
 */
public class MainFragment extends Fragment{

    @Inject
    IncidenciaService service;
    private Disposable disposable;
    private Dashboard dashboard;

    TextView cantIncHoy;
    TextView cantIncUltHora;
    TextView cantIncUltSieteDias;
    TextView cantIncAtend;
    TextView cantIncPend;
    TextView promIncAtendHora;
    TextView cateIncMasRep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main,container,false);
        cantIncHoy=v.findViewById(R.id.incidencias_hoy_dash);
        cantIncUltHora=v.findViewById(R.id.incidencias_ultima_hora_dash);
        cantIncUltSieteDias=v.findViewById(R.id.incidencias_ultimos_siete_dash);
        cantIncAtend=v.findViewById(R.id.incidencias_atendidas_dash);
        cantIncPend=v.findViewById(R.id.incidencias_pendientes_dash);
        promIncAtendHora=v.findViewById(R.id.promedio_incidencias_por_hora_dash);
        cateIncMasRep=v.findViewById(R.id.categoria_incidencia_mas_rep_dash);
        setUpDagger();
        fillData();
        return v;
    }
    private void setUpDagger(){
        ((BaseApplication)getActivity().getApplication()).getRetrofitComponent().inject(this);
    }
    private Context getActualContex(){
        return getContext();
    }
    private void fillData(){
        service.getDataDashboard().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<Dashboard>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }
                    @Override
                    public void onNext(Dashboard obj) {

                        dashboard=obj;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG1", "Error: " + e.getMessage());
                        Toast.makeText(getActualContex(), "Verifique su conexión a internet",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG1", "OnComplete: ");
                        cantIncHoy.setText(dashboard.getData().getCantIncidenciaHoy());
                        cantIncUltHora.setText(dashboard.getData().getCantIncidenciaUltimaHora());
                        cantIncUltSieteDias.setText(dashboard.getData().getCantIncidenciaUtimosSieteDias());
                        cantIncAtend.setText(dashboard.getData().getCantIncidenciaAtendidas());
                        cantIncPend.setText(dashboard.getData().getCantIncidenciaPendientes());
                        promIncAtendHora.setText(dashboard.getData().getPromIncidenciaAtendidaHora());
                        cateIncMasRep.setText(dashboard.getData().getCateIncidenciaMasReportada());
                    }
                }
        );
    }
}
