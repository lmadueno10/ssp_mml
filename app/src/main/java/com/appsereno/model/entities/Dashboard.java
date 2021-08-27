package com.appsereno.model.entities;

import com.google.gson.annotations.SerializedName;

public class Dashboard {
    @SerializedName("data")
    private DataDashboard data;

    public Dashboard(DataDashboard data) {
        this.data = data;
    }

    public DataDashboard getData() {
        return data;
    }

    public void setData(DataDashboard data) {
        this.data = data;
    }
}
