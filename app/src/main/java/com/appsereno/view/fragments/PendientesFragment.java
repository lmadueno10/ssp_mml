package com.appsereno.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.appsereno.R;
import com.appsereno.view.MainActivity;
import com.appsereno.viewmodel.adapter.PendientesAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;


/**
 * PendientesFragment is the class that is bound to the fragment_pendientes.xml view
 */
public class PendientesFragment extends Fragment {
    private Socket socket;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_pendientes,container,false);
        SharedPreferences preferences =getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        Integer idPersonal=preferences.getInt("id_personal",0);
        MainActivity ma= (MainActivity) getActivity();
        if(socket==null){
            socket=ma.getSocket();
            JSONObject obj= new JSONObject();
            try {
                obj.put("id_personal",idPersonal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("pendientes",obj);
        }
        ViewPager2 viewPager2= view.findViewById(R.id.viewpagger);
        viewPager2.setAdapter(new PendientesAdapter(this.getActivity()));
        TabLayout tabLayout=view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout,viewPager2,(tab,pos)->{
            if(pos==0){
                tab.setText("Asignados a mí");
                BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
            }else if(pos==1){
                tab.setText("Reportados por mí");
                BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
            }
        }).attach();

        return view;
    }
}
