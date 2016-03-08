package com.app.market.api.interfaces;

import com.app.market.api.models.ProductResponse;
import com.app.market.api.models.StatusRespone;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by OmarAli on 09/02/2016.
 */
public interface IBeacon {
    @GET("/here goes the requested url")
    void getProductInfo(@Query("UUID") String uuid,
                        @Query("major") String major,
                        @Query("minor") String minor
                        ,Callback<ProductResponse> callback);

    @FormUrlEncoded
    @POST("/tags.php")
    void storeUserInfo(@Field("name") String name,
                       @Field("email") String mail,
                       @Field("company") String company,
                       @Field("phone") String phone,
                       @Field("profile")String profile,
                       @Field("code")String code,
                       @Field("tag")String tag
            ,Callback<StatusRespone> callback);
}
