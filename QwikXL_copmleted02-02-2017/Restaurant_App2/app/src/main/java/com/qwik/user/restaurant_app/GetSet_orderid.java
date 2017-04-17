package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_orderid {


    @SerializedName("orderDetails")
    ArrayList<InnerModel1> order;
    public ArrayList<InnerModel1> getorder() {
        return order;
    }

    public class InnerModel1 {

        @SerializedName("price")
        String price;
        @SerializedName("createddate")
        String date;

        @SerializedName("id")
        String id;

        @SerializedName("quantity")
        String quantity;

        @SerializedName("name")
        String itemname;

        @SerializedName("unique_id")
        String unique_id;


        public String getprice() {
            return price;
        }

        public void setprice(String price) {
            price = price;
        }

        public String getorderid() {
            return id;
        }

        public void setorderid(String id) {
            id = id;
        }

        public String getquantity() {
            return quantity;
        }

        public void setquantity(String quantity) {
            quantity = quantity;
        }

        public String getitem() {
            return itemname;
        }

        public void setitemname(String itemname) {
            itemname = itemname;
        }

        public String getdate() {
            return date;
        }

        public void setdate(String date) {
            date = date;
        }

        public String getunique_id() {
            return unique_id;
        }

        public void setunique_id(String unique_id) {
            unique_id = unique_id;
        }

    }

    @SerializedName("Total")
    String total;

//    @SerializedName("grand_total")
//    String grand_total;

    @SerializedName("reward_point")
    String reward;


    @SerializedName("orderstatus")
    String status;

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        status = status;
    }

//    public String getgrand_total() {
//        return grand_total;
//    }
//
//    public void setgrand_total(String grand_total) {
//        grand_total = grand_total;
//    }

    public String getreward() {
        return reward;
    }

    public void setreward(String reward) {
        reward = reward;
    }

    public String gettotal() {
        return total;
    }

    public void settotal(String total) {
        total = total;
    }

}