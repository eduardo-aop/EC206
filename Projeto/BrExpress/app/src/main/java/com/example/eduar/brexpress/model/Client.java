package com.example.eduar.brexpress.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 06/06/2018.
 */

public class Client {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("cpf")
    private String mCpf;

    @SerializedName("pdw")
    private String mPwd;

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

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getCpf() {
        return mCpf;
    }

    public void setCpf(String mCpf) {
        this.mCpf = mCpf;
    }

    public String getPwd() {
        return mPwd;
    }

    public void setPwd(String mPwd) {
        this.mPwd = mPwd;
    }

    public String converToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
