package com.example.eduar.brexpress;

import android.widget.ImageView;

/**
 * Created by eduar on 03/04/2018.
 */

public class Product {

    private String name;
    private Float price;
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
