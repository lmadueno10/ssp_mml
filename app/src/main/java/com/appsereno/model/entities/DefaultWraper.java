package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class DefaultWraper {
    @SerializedName("data")
    private Object object;

    public DefaultWraper(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
