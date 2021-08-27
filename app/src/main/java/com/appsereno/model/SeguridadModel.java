package com.appsereno.model;

/**
 * Security Model is the class that defines the structure
 * of the return of JSON objects from the security endpoint
 */
public class SeguridadModel {
    private String message;

    private String data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
