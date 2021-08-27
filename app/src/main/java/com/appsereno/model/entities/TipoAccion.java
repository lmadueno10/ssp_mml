package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class TipoAccion {
    @SerializedName("id_tipo_accion")
    private Integer idTipoAccion;
    @SerializedName("nombre_accion")
    private String nombreAccion;
    @SerializedName("estado")
    private Integer estado;

    public TipoAccion(Integer idTipoAccion, String nombreAccion, Integer estado) {
        this.idTipoAccion = idTipoAccion;
        this.nombreAccion = nombreAccion;
        this.estado = estado;
    }

    public Integer getIdTipoAccion() {
        return idTipoAccion;
    }

    public void setIdTipoAccion(Integer idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }

    public String getNombreAccion() {
        return nombreAccion;
    }

    public void setNombreAccion(String nombreAccion) {
        this.nombreAccion = nombreAccion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "TipoAccion{" +
                "idTipoAccion=" + idTipoAccion +
                ", nombreAccion='" + nombreAccion + '\'' +
                ", estado=" + estado +
                '}';
    }
}
