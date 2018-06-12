package com.example.eduar.brexpress.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 11/06/2018.
 */

public class Order {
    @SerializedName("product_id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("product_status")
    private int mPurchaseStatus;

    @SerializedName("arrival_date")
    private long mArrivalDate;

    @SerializedName("purchase_date")
    private long mPurchaseDate;

    private Bitmap mImage;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getPurchaseStatus() {
        return mPurchaseStatus;
    }

    public void setPurchaseStatus(int purchaseStatus) {
        this.mPurchaseStatus = purchaseStatus;
    }

    public long getArrivalDate() {
        return mArrivalDate;
    }

    public void setArrivalDate(long arrivalDate) {
        this.mArrivalDate = arrivalDate;
    }

    public long getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.mPurchaseDate = purchaseDate;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }
}
