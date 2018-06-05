package com.example.eduar.brexpress.model;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 03/04/2018.
 */

public class Product {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("qtd")
    private Float mQtd;

    @SerializedName("price")
    private Float mPrice;

    @SerializedName("discount")
    private Float mDiscount;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("image")
    private String mImageBase64;

    @SerializedName("imageUrl")
    private String mImageUrl;

    private Bitmap mImage;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Float getPrice() {
        return mPrice;
    }

    public void setPrice(Float mPrice) {
        this.mPrice = mPrice;
    }

    public Float getQtd() {
        return mQtd;
    }

    public void setQtd(Float mQtd) {
        this.mQtd = mQtd;
    }

    public Float getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Float discount) {
        this.mDiscount = discount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }

    public String getImageBase64() {
        return mImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.mImageBase64 = imageBase64;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String converToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Float getPriceWithDiscount() {
        Float disc = (100 - this.getDiscount()) / 100;
        Float price = (this.getPrice() * disc);
        return price;
    }
}
