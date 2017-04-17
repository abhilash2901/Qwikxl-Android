package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_hotel {

    @SerializedName("StoreDetails")
    ArrayList<InnerModel1> hotel;

    public ArrayList<InnerModel1> gethotel() {
        return hotel;
    }

    public class InnerModel1 {

        @SerializedName("phone")
        String phone;

        @SerializedName("name")
        String name;

        @SerializedName("rate")
        String rate;

        @SerializedName("logo")
        String image;

        @SerializedName("id")
        String id;

        @SerializedName("specials")

        String specials;

        @SerializedName("delivery_minim")
        String delivery_minim;

        @SerializedName("delivery_charge")
        String delivery_charge;

        @SerializedName("distance")
        String distance;

        public String getphone() {
            return phone;
        }

        public void setphone(String phone) {
            phone = phone;
        }

        public String getname() {
            return name;
        }

        public void setname(String name) {
            name = name;
        }

        public String getrate() { return rate;}

        public void setrate(String rate) { rate = rate;}

        public String getimage() { return image;}

        public void setimage(String image) { image = image;}

        public String getid() {return id;}

        public void setid(String id) {id = id;}

        public String getspecials() { return specials;}

        public void setspecials(String specials) { specials = specials;}

        public String getdelivery_minim() { return delivery_minim;}

        public void setdelivery_minim(String delivery_minim) { delivery_minim = delivery_minim;}

        public String getdelivery_charge() { return delivery_charge;}

        public void setdelivery_charge(String delivery_charge) { delivery_charge = delivery_charge;}

        public String getdistance() { return distance;}

        public void setdistance(String distance) { distance = distance;}


    }
}