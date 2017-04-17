package com.qwik.user.restaurant_app;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

public class ApiClient {


    private static PosApp PosApp;

   // public static String url_common = "http://202.191.64.218/dev/qwikxl_admin_console_new/public";
    public static String url_common="http://54.175.210.202/qwikxl_admin_console_new/public";


    public static PosApp getPosApp() {
        if (PosApp == null) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://54.175.210.202/qwikxl_admin_console_new/public").setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("YOUR_LOG_TAG QwikXL")).build();
            //  http://qwikxl-novatross.rhcloud.com/orderWebService.php
            //   http://spericorn.com/hotel/process
            //RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://portal.qwikxl.com/process").build();
            //http://202.88.237.237:8080/qwik_test_stripe/demo/public
            PosApp = restAdapter.create(PosApp.class);
        }
        return PosApp;
    }

    public interface PosApp {

        //        @POST("/customerWebService.php?method=distanceList")
//        void getCurrencydetails(@Query("latitude") String lat, @Query("longitude") String longi, @Query("type") String token, Callback<GetSet_hotel> callback);
        // @FormUrlEncoded

        @POST("/distanceList")
        void getCurrencydetails(@Query("latitude") String latitude, @Query("longitude") String longitude, @Query("time") String time, Callback<GetSet_hotel> callback);

        @POST("/getDepartmentList")
        void getCategaorydetails(@Query("store_id") String id, @Query("keyword") String key, Callback<GetSet_category> callback);

        @POST("/getCategoryList")
        void get_mainCategaorydetails(@Query("store_id") String id, @Query("department_id") String department_id, @Query("keyword") String key, Callback<GetSet_maincategory> callback);

        @POST("/getSubCategoryList")
        void getsub_Categaorydetails(@Query("store_id") String id, @Query("category_id") String category_id, @Query("keyword") String key, Callback<GetSet_subcategory> callback);

        @POST("/getProductList")
        void getfooddetails(@Query("store_id") String store_id, @Query("category_id") String token, Callback<GetSet_Foodlist> callback);

        @POST("/getSingleProduct")
        void getfooddata(@Query("id") String token, Callback<GetSet_fooditem> callback);

        @POST("/customerLogin")
        void getlogin(@Query("email") String email, @Query("password") String password, Callback<GetSet_login> callback);

        @POST("/customerRegister")
        void getregister(@Query("User_details") String name, Callback<GetSet_login> callback);

        @POST("/customerDetails")
        void getmyaccount(@Query("email") String token, Callback<GetSet_myaccount2> callback);

        @POST("/updateCustomer")
        void getupdate(@Query("User_details") String User_details, Callback<GetSet_login> callback);

        @POST("/getLatitudeCity")
        void getlatlong(@Query("city") String city, Callback<GetSet_lattitude> callback);

        @POST("/app_call_web_service.php?method=getHotel")
        void gethotel(@Query("search_name") String name, @Query("key_value") String key, @Query("latitude") String latt, @Query("longitude") String longi, Callback<GetSet_hotel> callback);

        @POST("/orderDetails")
        void getorderdata(@Query("userid") String city, Callback<GetSet_orderid> callback);

        @POST("/orderData")
        void getorderitem(@Query("orderid") String city, Callback<GetSet_orderid> callback);

        @POST("/customerWebService.php?method=reward_point")
        void getrewardpoint(@Query("email") String email, Callback<GetSet_login> callback);

//        @POST("/customerWebService.php?method=updatePassword")
//        void update_password(@Query("username") String username, @Query("password") String password, Callback<GetSet_login> callback);

        @POST("/sentPassword")
        void getpassword(@Query("email") String email, Callback<GetSet_login> callback);

        @POST("/getCountryList")
        void getcountry(Callback<GetSet_country> callback);

        @POST("/updateCreditCard")
        void getupdatecard(@Query("Stripe_token") String Stripe_token, @Query("username") String username, @Query("country") String country1, @Query("zipcode") String zipcode1, Callback<GetSet_login> callback);

        @POST("/updatePassword")
        void change_password(@Query("email") String email, @Query("password") String password, Callback<GetSet_login> callback);

        @POST("/getCreditCarddetails")
        void getmyaccount2(@Query("username") String token, Callback<GetSet_myaccount2> callback);

        //@FormUrlEncoded
        @POST("/saveOrderDetails")
        void Placing_order(@Query("order_detail") String order_detail, Callback<GetSet_order> callback);


        @Multipart
        @POST("/customerImage")
        void getprofile_pic(@Part("email") String email, @Part("image") String image, Callback<GetSet_login> callback);

    }
}