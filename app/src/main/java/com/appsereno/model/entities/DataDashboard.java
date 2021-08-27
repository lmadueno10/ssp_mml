package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class DataDashboard {
    @SerializedName("cantIncidenciaHoy")
    private String cantIncidenciaHoy;
    @SerializedName("cantIncidenciaUltimaHora")
    private String cantIncidenciaUltimaHora;
    @SerializedName("cantIncidenciaUtimosSieteDias")
    private String cantIncidenciaUtimosSieteDias;
    @SerializedName("cantIncidenciaAtendidas")
    private String cantIncidenciaAtendidas;
    @SerializedName("cantIncidenciaPendientes")
    private String cantIncidenciaPendientes;
    @SerializedName("promIncidenciaAtendidaHora")
    private String promIncidenciaAtendidaHora;
    @SerializedName("cateIncidenciaMasReportada")
    private String cateIncidenciaMasReportada;
    @SerializedName("cantCateIncidenciaMasReportada")
    private String cantCateIncidenciaMasReportada;

    public DataDashboard(String cantIncidenciaHoy, String cantIncidenciaUltimaHora, String cantIncidenciaUtimosSieteDias, String cantIncidenciaAtendidas, String cantIncidenciaPendientes, String promIncidenciaAtendidaHora, String cateIncidenciaMasReportada, String cantCateIncidenciaMasReportada) {
        this.cantIncidenciaHoy = cantIncidenciaHoy;
        this.cantIncidenciaUltimaHora = cantIncidenciaUltimaHora;
        this.cantIncidenciaUtimosSieteDias = cantIncidenciaUtimosSieteDias;
        this.cantIncidenciaAtendidas = cantIncidenciaAtendidas;
        this.cantIncidenciaPendientes = cantIncidenciaPendientes;
        this.promIncidenciaAtendidaHora = promIncidenciaAtendidaHora;
        this.cateIncidenciaMasReportada = cateIncidenciaMasReportada;
        this.cantCateIncidenciaMasReportada = cantCateIncidenciaMasReportada;
    }

    public String getCantIncidenciaHoy() {
        return cantIncidenciaHoy;
    }

    public void setCantIncidenciaHoy(String cantIncidenciaHoy) {
        this.cantIncidenciaHoy = cantIncidenciaHoy;
    }

    public String getCantIncidenciaUltimaHora() {
        return cantIncidenciaUltimaHora;
    }

    public void setCantIncidenciaUltimaHora(String cantIncidenciaUltimaHora) {
        this.cantIncidenciaUltimaHora = cantIncidenciaUltimaHora;
    }

    public String getCantIncidenciaUtimosSieteDias() {
        return cantIncidenciaUtimosSieteDias;
    }

    public void setCantIncidenciaUtimosSieteDias(String cantIncidenciaUtimosSieteDias) {
        this.cantIncidenciaUtimosSieteDias = cantIncidenciaUtimosSieteDias;
    }

    public String getCantIncidenciaAtendidas() {
        return cantIncidenciaAtendidas;
    }

    public void setCantIncidenciaAtendidas(String cantIncidenciaAtendidas) {
        this.cantIncidenciaAtendidas = cantIncidenciaAtendidas;
    }

    public String getCantIncidenciaPendientes() {
        return cantIncidenciaPendientes;
    }

    public void setCantIncidenciaPendientes(String cantIncidenciaPendientes) {
        this.cantIncidenciaPendientes = cantIncidenciaPendientes;
    }

    public String getPromIncidenciaAtendidaHora() {
        return promIncidenciaAtendidaHora;
    }

    public void setPromIncidenciaAtendidaHora(String promIncidenciaAtendidaHora) {
        this.promIncidenciaAtendidaHora = promIncidenciaAtendidaHora;
    }

    public String getCateIncidenciaMasReportada() {
        return cateIncidenciaMasReportada;
    }

    public void setCateIncidenciaMasReportada(String cateIncidenciaMasReportada) {
        this.cateIncidenciaMasReportada = cateIncidenciaMasReportada;
    }

    public String getCantCateIncidenciaMasReportada() {
        return cantCateIncidenciaMasReportada;
    }

    public void setCantCateIncidenciaMasReportada(String cantCateIncidenciaMasReportada) {
        this.cantCateIncidenciaMasReportada = cantCateIncidenciaMasReportada;
    }
}
