package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClasificacionWraper {
    @SerializedName("data")
    private List<ClasificacionIncidencia> data = null;

    public ClasificacionWraper(List<ClasificacionIncidencia> data) {
        this.data = data;
    }

    public List<ClasificacionIncidencia> getData() {
        return data;
    }

    public void setData(List<ClasificacionIncidencia> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClasificacionWraper{" +
                "data=" + data +
                '}';
    }
}
