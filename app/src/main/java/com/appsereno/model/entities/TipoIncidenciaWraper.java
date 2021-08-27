package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TipoIncidenciaWraper {
    @SerializedName("data")
    private List<TipoIncidencia> data = null;

    public TipoIncidenciaWraper(List<TipoIncidencia> data) {
        this.data = data;
    }

    public List<TipoIncidencia> getData() {
        return data;
    }

    public void setData(List<TipoIncidencia> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TipoIncidenciaWraper{" +
                "data=" + data +
                '}';
    }
}
