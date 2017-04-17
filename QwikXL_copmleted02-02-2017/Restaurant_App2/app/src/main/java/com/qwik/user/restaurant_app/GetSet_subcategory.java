package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_subcategory {


    @SerializedName("SubCategoryDetails")
    ArrayList<InnerModel1> subcategory;

    public ArrayList<InnerModel1> getsubcategory() {
        return subcategory;
    }

    public class InnerModel1 {

        @SerializedName("categoryname")
        String cat_name;

        @SerializedName("image")
        String image;

        @SerializedName("id")
        String id;

        public String getsubCat_name() {
            return cat_name;
        }

        public void setsubCat_name(String cat_name) {
            cat_name = cat_name;
        }

        public String getsubimage() {
            return image;
        }

        public void setsubimage(String image) {
            image = image;
        }

        public String getsubcat_id() {
            return id;
        }

        public void setsubcat_id(String id) {
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

