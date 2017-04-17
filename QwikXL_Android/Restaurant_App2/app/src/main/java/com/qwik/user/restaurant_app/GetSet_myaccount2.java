package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetSet_myaccount2 {

    @SerializedName("customerDetails")
    ArrayList<InnerModel1> hotel;

    public ArrayList<InnerModel1> getaccount() {
        return hotel;
    }

    @SerializedName("data")
    ArrayList<InnerModel2> data;

    public ArrayList<InnerModel2> getdata() {
        return data;
    }

    public class InnerModel1 {

        @SerializedName("firstname")
        String firstname;

        @SerializedName("lastname")
        String lastname;

        @SerializedName("email")
        String email;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("password")
        String password;

        @SerializedName("image")
        String image;

        @SerializedName("creditcardno")
        String creditcardno;

        @SerializedName("month")
        String month;

        @SerializedName("year")
        String year;

        @SerializedName("cvv")
        String cvv;

        @SerializedName("address")
        String address;

        @SerializedName("country")
        String country;

        @SerializedName("zipcode")
        String zipcode;


        public String getFirstnamename() {
            return firstname;
        }

        public void setFirstnamename(String firstname) {
            firstname = firstname;
        }

        public String getlastname() {
            return lastname;
        }

        public void setlastname(String lastname) {
            lastname = lastname;
        }

        public String getemail() {
            return email;
        }

        public void setemail(String email) {
            email = email;
        }

        public String getaddress() {
            return address;
        }

        public void setaddress(String address) {
            address = address;
        }


        public String getmobile() {
            return mobile;
        }

        public void setmobile(String mobile) {
            mobile = mobile;
        }

        public String getimage() {
            return image;
        }

        public void setimage(String image) {
            image = image;
        }

        public String getcreditcardno() {
            return creditcardno;
        }

        public void setcreditcardno(String creditcardno) {
            creditcardno = creditcardno;
        }


        public String getcountry() {
            return country;
        }

        public void setcountry(String country) {
            country = country;
        }

        public String getmonth() {
            return month;
        }

        public void setmonth(String month) {
            month = month;
        }

        public String getyear() {
            return year;
        }

        public void setyear(String year) {
            year = year;
        }

        public String getcvv() {
            return cvv;
        }

        public void setcvv(String cvv) {
            cvv = cvv;
        }


        public String getpassword() {
            return password;
        }

        public void setpassword(String password) {
            password = password;
        }

        public String getzipcode() {
            return zipcode;
        }

        public void setzipcode(String zipcode) {
            zipcode = zipcode;
        }


    }

    public class InnerModel2 {

        @SerializedName("card_type")
        String card_type;

        @SerializedName("exp_month")
        String exp_month;

        @SerializedName("exp_year")
        String exp_year;

        @SerializedName("card_last4")
        String card_last4;

        @SerializedName("stripe_customer_id")
        String stripe_customer_id;


        public String getcard_type() {
            return card_type;
        }

        public void setcard_type(String card_type) {
            card_type = card_type;
        }

        public String getexp_month() {
            return exp_month;
        }

        public void setexp_month(String exp_month) {
            exp_month = exp_month;
        }

        public String getexp_year() {
            return exp_year;
        }

        public void setexp_year(String exp_year) {
            exp_year = exp_year;
        }

        public String getcard_last4() {
            return card_last4;
        }

        public void setv(String card_last4) {
            card_last4 = card_last4;
        }

        public String getstripe_customer_id() {
            return stripe_customer_id;
        }

        public void setstripe_customer_id(String stripe_customer_id) {
            stripe_customer_id = stripe_customer_id;
        }

    }


    @SerializedName("Status")
    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        Status = Status;
    }

    @SerializedName("Message")
    String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        Message = Message;
    }



    @SerializedName("country_code")
    String country_code_Id;

    public String getcountrycodeId() {
        return country_code_Id;
    }

    public void setcountrycodeId(String country_code_Id) {
        country_code_Id = country_code_Id;
    }

    @SerializedName("country_name_Id")
    String country_name_Id;

    public String getcountrynameId() {
        return country_name_Id;
    }

    public void setcountrynameId(String country_name_Id) {
        country_name_Id = country_name_Id;
    }
}