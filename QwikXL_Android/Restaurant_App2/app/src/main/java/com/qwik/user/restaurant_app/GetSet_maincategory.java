package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_maincategory {

    @SerializedName("categoryDetails")
    ArrayList<InnerModel1> category;

    public ArrayList<InnerModel1> getmaincategory() {
        return category;
    }


    public class InnerModel1 {

        @SerializedName("categoryname")
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




}

