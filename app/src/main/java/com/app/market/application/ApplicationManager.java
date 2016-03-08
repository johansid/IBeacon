package com.app.market.application;

import android.app.Application;
import android.util.Log;
import com.app.market.bus.BusProvider;
import com.app.market.manager.RetrofitManager;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.squareup.otto.Bus;
import java.util.UUID;

/**
 * Created by OmarAli on 09/02/2016.
 */
public class ApplicationManager extends Application {

    private SocialNetwork socialNetwork = null;
    private RetrofitManager sProductManager;
    private Bus mBus = BusProvider.getInstance();
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sProductManager=new RetrofitManager(this,mBus);
        //register sProductManager and application context to bus
        mBus.register(sProductManager);
        mBus.register(this);

    /*   beaconManager = new BeaconManager(getApplicationContext());
        // add this below:
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

                beaconManager.startMonitoring(new Region(
                        "yello region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        53099, 5734));
                // UUID , major number , minor number
                beaconManager.startMonitoring(new Region(
                        "blue region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        27908, 18629));

                beaconManager.startMonitoring(new Region(
                        "ice region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        59892, 31845));
            }
        }); */
    }

    public SocialNetwork getInstance(){return socialNetwork;
    }

    public void setSocialManager(SocialNetwork appManager){
        Log.d("manager :","done");
        this.socialNetwork = appManager;
    }

    public BeaconManager getBeaconManager(){
        return this.beaconManager;
    }
}
