package com.appsereno.model.entities;

/**
 * Token is the class that defines the structure
 * of the return of JSON objects from the auth/signin endpoint
 */
public class Token {
    private int code;
    private String token;

    public Token(int code, String token) {
        this.code = code;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
