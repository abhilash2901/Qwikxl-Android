package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;


public class GetSet_myaccount {

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


