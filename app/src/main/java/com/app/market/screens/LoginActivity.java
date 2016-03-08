package com.app.market.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.market.R;
import com.app.market.api.events.StoreUserEvent;
import com.app.market.api.models.StatusRespone;
import com.app.market.application.ApplicationManager;
import com.app.market.bus.BusProvider;
import com.estimote.sdk.SystemRequirementsChecker;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LoginActivity extends AppCompatActivity  {

    static Context context;
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check if user already login in before
//        SharedPreferences preferences = getSharedPreferences("myPref",this.MODE_PRIVATE);
//        SocialNetwork mSocialNetworkManager = ((ApplicationManager) getApplication()).getInstance();
//        if(mSocialNetworkManager!=null && preferences.getInt("id",0)!=0){
//            Intent i = new Intent(this,SliderMenu.class);
//            startActivity(i);
//        }

        context = this;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
