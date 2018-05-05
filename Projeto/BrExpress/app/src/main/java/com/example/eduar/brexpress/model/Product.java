package com.example.eduar.brexpress.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 03/04/2018.
 */

public class Product {

    @SerializedName("name")
    private String mName;

    @SerializedName("price")
    private Float mPrice;

    @SerializedName("discount")
    private Float mDiscount;

    @SerializedName("mainImage")
    private Image mMainImage;

    @SerializedName("photos")
    private Image mImages;

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

    public Float getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Float discount) {
        this.mDiscount = discount;
    }

    public Image getImage() {
        return mMainImage;
    }

    public void setImage(Image image) {
        this.mMainImage = image;
    }

    public Image getImages() {
        return mImages;
    }

    public void setImages(Image mImages) {
        this.mImages = mImages;
    }
}
