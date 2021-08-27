package com.appsereno.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.appsereno.R;
import com.appsereno.viewmodel.adapter.DetalleIncidenteTabAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetalleIncidenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidente);
        ViewPager2 viewPager2= findViewById(R.id.viewpagger_detalle_incidente);
        viewPager2.setAdapter(new DetalleIncidenteTabAdapter(this));
        TabLayout tabLayout=findViewById(R.id.tabLayout_detalle_incidente);

        new TabLayoutMediator(tabLayout,viewPager2,(tab, pos)->{
            if(pos==0){
                tab.setText("Detalle");
            }else if(pos==1){
               tab.setText("Acciones");
            }
        }).attach();
        MaterialToolbar materialToolbar = findViewById(R.id.app_main_menu);
        materialToolbar.setTitle("Detalle de incidente");
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setData();

       // btn.setOnClickListener(this::onClick);
    }

    private void setData(){
        Integer idIncidencia = getIntent().getIntExtra("id_incidencia",0);
        String reporterName = getIntent().getStringExtra("reporter_name");
        String reporterDni= getIntent().getStringExtra("reporter_dni");
        String incidentType= getIntent().getStringExtra("incident_type");
        String incidentDate= getIntent().getStringExtra("incident_datetime");
        String incidentDescription= getIntent().getStringExtra("incident_description");
        String clasIncidente=getIntent().getStringExtra("clasi_incidente");
        String subTipoIncidente=getIntent().getStringExtra("subtipo_incidente");
        String dirIncidente=getIntent().getStringExtra("dir_incidente");
        String refIncidente=getIntent().getStringExtra("ref_incidente");
        String nroDir=getIntent().getStringExtra("nro_dir");
        String interior=getIntent().getStringExtra("interior");
        String lote=getIntent().getStringExtra("lote");

        SharedPreferences preferences=getSharedPreferences("detalle_incidente", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id_incidencia", idIncidencia);
        editor.putString("reporterName", reporterName);
        editor.putString("reporterDni",reporterDni);
        editor.putString("incidentType", incidentType);
        editor.putString("incidentDate", incidentDate);
        editor.putString("clasIncidente",clasIncidente);
        editor.putString("subTipoIncidente",subTipoIncidente);
        editor.putString("dirIncidente",dirIncidente);
        editor.putString("refIncidente",refIncidente);
        editor.putString("incidentDescription",incidentDescription);
        editor.putString("nro_dir",nroDir);
        editor.putString("interior",interior);
        editor.putString("lote",lote);



        editor.apply();
    }

    public void onClick(View v){
        Intent intent= new Intent(this,UbicacionMapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}