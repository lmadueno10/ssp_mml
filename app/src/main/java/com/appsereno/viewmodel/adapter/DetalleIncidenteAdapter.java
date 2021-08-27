package com.appsereno.viewmodel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsereno.R;
import com.appsereno.model.entities.Incidente;
import com.appsereno.model.entities.Reportador;
import com.appsereno.view.DetalleIncidenteActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DetalleIncidenteAdapter extends RecyclerView.Adapter<DetalleIncidenteAdapter.DetalleIncidenteAdapterHolder>  {

    private List<Incidente> incidentes;
    public DetalleIncidenteAdapter(){
        if(incidentes==null){
            incidentes= new ArrayList<>();
        }
        /*
        incidentes= new ArrayList<>();
        incidentes.add(new Incidente("i089","Accidente de tránsito","Accidente","Choque","Plaza de armas - Cajamarca","2021-04-08","10:30 am","Accidente de tránsito","Jr. Dos de Mayo 567 - Cajamarca",new Reportador("Gustavo reyes","45346787")));
        incidentes.add(new Incidente("i090","Alerta SOS","Alerta","Alerta por bandalismo","Mercado","2021-04-08","10:35 am","Alerta SOS","Jr. Miguel Iglesias 34 - Cajamarca",new Reportador("Gustavo reyes","45346787")));
        incidentes.add(new Incidente("i091","Accidente de tránsito","Accidente","Choque con heridos","Ovalo El Inca","2021-04-08","10:40 am","Accidente de tránsito","Av. El Maestro 432 - Cajamarca",new Reportador("Gustavo reyes","45346787")));
        incidentes.add(new Incidente("i093","Accidente de tránsito","Accidente","Choque sin heridos","Plaza de armas - Cajamarca","2021-04-08","11:00 am","Accidente de tránsito","Jr. El Batan 456 - Cajamarca",new Reportador("Gustavo reyes","45346787")));
        incidentes.add(new Incidente("i094","Accidente de tránsito","Accidente","Choque","","2021-04-08","11:13 am","Accidente de tránsito","Jr. Amalia puga 453 - Cajamarca",new Reportador("Gustavo reyes","45346787")));
        incidentes.add(new Incidente("i095","Accidente de tránsito","Accidente","Choque moto","","2021-04-08","11:45 am","Accidente de tránsito","Jr. Dos de Mayo 567 - Cajamarca",new Reportador("Gustavo reyes","45346787")));

         */
    }

    @NonNull
    @Override
    public DetalleIncidenteAdapter.DetalleIncidenteAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asignados_response, parent, false);

        return new DetalleIncidenteAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DetalleIncidenteAdapter.DetalleIncidenteAdapterHolder holder, int position) {
        holder.getTxtDetail().setText(incidentes.get(position).getDescripcion());
        holder.getTxtAddress().setText(incidentes.get(position).getDireccion());
        holder.getTxtReporter().setText(incidentes.get(position).getNombreCiudadano());
        holder.getBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.getContext(), DetalleIncidenteActivity.class);
                intent.putExtra("id_incidencia",incidentes.get(position).getIdIncidencia()!=null?incidentes.get(position).getIdIncidencia():"");
                intent.putExtra("reporter_name",incidentes.get(position).getNombreCiudadano()!=null?incidentes.get(position).getNombreCiudadano():"");
                intent.putExtra("clasi_incidente",incidentes.get(position).getClasificacion()!=null?incidentes.get(position).getClasificacion():"");
                intent.putExtra("subtipo_incidente",incidentes.get(position).getSubtipo()!=null?incidentes.get(position).getSubtipo():"");
                intent.putExtra("incident_type",incidentes.get(position).getTipoIncidente()!=null?incidentes.get(position).getTipoIncidente():"");
                intent.putExtra("dir_incidente",incidentes.get(position).getDireccion()!=null?incidentes.get(position).getDireccion():"");
                intent.putExtra("ref_incidente",incidentes.get(position).getReferencia()!=null?incidentes.get(position).getReferencia():"");
                intent.putExtra("incident_datetime",incidentes.get(position).getFechaHora()!=null?incidentes.get(position).getFechaHora():"");
                intent.putExtra("incident_description",incidentes.get(position).getDescripcion()!=null?incidentes.get(position).getDescripcion():"");
                intent.putExtra("nro_dir",incidentes.get(position).getNroDireccion()!=null?incidentes.get(position).getNroDireccion().toString():"");
                intent.putExtra("interior",incidentes.get(position).getInterior()!=null?incidentes.get(position).getInterior():"");
                intent.putExtra("lote",incidentes.get(position).getLote()!=null?incidentes.get(position).getLote():"");
                holder.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidentes.size();
    }

    public void setData(List<Incidente> incidentes){
        this.incidentes=incidentes;
        notifyDataSetChanged();
    }
    public static class DetalleIncidenteAdapterHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final TextView txtDetail;
        private final TextView txtAddress;
        private final TextView txtReporter;
        private final ImageButton btn;

        public DetalleIncidenteAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txtDetail = itemView.findViewById(R.id.incident_description);
            txtAddress=itemView.findViewById(R.id.incident_address);
            txtReporter=itemView.findViewById(R.id.ciudadano);
            btn = itemView.findViewById(R.id.btn_see);
            context=itemView.getContext();
        }


        public TextView getTxtDetail() {
            return txtDetail;
        }

        public TextView getTxtAddress() {
            return txtAddress;
        }

        public TextView getTxtReporter(){return txtReporter;}

        public ImageButton getBtn(){return btn;}

        public Context getContext(){return context;}

    }
}
