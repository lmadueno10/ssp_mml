package com.appsereno.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appsereno.R;

public class DetalleIncidenteItemFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_detalle_incidente_item,container,false);
        setData(view);

        return view;
    }

    private void setData(View v){
        SharedPreferences preferences = getContext().getSharedPreferences("detalle_incidente", Context.MODE_PRIVATE);
        String reporterName = preferences.getString("reporterName", "-");
        String reporterDni = preferences.getString("reporterDni", "-");
        String incidentType = preferences.getString("incidentType", "-");
        String incidentDate = preferences.getString("incidentDate", "-");
        String incidentDescription = preferences.getString("incidentDescription", "-");
        String reporterDNI = preferences.getString("reporterDNI", "-");
        String clasIncidente = preferences.getString("clasIncidente", "-");
        String subTipoIncidente = preferences.getString("subTipoIncidente", "-");
        String dirIncidente = preferences.getString("dirIncidente", "-");
        String refIncidente = preferences.getString("refIncidente", "-");
        String nroDir = preferences.getString("nro_dir", "-");
        String intDir = preferences.getString("interior", "-");
        String loteDir = preferences.getString("lote", "-");


        TextView txtReporterName=v.findViewById(R.id.ciudadano_name);
        TextView txtIncidentDescription=v.findViewById(R.id.descripcion_incidente);
        TextView txtClasIncidente=v.findViewById(R.id.clasificacion_incidente);
        TextView txtIncidentType=v.findViewById(R.id.tipo_incidente);
        TextView txtSubTipoIncidente=v.findViewById(R.id.subtipo_incidente);
        TextView txtDirIncidente=v.findViewById(R.id.direccion_incidente);
        TextView txtRefIncidente=v.findViewById(R.id.ref_dir_incidente);
        TextView txtNroDir=v.findViewById(R.id.nro_dir_incidente);
        TextView txtIntDir=v.findViewById(R.id.interior_dir_incidente);
        TextView txtLoteDir=v.findViewById(R.id.lote_dir_incidente);

         txtReporterName.setText(reporterName);
         txtIncidentDescription.setText(incidentDescription);
         txtClasIncidente.setText(clasIncidente);
         txtIncidentType.setText(incidentType);
         txtSubTipoIncidente.setText(subTipoIncidente);
         txtDirIncidente.setText(dirIncidente);
        txtRefIncidente.setText(refIncidente);
         txtIntDir.setText(intDir);
         txtNroDir.setText(nroDir);
         txtLoteDir.setText(loteDir);
    }
}
