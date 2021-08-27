package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class TipoIncidencia {
    @SerializedName("multitabla_id")
    private Integer multitablaId;
    @SerializedName("tabla")
    private String tabla;
    @SerializedName("sigla")
    private Object sigla;
    @SerializedName("valor")
    private String valor;
    @SerializedName("padre_id")
    private Integer padreId;
    @SerializedName("estado")
    private String estado;
    @SerializedName("clasificacion")
    private String clasificacion;

    public TipoIncidencia(Integer multitablaId, String tabla, Object sigla, String valor, Integer padreId, String estado, String clasificacion) {
        this.multitablaId = multitablaId;
        this.tabla = tabla;
        this.sigla = sigla;
        this.valor = valor;
        this.padreId = padreId;
        this.estado = estado;
        this.clasificacion = clasificacion;
    }

    public Integer getMultitablaId() {
        return multitablaId;
    }

    public void setMultitablaId(Integer multitablaId) {
        this.multitablaId = multitablaId;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Object getSigla() {
        return sigla;
    }

    public void setSigla(Object sigla) {
        this.sigla = sigla;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getPadreId() {
        return padreId;
    }

    public void setPadreId(Integer padreId) {
        this.padreId = padreId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public String toString() {
        return "TipoIncidencia{" +
                "multitablaId=" + multitablaId +
                ", tabla='" + tabla + '\'' +
                ", sigla=" + sigla +
                ", valor='" + valor + '\'' +
                ", padreId=" + padreId +
                ", estado='" + estado + '\'' +
                ", clasificacion='" + clasificacion + '\'' +
                '}';
    }
}
