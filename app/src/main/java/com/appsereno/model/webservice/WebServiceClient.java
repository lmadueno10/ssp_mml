package com.appsereno.model.webservice;

import com.appsereno.model.SeguridadModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;
/**
 * WebServiceClient is the interface that contains the methods responsible
 * for accessing the data of the different endpoints
 */
public interface WebServiceClient {
    /**
     * This method always returns Observable<SeguridadModel>
     * @return Observable<SeguridadModel>
     */
    @GET("seguridad")
    Observable<SeguridadModel> getSeguridadObservavle();
    @GET()
    Observable<SeguridadModel> getSeguridadObservavle(@Url String url);
}
