package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;
/**
 * Usuario is the class that defines the structure
 * of the return of JSON objects from the auth/signin endpoint
 */
public class Usuario {
    @SerializedName("id_usuario")
    private int id;
    @SerializedName("usuario")
    private String usuario;
    @SerializedName("codigo")
    private String codigoUsuario;
    @SerializedName("sector")
    private String sector;
    @SerializedName("nombres_apellidos")
    private String nombresApellidos;
    @SerializedName("dni")
    private String dni;
    @SerializedName("celular")
    private String celular;
    @SerializedName("id_supervisor")
    private  String supervisor;
    @SerializedName("id_personal")
    private int idPersonal;
    @SerializedName("emei")
    private String emei;
    @SerializedName("estado")
    private int estado;

    public Usuario(int id, String usuario, String codigoUsuario, String sector, String nombresApellidos, String dni, String celular, String supervisor, int idPersonal, String emei, int estado) {
        this.id = id;
        this.usuario = usuario;
        this.codigoUsuario = codigoUsuario;
        this.sector = sector;
        this.nombresApellidos = nombresApellidos;
        this.dni = dni;
        this.celular = celular;
        this.supervisor = supervisor;
        this.idPersonal = idPersonal;
        this.emei = emei;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public String getEmei() {
        return emei;
    }

    public void setEmei(String emei) {
        this.emei = emei;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", codigoUsuario='" + codigoUsuario + '\'' +
                ", sector='" + sector + '\'' +
                ", nombresApellidos='" + nombresApellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", celular='" + celular + '\'' +
                ", supervisor='" + supervisor + '\'' +
                ", idPersonal=" + idPersonal +
                ", emei='" + emei + '\'' +
                ", estado=" + estado +
                '}';
    }
}
