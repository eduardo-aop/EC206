package com.example.eduar.brexpress.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 10/06/2018.
 */

public class Worker extends User {

    @SerializedName("salary")
    private Float mSalary;

    @SerializedName("sector")
    private String mSector;

    @SerializedName("function")
    private String mFunction;
}
