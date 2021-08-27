package com.appsereno.viewmodel.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.Login;
import com.appsereno.model.webservice.LoginService;
import com.appsereno.view.LoginActivity;
import com.appsereno.view.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * LoginViewModel is the class that is responsible for receiving the data from the REST API and
 * passing it to the LoginActivity view
 */
public class LoginViewModel extends ViewModel {

    @Inject
    LoginService loginService;
    Disposable disposable;
    private Login login;
    public LoginViewModel(){
    }
    
    public LoginViewModel(BaseApplication app){
        initializeDagger(app);
    }

    private void initializeDagger(BaseApplication app){
        app.getRetrofitComponent().inject(this);
    }

    public void signIn(String usuario, String password, View v, SharedPreferences preferences, LoginActivity loginActivity, boolean keepAlive){

        Observer<Login> observer = new Observer<Login>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                loginActivity.initProgress();
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Login data) {
                login = data;
                Log.d("INFO", "LOGIN: " + data);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG1", "Error: " + e.getMessage());
                loginActivity.endProgress();
                Snackbar.make(v, "ocurrió un error de conexión."+e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("ok", v1 -> {

                        })
                        .show();
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d("TAG1", "OnComplete: ");
                if (login.getCode() == 200 || login.getCode() == 204) {
                    loginActivity.endProgress();
                    Snackbar.make(v, "Usuario o contraseña incorrecto.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ok", v1 -> {

                            })
                            .show();
                } else {/*
                    Snackbar.make(v, "Inicio Session Correcta", Snackbar.LENGTH_LONG)
                            .show();*/
                    if (preferences != null) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("auth_token", login.getAuth_token());
                        editor.putString("refresh_token", login.getRefresh_token());
                        editor.putInt("user_id", login.getUser().getId());
                        editor.putString("user", login.getUser().getUsuario());
                        editor.putString("user_info", login.getUser().getNombresApellidos());
                        editor.putString("dni", login.getUser().getDni());
                        editor.putString("user_code", login.getUser().getCodigoUsuario());
                        editor.putString("sector_id", login.getUser().getSector());
                        editor.putString("celular", login.getUser().getCelular());
                        editor.putString("supervisor", login.getUser().getSupervisor());
                        editor.putInt("id_personal",login.getUser().getIdPersonal());
                        editor.putBoolean("keep_alive", keepAlive);
                        editor.apply();
                        Log.i("LOGIN",login.toString());
                        Intent intent = new Intent(loginActivity, MainActivity.class);
                        loginActivity.startActivity(intent);
                        loginActivity.finish();
                    }
                }
            }
        };
        this.loginService
                .getLoginObservable(usuario,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     *
     * @param user 
     * @param pass
     * @return Observable<Login>
     */
    public Observable<Login> getLogin(String user, String pass){
        return loginService.getLoginObservable(user,pass);
    }
}
