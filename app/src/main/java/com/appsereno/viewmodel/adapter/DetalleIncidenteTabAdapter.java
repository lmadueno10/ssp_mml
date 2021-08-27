package com.appsereno.viewmodel.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.appsereno.view.fragments.DetalleIncidenteAcionesFragment;
import com.appsereno.view.fragments.DetalleIncidenteItemFragment;

public class DetalleIncidenteTabAdapter extends FragmentStateAdapter {


    public DetalleIncidenteTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new DetalleIncidenteItemFragment();
            default:
                return  new DetalleIncidenteAcionesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
