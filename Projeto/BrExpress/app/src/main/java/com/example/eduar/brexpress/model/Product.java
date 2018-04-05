package com.example.eduar.brexpress.model;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduar on 03/04/2018.
 */

public class Product {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private Float price;

    @SerializedName("discount")
    private Float discount;

    private ImageView image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
