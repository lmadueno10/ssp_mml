package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IncidenciaWraper {
    @SerializedName("data")
    private List<Incidente> incidencias;

    public IncidenciaWraper(List<Incidente> incidencias) {
        this.incidencias = incidencias;
    }

    public List<Incidente> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<Incidente> incidencias) {
        this.incidencias = incidencias;
    }

    @Override
    public String toString() {
        return "IncidenciaWraper{" +
                "incidencias=" + incidencias +
                '}';
    }
}
