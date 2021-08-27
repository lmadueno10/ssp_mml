package com.appsereno.model.webservice;

import com.appsereno.model.entities.ClasificacionWraper;
import com.appsereno.model.entities.Dashboard;
import com.appsereno.model.entities.DefaultWraper;
import com.appsereno.model.entities.IncidenciaWraper;
import com.appsereno.model.entities.Login;
import com.appsereno.model.entities.SubtipoIncidenciaWraper;
import com.appsereno.model.entities.TipoAccionWraper;
import com.appsereno.model.entities.TipoIncidenciaWraper;
import com.appsereno.model.entities.Token;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * IncidenciaService is the interface
 *
 */
public interface IncidenciaService {

    /**
     * This method always returns Observable<{@link IncidenciaWraper}>
     * @return Observable<IncidenciaWraper>
     */
    @GET("incidencias/sereno/{id}")
    Observable<IncidenciaWraper> getIncidentsByPersonal(@Path("id") Integer id);


    @GET("incidencias/usuario/{id}")
    Observable<IncidenciaWraper> getIncidentsByUsuer(@Path("id") Integer id);

    /**
     * This method always returns Observable<{@link ClasificacionWraper}>
     * @return Observable<ClasificacionWraper>
     */
    @GET("clasificacion")
    Observable<ClasificacionWraper> getClasificacionIncidencia();

    /**
     * This method always returns Observable<{@link TipoIncidenciaWraper}>
     * @return Observable<TipoIncidenciaWraper>
     */
    @GET("tipo/clasificacion/{id}")
    Observable<TipoIncidenciaWraper> getTipoIncidenciaByIdClas(@Path("id") Integer id);

    /**
     *
     * This method always returns Observable<{@link TipoIncidenciaWraper}>
     * @return Observable<TipoIncidenciaWraper>
     */
    @GET("subtipo/tipo/{id}")
    Observable<SubtipoIncidenciaWraper> getSubtitpoIncidencia(@Path("id") Integer id);

    /**
     * This method always returns Observable<{@link ClasificacionWraper}>
     * @return Observable<ClasificacionWraper>
     */
    @GET("tipo-accion")
    Observable<TipoAccionWraper> getTipoAccion();

    /**
     * This method always returns Observable<{@link Dashboard}>
     * @return Observable<Dashboard>
     */
    @GET("incidencias/dashboard/count")
    Observable<Dashboard> getDataDashboard();

    /**
     * This method always returns Observable<{@link IncidenciaWraper}>
     * @return Observable<IncidenciaWraper>
     */
    @POST("incidencias/sereno")
    @Multipart
    Observable<IncidenciaWraper> reportarIncidencia(
            @Part("video") RequestBody video,
            @Part("image") RequestBody image,
            //@Part MultipartBody.Part vide,
            //@Part MultipartBody.Part image,
            @Part MultipartBody.Part audio,
            @Part("fecha") RequestBody fecha,
            @Part("hora") RequestBody hora,
            @Part("id_sereno_asignado") RequestBody idSereno,
            @Part("nombre_ciudadano") RequestBody nombreCiudadano,
            @Part("id_clasificacion") RequestBody idClasificacion,
            @Part("id_tipo") RequestBody idTipo,
            @Part("id_subtipo") RequestBody idSubtipo,
            @Part("interior") RequestBody interior,
            @Part("lote") RequestBody lote,
            @Part("referencia") RequestBody referencia,
            @Part("descripcion") RequestBody descripcion,
            @Part("nro_direccion") RequestBody nroDireccion,
            @Part("telefono_ciudadano") RequestBody telefonoCiudadano,
            @Part("direccion") RequestBody direccion,
            @Part("id_usuario_rep") RequestBody idUsuarioRep,
            @Part("lat") RequestBody lat,
            @Part("lng") RequestBody lng
            );

    /**
     * This method always returns Observable<{@link IncidenciaWraper}>
     * @return Observable<IncidenciaWraper>
     */
    @POST("accion-incidencia")
    @Multipart
    Observable<DefaultWraper> guardarAccionIncidencia(
            @Part MultipartBody.Part vide,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part audio,
            @Part("fecha") RequestBody fecha,
            @Part("hora") RequestBody hora,
            @Part("id_incidencia") RequestBody idIncidencia,
            @Part("id_tipo_accion") RequestBody idTipoAccion,
            @Part("descripcion") RequestBody descripcion
    );
    /**
     * This method always returns Observable<{@link IncidenciaWraper}>
     * @return Observable<DefaultWraper>
     */
    @POST("video")
    @Multipart
    Observable<DefaultWraper> guardarVideo(
            @Part MultipartBody.Part video,
            @Part("fecha") RequestBody fecha,
            @Part("hora") RequestBody hora,
            @Part("id_personal") RequestBody idIncidencia
    );
}
