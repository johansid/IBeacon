package com.app.market.BeaconService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.app.market.Constant;
import com.app.market.application.ApplicationManager;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

/**
 * Created by OmarAli on 15/02/2016.
 */
public class MyService extends Service {


    BeaconManager beaconManager;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        beaconManager = new BeaconManager(this);

        Toast.makeText(getApplicationContext(),"service started",Toast.LENGTH_SHORT).show();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

                Region yellowRegion = new Region("ranged region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.YELLOW_MAJOR, Constant.YELLOW_MINOR);

                Region blueRegion = new Region("ranged region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.BLUE_MAJOR, Constant.BLUE_MINOR);

                Region iceRegion = new Region("ranged region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.ICE_MAJOR, Constant.ICE_MINOR);

                beaconManager.startRanging(yellowRegion);
                beaconManager.startRanging(blueRegion);
                beaconManager.startRanging(iceRegion);
            }
        });



        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if(list.size()>0){
                    String mac = list.get(0).getMacAddress().toString();
                    int major = list.get(0).getMajor();
                    int minor = list.get(0).getMinor();

                    // yellow Beacon
                    if(major == Constant.YELLOW_MAJOR){
                        Toast.makeText(getApplicationContext(),"yello",Toast.LENGTH_SHORT).show();
                    } // blue beacon
                    else if(major == Constant.BLUE_MAJOR){
                        Toast.makeText(getApplicationContext(),"blue",Toast.LENGTH_SHORT).show();
                    }// ice beacon
                    else if(major == Constant.ICE_MAJOR){
                        Toast.makeText(getApplicationContext(),"ice",Toast.LENGTH_SHORT).show();
                        Log.d("mac inner : ","true");
                    }
                }

            }
        });
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
