package com.appsereno.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsereno.R;
import com.appsereno.model.SeguridadModel;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.IncidenciaWraper;
import com.appsereno.model.webservice.IncidenciaService;
import com.appsereno.viewmodel.adapter.DetalleIncidenteAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetalleIncidenteFragment extends Fragment {
    private DetalleIncidenteAdapter adapter;
    private RecyclerView recyclerView;
    private Disposable disposable;
    @Inject
    IncidenciaService incidenciaService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_asignados,container,false);
        adapter = new DetalleIncidenteAdapter();
        recyclerView = view.findViewById(R.id.asignados_recycler);
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lim);
        recyclerView.setAdapter(adapter);
        setUpDagger();
        getData();
        return view;
    }

    /**
     * This method is responsible for capturing the retrofit component and performing
     * the injection
     */
    private void setUpDagger(){
        ((BaseApplication)getActivity().getApplication()).getRetrofitComponent().inject(this);
    }

    /**
     * This method makes a request for data
     */
    private void getData(){
        SharedPreferences preferences =this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        int idPersonal = preferences.getInt("id_personal",0);
        incidenciaService
                .getIncidentsByPersonal(idPersonal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<IncidenciaWraper>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }
                            @Override
                            public void onNext(IncidenciaWraper data) {
                                TabLayout tabLayout=getActivity().findViewById(R.id.tabLayout);
                                if(tabLayout!=null){
                                    TabLayout.Tab tb= tabLayout.getTabAt(0);
                                    BadgeDrawable bd=tb.getBadge();
                                    bd.setNumber(data.getIncidencias().size());
                                }
                                adapter.setData(data.getIncidencias());

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");

                            }
                        }
                );
    }



}
