package com.example.eduar.brexpress.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 10/06/2018.
 */

public class Worker extends User {

    @SerializedName("worker_id")
    private int mWorkerId;

    @SerializedName("salary")
    private Float mSalary;

    @SerializedName("sector")
    private String mSector;

    @SerializedName("function")
    private String mFunction;

    public int getWorkerId() {
        return mWorkerId;
    }

    public void setWorkerId(int mWorkerId) {
        this.mWorkerId = mWorkerId;
    }

    public Float getSalary() {
        return mSalary;
    }

    public void setSalary(Float mSalary) {
        this.mSalary = mSalary;
    }

    public String getSector() {
        return mSector;
    }

    public void setSector(String mSector) {
        this.mSector = mSector;
    }

    public String getFunction() {
        return mFunction;
    }

    public void setFunction(String mFunction) {
        this.mFunction = mFunction;
    }
}
