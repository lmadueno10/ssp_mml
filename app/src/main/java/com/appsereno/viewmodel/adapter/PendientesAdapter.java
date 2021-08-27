package com.appsereno.viewmodel.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.appsereno.view.fragments.DetalleIncidenteFragment;
import com.appsereno.view.fragments.IncidentesReportadosPorMIFragment;

public class PendientesAdapter extends FragmentStateAdapter {

    public PendientesAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new DetalleIncidenteFragment();
            default:
                return  new IncidentesReportadosPorMIFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
