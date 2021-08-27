package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class Incidente {

    @SerializedName("tipo")
    private String tipoIncidente;
    @SerializedName("clasificacion")
    private String clasificacion;
    @SerializedName("referencia")
    private String referencia;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("hora")
    private String hora;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("id_incidencia")
    private Integer idIncidencia;
    @SerializedName("fecha_hora")
    private String fechaHora;
    @SerializedName("id_sereno_asignado")
    private Integer idSerenoAsignado;
    @SerializedName("nombres_apellidos")
    private String nombresApellidos;
    @SerializedName("nombre_ciudadano")
    private String nombreCiudadano;
    @SerializedName("telefono_ciudadano")
    private String telefonoCiudadano;
    @SerializedName("id_clasificacion")
    private Integer idClasificacion;
    @SerializedName("id_tipo")
    private Integer idTipo;
    @SerializedName("subtipo")
    private String subtipo;
    @SerializedName("id_subtipo")
    private Integer idSubtipo;
    @SerializedName("interior")
    private String interior;
    @SerializedName("lote")
    private String lote;
    @SerializedName("nro_direccion")
    private Integer nroDireccion;
    @SerializedName("estado")
    private Integer estado;
    @SerializedName("id_usuario_rep")
    private Integer idUsuarioRep;
    @SerializedName("video")
    private Object video;
    @SerializedName("audio")
    private Object audio;
    @SerializedName("image")
    private Object image;

    public Incidente(String tipoIncidente, String clasificacion, String referencia, String fecha, String hora, String descripcion, String direccion, Integer idIncidencia, String fechaHora, Integer idSerenoAsignado, String nombresApellidos, String nombreCiudadano, String telefonoCiudadano, Integer idClasificacion, Integer idTipo, String subtipo, Integer idSubtipo, String interior, String lote, Integer nroDireccion, Integer estado, Integer idUsuarioRep, Object video, Object audio, Object image) {
        this.tipoIncidente = tipoIncidente;
        this.clasificacion = clasificacion;
        this.referencia = referencia;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.idIncidencia = idIncidencia;
        this.fechaHora = fechaHora;
        this.idSerenoAsignado = idSerenoAsignado;
        this.nombresApellidos = nombresApellidos;
        this.nombreCiudadano = nombreCiudadano;
        this.telefonoCiudadano = telefonoCiudadano;
        this.idClasificacion = idClasificacion;
        this.idTipo = idTipo;
        this.subtipo = subtipo;
        this.idSubtipo = idSubtipo;
        this.interior = interior;
        this.lote = lote;
        this.nroDireccion = nroDireccion;
        this.estado = estado;
        this.idUsuarioRep = idUsuarioRep;
        this.video = video;
        this.audio = audio;
        this.image = image;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public void setTipoIncidente(String tipoIncidente) {
        this.tipoIncidente = tipoIncidente;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getIdSerenoAsignado() {
        return idSerenoAsignado;
    }

    public void setIdSerenoAsignado(Integer idSerenoAsignado) {
        this.idSerenoAsignado = idSerenoAsignado;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getNombreCiudadano() {
        return nombreCiudadano;
    }

    public void setNombreCiudadano(String nombreCiudadano) {
        this.nombreCiudadano = nombreCiudadano;
    }

    public String getTelefonoCiudadano() {
        return telefonoCiudadano;
    }

    public void setTelefonoCiudadano(String telefonoCiudadano) {
        this.telefonoCiudadano = telefonoCiudadano;
    }

    public Integer getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(Integer idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }

    public Integer getIdSubtipo() {
        return idSubtipo;
    }

    public void setIdSubtipo(Integer idSubtipo) {
        this.idSubtipo = idSubtipo;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getNroDireccion() {
        return nroDireccion;
    }

    public void setNroDireccion(Integer nroDireccion) {
        this.nroDireccion = nroDireccion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getIdUsuarioRep() {
        return idUsuarioRep;
    }

    public void setIdUsuarioRep(Integer idUsuarioRep) {
        this.idUsuarioRep = idUsuarioRep;
    }

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
    }

    public Object getAudio() {
        return audio;
    }

    public void setAudio(Object audio) {
        this.audio = audio;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Incidente{" +
                "tipoIncidente='" + tipoIncidente + '\'' +
                ", clasificacion='" + clasificacion + '\'' +
                ", referencia='" + referencia + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", idIncidencia=" + idIncidencia +
                ", fechaHora='" + fechaHora + '\'' +
                ", idSerenoAsignado=" + idSerenoAsignado +
                ", nombresApellidos='" + nombresApellidos + '\'' +
                ", nombreCiudadano='" + nombreCiudadano + '\'' +
                ", telefonoCiudadano='" + telefonoCiudadano + '\'' +
                ", idClasificacion=" + idClasificacion +
                ", idTipo=" + idTipo +
                ", subtipo='" + subtipo + '\'' +
                ", idSubtipo=" + idSubtipo +
                ", interior='" + interior + '\'' +
                ", lote='" + lote + '\'' +
                ", nroDireccion=" + nroDireccion +
                ", estado=" + estado +
                ", idUsuarioRep=" + idUsuarioRep +
                ", video=" + video +
                ", audio=" + audio +
                ", image=" + image +
                '}';
    }
}

