package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_category {

    @SerializedName("categoryDetails")
    ArrayList<InnerModel1> category;

    public ArrayList<InnerModel1> getcategory() {
        return category;
    }


    @SerializedName("banners")
    ArrayList<InnerModel2> banners;

    public ArrayList<InnerModel2> getbanners() {
        return banners;
    }


    public class InnerModel1 {

        @SerializedName("name")
        String cat_name;

        @SerializedName("image")
        String image;

        @SerializedName("id")
        String id;

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            cat_name = cat_name;
        }

        public String getimage() {
            return image;
        }

        public void setimage(String image) {
            image = image;
        }

        public String getcat_id() {
            return id;
        }

        public void setcat_id(String id) {
            id = id;
        }


    }

    @SerializedName("Status")
    String status;

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        status = status;
    }

    @SerializedName("slider1")
    String slider1;

    public String getimg1() {
        return slider1;
    }

    public void setimg1(String slider1) {
        slider1 = slider1;
    }


    @SerializedName("slider2")
    String slider2;

    public String getimg2() {
        return slider2;
    }

    public void setimg2(String slider2) {
        slider2 = slider2;
    }

    @SerializedName("slider3")
    String slider3;

    public String getimg3() {
        return slider3;
    }

    public void setimg3(String slider3) {
        slider3 = slider3;
    }


    public class InnerModel2 {

        @SerializedName("image")
        String image;

        public String getimage() {
            return image;
        }

        public void setimage(String image) {
            image = image;
        }

    }
}

