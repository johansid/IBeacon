package com.app.market.api;

import com.app.market.api.events.GetProductEvent;
import com.app.market.api.events.StoreUserEvent;
import com.app.market.api.interfaces.IBeacon;
import com.app.market.api.models.ProductResponse;
import com.app.market.api.models.StatusRespone;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by OmarAli on 26/01/2016.
 */
public class IBeaconClient {

    public static String BASE_URL="http://super-buzz-tree.hostoi.com";
    private static IBeaconClient mIBeaconClient;
    private static RestAdapter mRestAdapter;

    /**
     * singleton design pattern
     * Returns only one object of the ForecastClient
     */
    public static IBeaconClient getClient(){
        if(mRestAdapter==null){
            mIBeaconClient =new IBeaconClient();
        }
        return mIBeaconClient;
    }

    /**
     * building RestAdapter object with specified configurations
     */
    private IBeaconClient(){
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    /**
     * after this method gets called a callback will be fired with either success or fail request
     * if success request then a callback holding weather object will be fired
     *
     * @param  productEvent  an object from GetWeatherEvent class
     * @param  productCallback the callback after network request made with Retrofit
     */
    public void getProductInfo(GetProductEvent productEvent, Callback<ProductResponse>productCallback){

        //initialize IBeacon interface using RestAdapter configurations
        IBeacon beacon=mRestAdapter.create(IBeacon.class);

        beacon.getProductInfo( productEvent.get_UUID(),
                productEvent.get_majorNumber(),
                productEvent.get_minorNumber(),productCallback);
    }

    /**
     * after this method gets called a callback will be fired with either success or fail request
     * if success request then a callback holding weather object will be fired
     *
     * @param  userEvent  an object from StoreUserEvent
     * @param  responeCallback the callback after network request made with Retrofit
     */
    public void storeUserInfo(StoreUserEvent userEvent, Callback<StatusRespone>responeCallback){

        //initialize IBeacon interface using RestAdapter configurations
        IBeacon beacon=mRestAdapter.create(IBeacon.class);

        beacon.storeUserInfo( userEvent.getName(),
                userEvent.getEmail(),
                userEvent.getCompany(),
                userEvent.getPhone(),
                userEvent.getProfile(),
                userEvent.getCode(),
                "new",
                responeCallback);
    }
}
