package com.example.eduar.brexpress.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 13/06/2018.
 */

public class Shipping {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("company")
    private String mCompany;

    @SerializedName("payment")
    private float mPayment;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public float getPayment() {
        return mPayment;
    }

    public void setPayment(float mPayment) {
        this.mPayment = mPayment;
    }

    public String converToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
