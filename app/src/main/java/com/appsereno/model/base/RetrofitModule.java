package com.appsereno.model.base;

import com.appsereno.config.GlobalVariables;
import com.appsereno.model.webservice.IncidenciaService;
import com.appsereno.model.webservice.LoginService;
import com.appsereno.model.webservice.WebServiceClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * is the class in which you configure all the parameters to establish a connection with the rest
 * of the API using retrofit framework.
 */
@Module
public class RetrofitModule {
    /**
     * This method returns an intance of GsonConverterFactory
     * @return GsonConverterFactory
     */
    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory(){
        return GsonConverterFactory.create();
    }

    /**
     * This method returns an instance of HttpLoggingInterceptor
     * @return HttpLoggingInterceptor
     */
    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * This method receives as a parameter and returns OkHttpClient
     * @param loggingInterceptor HttpLoggingInterceptor
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor){
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    /**
     * This method always returns RxJava2CallAdapterFactory
     * @return RxJava2CallAdapterFactory
     */
    @Singleton
    @Provides
    RxJava2CallAdapterFactory providesRxJava2CallAdapterFactory(){
        return RxJava2CallAdapterFactory.create();
    }

    /**
     * This method receives OkHttpClient, GsonConverterFactory, RxJava2CallAdapterFactory as parameters
     * @param client OkHttpClient
     * @param gsonConverterFactory GsonConverterFactory
     * @param rxJava2CallAdapterFactory RxJava2CallAdapterFactory
     * @return Retrofit
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory gsonConverterFactory, RxJava2CallAdapterFactory rxJava2CallAdapterFactory){
        return new Retrofit.Builder()
                .baseUrl(GlobalVariables.getBaseUri())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(client)
                .build();
    }

    /**
     * This method always returns WebServiceClient
     * @param retrofit Retrofit
     * @return WebServiceClient
     */
    @Singleton
    @Provides
    WebServiceClient provideApiClient(Retrofit retrofit){
        return retrofit.create(WebServiceClient.class);
    }

    @Singleton
    @Provides
    LoginService provideApiLogin(Retrofit retrofit){
        return  retrofit.create(LoginService.class);
    }

    /**
     * This method always returns IncidenciaService
     * @param retrofit Retrofit
     * @return IncidenciaService
     */
    @Singleton
    @Provides
    IncidenciaService provideApiIncidencia(Retrofit retrofit){
        return retrofit.create(IncidenciaService.class);
    }
}
