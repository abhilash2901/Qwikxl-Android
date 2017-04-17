package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;


public class GetSet_fooditem {

    @SerializedName("itemname")
    String name;

    public String getname() {
        return name;
    }

    public void setname(String name) {
        name = name;
    }

    @SerializedName("price")
    String price;

    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        price = price;
    }

    @SerializedName("image")
    String image;

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        image = image;
    }

    @SerializedName("id")
    String id;

    public String getid() {
        return id;
    }

    public void setid(String id) {
        id = id;
    }

    @SerializedName("quantity")
    String quantity;

    public String getquantity() {
        return quantity;
    }

    public void setquantity(String quantity) {
        quantity = quantity;
    }

    @SerializedName("description")
    String description;

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        description = description;
    }

    @SerializedName("overview")
    String overview;

    public String getoverview() {
        return overview;
    }

    public void setoverview(String overview) {
        overview = overview;
    }


    @SerializedName("nutritionalinfo")
    String nutritionalinfo;

    public String getnutritionalinfo() {
        return nutritionalinfo;
    }

    public void setnutritionalinfo(String nutritionalinfo) {
        nutritionalinfo = nutritionalinfo;
    }

    @SerializedName("moreinfo")
    String moreinfo;

    public String getmoreinfo() {
        return moreinfo;
    }

    public void setmoreinfo(String moreinfo) {
        moreinfo = moreinfo;
    }


}

