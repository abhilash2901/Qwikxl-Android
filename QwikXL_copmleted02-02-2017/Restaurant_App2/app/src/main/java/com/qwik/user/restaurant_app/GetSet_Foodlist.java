package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_Foodlist {

    @SerializedName("ProductDetails")
    ArrayList<InnerModel1> foodlist;

    public ArrayList<InnerModel1> getfoodlist() {
        return foodlist;
    }

    public class InnerModel1 {

        @SerializedName("itemname")
        String foodname;

        @SerializedName("image")
        String image;

        @SerializedName("price")
        String Price;

        @SerializedName("id")
        String id;

        @SerializedName("quantity")
        String quantity;

        @SerializedName("description")
        String description;

        public String getfood_name() {
            return foodname;
        }

        public void setfood_name(String foodname) {
            foodname = foodname;
        }

        public String getimage() {
            return image;
        }

        public void setimage(String image) {
            image = image;
        }

        public String getprice() {
            return Price;
        }

        public void setprice(String Price) {
            Price = Price;
        }

        public String getfood_id() {
            return id;
        }

        public void setfood_id(String id) {
            id = id;
        }

        public String get_quantity() {
            return quantity;
        }

        public void set_quantity(String quantity) {
            quantity = quantity;
        }

        public String get_description() {
            return description;
        }

        public void set_description(String description) {
            description = description;
        }
    }


}