package com.appsereno;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appsereno.model.SeguridadModel;
import com.appsereno.model.base.DaggerRetrofitComponent;
import com.appsereno.model.base.RetrofitComponent;
import com.appsereno.model.base.RetrofitModule;
import com.appsereno.view.SeguridadActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@RunWith(MockitoJUnitRunner.class)
public class SeguridadTesting {
    @Mock
    RetrofitComponent retrofitComponent;
    @Mock
    Disposable disposable;
    @Mock
    SeguridadActivity seguridadActivity;
    @Before
    public void inizialice(){
        retrofitComponent = DaggerRetrofitComponent
                .builder()
                .retrofitModule(new RetrofitModule())
                .build();
        seguridadActivity=Mockito.mock(SeguridadActivity.class);
    }

    @Test
    public void testingObservable() throws Exception{
        createObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread()).subscribe(createObserver());
        disposable.dispose();
    }



    @Test
    public void testinInjection() throws  Exception{
        retrofitComponent.inject(seguridadActivity);
    }

    private Observable createObservable(){
        return Observable.create(new ObservableOnSubscribe<SeguridadModel>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SeguridadModel> emitter) throws Exception {
                emitter.onNext(new SeguridadModel());
            }
        });
    }

    private Observer createObserver(){
        return  new Observer<SeguridadModel> () {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(@NonNull SeguridadModel data) {
                Log.d("TEST1","onNext "+data+" Hilo "+Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("TEST1","onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("TEST1","onNext "+" Hilo "+Thread.currentThread().getName());
            }
        };
    }
}
