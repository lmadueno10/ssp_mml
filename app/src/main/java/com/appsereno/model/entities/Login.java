package com.appsereno.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Login is the class that defines the structure
 * of the return of JSON objects from the auth/signin endpoint
 */
public class Login {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("auth_token")
    @Expose
    private String auth_token;
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;
    @SerializedName("user")
    @Expose
    private Usuario user;

    public Login(){}
    public Login(int code, String auth_token, String refresh_token, Usuario user) {
        this.code = code;
        this.auth_token = auth_token;
        this.refresh_token = refresh_token;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Login{" +
                "code=" + code +
                ", auth_token='" + auth_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", user=" + user +
                '}';
    }
}
