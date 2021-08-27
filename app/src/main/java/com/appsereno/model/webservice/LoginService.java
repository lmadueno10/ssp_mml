package com.appsereno.model.webservice;

import com.appsereno.model.SeguridadModel;
import com.appsereno.model.entities.Login;
import com.appsereno.model.entities.Token;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
/**
 * LoginService is the interface that contains the methods responsible
 * for generate credential's
 */
public interface LoginService {

    /**
     * This method always returns Observable<{@link Login}>
     * @return Observable<Login>
     */
    @POST("auth/signin-personal")
    @FormUrlEncoded
    Observable<Login> getLoginObservable(@Field("usuario") String usuario,@Field("password") String password);


    @POST("auth/refresh-token")
    @FormUrlEncoded
    Observable<Token> getTokenObservable(@Field("usuario") String usuario,@Field("password") String password);

}
