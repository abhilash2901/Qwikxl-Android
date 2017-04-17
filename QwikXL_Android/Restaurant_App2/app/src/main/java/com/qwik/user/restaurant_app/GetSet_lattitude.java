package com.qwik.user.restaurant_app;

import com.google.gson.annotations.SerializedName;

public class GetSet_lattitude {

    @SerializedName("Status")
    String Status;

    @SerializedName("Message")
    String Message;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    public String getlatt() {
        return latitude;
    }

    public void setlatt(String latitude) {
        latitude = latitude;
    }

    public String getlongi() {
        return longitude;
    }

    public void setlongi(String longitude) {
        longitude = longitude;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        Message = Message;
    }
}


