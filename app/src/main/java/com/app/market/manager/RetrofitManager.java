package com.app.market.manager;

import android.content.Context;
import android.util.Log;

import com.app.market.api.events.GetProductEvent;
import com.app.market.api.IBeaconClient;
import com.app.market.api.events.StoreUserEvent;
import com.app.market.api.models.ProductResponse;
import com.app.market.api.models.StatusRespone;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by OmarAli on 26/01/2016.
 */
public class RetrofitManager {
    private Context mContext;
    private Bus mBus;
    private IBeaconClient iBeaconClient;

    public RetrofitManager(Context context, Bus bus){
        this.mBus=bus;
        this.mContext=context;
        //get iBeacon Client
        iBeaconClient=IBeaconClient.getClient();
    }

    /**
     * @Subscripe annotation is to make this method notified by mBus
     * this method represent the callback that will be fired after iBeaconClient make getProduct request
     * when request success the product object will be posted on Bus to notify all registered activitys
     *
     * @param  productEvent  an object from GetProductEvent class to hold Beacon information
     */
    @Subscribe
    public void OnGetProductEvent(GetProductEvent productEvent){

        Callback<ProductResponse>callback=new Callback<ProductResponse>() {
            @Override
            public void success(ProductResponse productResponse, Response response){
                mBus.post(productResponse);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        iBeaconClient.getProductInfo(productEvent,callback);
    }

    @Subscribe
    public void OnStoreUserEvent(StoreUserEvent userEvent){

        Callback<StatusRespone>callback=new Callback<StatusRespone>() {
            @Override
            public void success(StatusRespone statusRespone, Response response){
                if(response.toString().contains("ok")){
                    Log.d("response : ","ok");
                }
                mBus.post(statusRespone);
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(error);
            }
        };
        iBeaconClient.storeUserInfo(userEvent,callback);
    }
}
