package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_country {

    @SerializedName("countryDetails")
    ArrayList<InnerModel1> country;

    public ArrayList<InnerModel1> getcountrylist() {
        return country;
    }

    public class InnerModel1 {

        @SerializedName("name")
        String countryname;

        @SerializedName("flag")
        String flag;

        @SerializedName("sortname")
        String countrycode;

        public String getCountry_name() {
            return countryname;
        }

        public void setcountry_name(String countryname) {
            countryname = countryname;
        }

        public String getflag() {
            return flag;
        }

        public void setflag(String flag) {
            flag = flag;
        }

        public String getcountry_code() {
            return countrycode;
        }

        public void setcountry_code(String countrycode) {
            countrycode = countrycode;
        }

    }


}