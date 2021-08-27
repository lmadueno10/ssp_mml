package com.appsereno.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import com.appsereno.viewmodel.adapter.SeguridadAdapter;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.SeguridadModel;
import com.appsereno.model.webservice.WebServiceClient;
import com.appsereno.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * SeguridadActivity is the class that is bound to the activity_seguridad.xml view
 */
public class SeguridadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SeguridadAdapter adapter;
    private SeguridadModel seguridadModel;

    private Disposable disposable;

    @Inject
    WebServiceClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);
        setUpDagger();
        setUpView();
        getData();
    }

    /**
     * This method is responsible for capturing the retrofit component and performing
     * the injection in seguridadActivity
     */
    private void setUpDagger(){
        ((BaseApplication)getApplication()).getRetrofitComponent().inject(this);
    }

    /**
     * This method makes a request for data
     */
    private void getData(){

        client
                .getSeguridadObservavle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<SeguridadModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(SeguridadModel data) {
                                Snackbar.make(adapter.getView(),"Loading...",Snackbar.LENGTH_LONG)
                                    .show();
                                adapter.setData(data);

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("TAG1", "Error: " + e.getMessage());
                                Snackbar.make(adapter.getView(),"Error connection",Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Return", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        }).show();
                            }

                            @Override
                            public void onComplete() {
                                Log.d("TAG1", "OnComplete: ");

                            }
                        }
                );
    }

    /**
     * This method setting the data to UI
     */
    private void setUpView(){
        seguridadModel = new SeguridadModel();
        adapter = new SeguridadAdapter(seguridadModel);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lim);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    public void logout(MenuItem item) {
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("auth_token",null);
        editor.putString("refresh_token",null);
        editor.apply();
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}