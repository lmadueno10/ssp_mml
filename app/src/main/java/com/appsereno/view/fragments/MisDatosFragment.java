package com.appsereno.view.fragments;

import android.annotation.SuppressLint;
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
import com.google.android.material.textfield.TextInputEditText;

/**
 * MisDatosFragment is the class that is bound to the fragment_misdatos.xml view
 */
public class MisDatosFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mis_datos,container,false);
        fillData(v);
        return v;
    }

    @SuppressLint("SetTextI18n")
    private void fillData(View v){
        try {
            SharedPreferences preferences =this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

            String dni = preferences.getString("dni","-");
            String userCode = preferences.getString("user_code", "-");
            String userName = preferences.getString("user_info", "-");
            String sectorId=preferences.getString("sector_id","-");
            String celular=preferences.getString("celular","-");
            String supervisor = preferences.getString("supervisor","-");
            TextInputEditText txtUserId=v.findViewById(R.id.user_code);
            TextInputEditText txtUserInfo=v.findViewById(R.id.user_info);
            TextInputEditText txtSectorId=v.findViewById(R.id.user_sector);
            TextInputEditText txtCelular=v.findViewById(R.id.user_cel);
            TextInputEditText txtSupervisor=v.findViewById(R.id.user_sup);
            TextInputEditText txtDni=v.findViewById(R.id.user_dni);

            txtUserId.setText(userCode);
            txtUserInfo.setText(userName);
            txtSectorId.setText(sectorId+"");
            txtCelular.setText(celular);
            txtSupervisor.setText(supervisor);
            txtDni.setText(dni);

        }catch (Exception e){
            Log.d("ERROR:",e.getMessage());
            e.printStackTrace();
        }
    }
}
