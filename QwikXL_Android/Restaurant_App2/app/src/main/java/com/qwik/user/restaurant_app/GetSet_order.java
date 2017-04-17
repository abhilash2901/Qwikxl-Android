package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;


public class GetSet_order {

    @SerializedName("Status")
    String status;

    @SerializedName("Orderid")
    String Orderid;

    @SerializedName("unique_id")
    String unique_id;

    @SerializedName("transaction_id")
    String transaction_id;



    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        status = status;
    }

    public String gettransaction_id() {
        return transaction_id;
    }

    public void settransaction_id(String transaction_id) {
        transaction_id = transaction_id;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String Orderid) {
        Orderid = Orderid;
    }

    public String getunique_id() {
        return unique_id;
    }

    public void setunique_id(String unique_id) {
        unique_id = unique_id;
    }



}


