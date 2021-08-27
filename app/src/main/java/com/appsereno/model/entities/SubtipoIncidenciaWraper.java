package com.appsereno.model.entities;

import java.util.List;

public class SubtipoIncidenciaWraper {
    private List<SubtipoIncidencia> data;

    public SubtipoIncidenciaWraper(List<SubtipoIncidencia> data) {
        this.data = data;
    }

    public List<SubtipoIncidencia> getData() {
        return data;
    }

    public void setData(List<SubtipoIncidencia> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SubtipoIncidenciaWraper{" +
                "data=" + data +
                '}';
    }
}
