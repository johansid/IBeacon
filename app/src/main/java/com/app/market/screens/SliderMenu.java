package com.app.market.screens;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.app.market.Constant;
import com.app.market.FileManager;
import com.app.market.R;
import com.app.market.api.models.StatusRespone;
import com.app.market.application.ApplicationManager;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SliderMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean timerFinished = false;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    File imageFile;
    BeaconManager beaconManager=null;
    Region yellowRegion = new Region("ranged region",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.YELLOW_MAJOR, Constant.YELLOW_MINOR);

    Region blueRegion = new Region("ranged region",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.BLUE_MAJOR, Constant.BLUE_MINOR);

    Region iceRegion = new Region("ranged region",
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Constant.ICE_MAJOR, Constant.ICE_MINOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        beaconManager = new BeaconManager(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startBeaconService();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slider_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.startMonitoring(yellowRegion);
        beaconManager.stopMonitoring(blueRegion);
       // beaconManager.stopRanging(iceRegion);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            // start intent for picking image from camera on camera button clicked
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(),SliderMenu.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_container, new ProfileFragment()).commit();

        }
        else if(id==R.id.nav_alrhab){
            FileManager.copyAssets(getApplicationContext());
            Intent i = new Intent(Intent.ACTION_VIEW);
            File mcqDemo = new File(getExternalFilesDir(null) + "/antar.pdf");
            i.setDataAndType(Uri.fromFile(mcqDemo), "application/pdf");
            startActivity(i);

        }else if (id == R.id.nav_share) {
            if(imageFile==null){
                Toast.makeText(this,"no image to share",Toast.LENGTH_SHORT).show();

           } else {

                SocialNetwork socialNetwork = ((ApplicationManager)getApplication()).getInstance();

                if(socialNetwork!=null){
                    Toast.makeText(this,"sharing , please wait..",Toast.LENGTH_SHORT).show();
                    socialNetwork.requestPostPhoto(imageFile, "#Antar_Steel", new OnPostingCompleteListener() {
                    @Override
                    public void onPostSuccessfully(int socialNetworkID) {
                        Toast.makeText(getApplicationContext(),"have a nice day,photo shared successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                        Toast.makeText(getApplicationContext(),"problem with sharing,please try again",Toast.LENGTH_LONG).show();
                    }});
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * will be fired one user take a camera photo
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                imageFile = new File(getRealPathFromURI());
            }
        }
    }

    /**
     *
     * @return real path of captured image
     */
    public String getRealPathFromURI() {

        String fileSrc = null;
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
        if(cursor != null && cursor.moveToLast()){
            Uri fileURI = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            fileSrc = fileURI.toString();
            cursor.close();
        }
        return fileSrc;
    }

    CountDownTimer timer = new CountDownTimer(1 *60 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
            Log.d("timer : ", "started");
        }

        public void onFinish() {
            //Logout
            Log.d("timer : ","finish");
            SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("timerFinish",true);
            editor.commit();
        }
    };

    public void startBeaconService(){

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(yellowRegion);
                beaconManager.startMonitoring(blueRegion);
                beaconManager.startMonitoring(iceRegion);
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                if(list.size()>0) {
                    int major = list.get(0).getMajor();

                    // yellow Beacon
                    // welcome notification
                    if (major == Constant.YELLOW_MAJOR) {

                        SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);
                        if(preferences.getBoolean("firstWelcome",true) == true){
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("firstWelcome",false);
                            editor.commit();

                            showNotification(
                                    "yellow",
                                    "Welcome,it's nice to have you with us today ");
                        }

                        Toast.makeText(getApplicationContext(), "yello", Toast.LENGTH_SHORT).show();
                    }
                    // blue beacon
                    // registration page
                    else if (major == Constant.BLUE_MAJOR) {

                        SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);
                        if(preferences.getBoolean("firstRegister", true) == true
                                && preferences.getBoolean("RegisterFired", false) == false){
                            Intent intent = new Intent(getApplicationContext(),RegisterEnglish.class);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("firstRegister",false);
                            editor.commit();
                            timer.start();

                            startActivity(intent);
                        }
                    }
                    // ice beacon
                    // product info
                    else if (major == Constant.ICE_MAJOR) {

                        SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);

                        if(preferences.getBoolean("timerFinish", false) == true){

                            Intent intent = new Intent(getApplicationContext(),ImageSlider.class);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("timerFinish", false);
                            editor.commit();

                            timer.cancel();
                            timer.start();
                            startActivity(intent);

                            showNotification(
                                    "ice",
                                    "Welcome,it's nice to have you with us today ");

                            Log.d("ice : ","true");
                        }
                        Toast.makeText(getApplicationContext(), "ice", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onExitedRegion(Region region) {

            }
        });
    }

    @Subscribe
    public void OnStoreUserEvent(StatusRespone respone){
        /**
         * do what ever we want with the product info
         */
        Toast.makeText(this,respone.getStatus(),Toast.LENGTH_LONG).show();
        Log.d("status log :",respone.getStatus());
    }

    public void showNotification(String title, String message) {

        Intent notifyIntent = new Intent(this, SliderMenu.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
