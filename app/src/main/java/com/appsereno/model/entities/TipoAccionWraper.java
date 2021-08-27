package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TipoAccionWraper {
    @SerializedName("data")
    private List<TipoAccion> tipoAccionList;

    public TipoAccionWraper(List<TipoAccion> tipoAccionList) {
        this.tipoAccionList = tipoAccionList;
    }

    public List<TipoAccion> getTipoAccionList() {
        return tipoAccionList;
    }

    public void setTipoAccionList(List<TipoAccion> tipoAccionList) {
        this.tipoAccionList = tipoAccionList;
    }

    @Override
    public String toString() {
        return "TipoAccionWraper{" +
                "tipoAccionList=" + tipoAccionList +
                '}';
    }
}
