package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class ClasificacionIncidencia {

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

    public ClasificacionIncidencia(Integer multitablaId, String tabla, Object sigla, String valor, Integer padreId, String estado) {
        this.multitablaId = multitablaId;
        this.tabla = tabla;
        this.sigla = sigla;
        this.valor = valor;
        this.padreId = padreId;
        this.estado = estado;
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

    @Override
    public String toString() {
        return "ClasificacionIncidencia{" +
                "multitablaId=" + multitablaId +
                ", tabla='" + tabla + '\'' +
                ", sigla=" + sigla +
                ", valor='" + valor + '\'' +
                ", padreId=" + padreId +
                ", estado='" + estado + '\'' +
                '}';
    }
}


