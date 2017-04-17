package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;


public class GetSet_login {

    @SerializedName("Status")
    String status;

    @SerializedName("reward_point")
    String reward_point;

    @SerializedName("Error")
    String error;

    @SerializedName("Mobile")
    String Mobile;

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        status = status;
    }

    public String geterror() {
        return error;
    }

    public void seterror(String error) {
        error = error;
    }

    public String getreward() {
        return reward_point;
    }

    public void setreward(String reward_point) {
        reward_point = reward_point;
    }

    public String getmobile() {
        return Mobile;
    }

    public void setmobile(String Mobile) {
        Mobile = Mobile;
    }

}


