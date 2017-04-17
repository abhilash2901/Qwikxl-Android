package com.qwik.user.restaurant_app;


import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Data_values {
    static String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";
    static String passmatcher = "^(?=(.*[A-Z]){1})(?=(.*[a-z]){1})(?=(.*[0-9]){1})(?=(.*[~!@#$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?]{1})).*$";
    static String reward_point;
    AABDatabaseManager db = null;
    ArrayList<ArrayList<Object>> data = null;

    public static class Tools {
        private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

        public static int generateViewId() {
            if (Build.VERSION.SDK_INT < 17) {
                for (; ; ) {
                    final int result = sNextGeneratedId.get();
                    int newValue = result + 1;
                    if (newValue > 0x00FFFFFF)
                        newValue = 1; // Roll over to 1, not 0.
                    if (sNextGeneratedId.compareAndSet(result, newValue)) {
                        return result;
                    }
                }
            } else {
                return View.generateViewId();
            }
        }
    }

    static void logout() {
        SharedPreferences.Editor editor = Home.pref.edit();
        editor.clear();
        editor.commit();
    }

    public String reward(String username) {
        try {
            ApiClient.getPosApp().getrewardpoint(username, new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {
                    System.out.println("Sucess" + response.getUrl());
                    String res = getSet_login.getstatus();
                    if (res.equalsIgnoreCase("true")) {
                        try {
                            reward_point = getSet_login.getreward();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return reward_point;
    }
    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

}
