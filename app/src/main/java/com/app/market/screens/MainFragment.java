package com.app.market.screens;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.app.market.R;
import com.app.market.api.events.StoreUserEvent;
import com.app.market.api.models.StatusRespone;
import com.app.market.application.ApplicationManager;
import com.app.market.bus.BusProvider;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {

    public static SocialNetworkManager mSocialNetworkManager = null;
    SocialNetwork socialNetwork;
    private Button facebook;
    private Bus mBus= BusProvider.getInstance();
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // init buttons and set Listener
        facebook = (Button) rootView.findViewById(R.id.facebook);
        facebook.setOnClickListener(loginClick);

        socialNetwork = ((ApplicationManager) getActivity().getApplication()).getInstance();
        if(socialNetwork!=null){
            if(socialNetwork.isConnected()){
                startAbout();
            }
        }

        //Chose permissions
        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));

        //Use manager to manage SocialNetworks
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(LoginActivity.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager FacebookSocialNetwork
            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, LoginActivity.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }

        return rootView;
    }


//     @Override
//     public void onStop() {
//         super.onStop();
//         getActivity().finish();
//     }


    @Subscribe
    public void OnStoreUserEvent(StatusRespone respone){
        /**
         * do what ever we want with the product info
         */
        Toast.makeText(getActivity(), respone.getStatus(), Toast.LENGTH_LONG).show();
        Log.d("status log :",respone.getStatus());
    }

    private void initSocialNetwork(SocialNetwork socialNetwork){
        if(socialNetwork.isConnected()){
            switch (socialNetwork.getID()){
                case FacebookSocialNetwork.ID:
                    facebook.setText("Show Facebook profile");
                    break;
            }
        }
    }
    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }

    //Login listener

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = 0;
            switch (view.getId()){
                case R.id.facebook:
                    networkId = FacebookSocialNetwork.ID;
                    break;
            }
            socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
            if(!socialNetwork.isConnected()) {
                if(networkId != 0) {
                    socialNetwork.requestLogin();
                } else {
                    Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                }
            } else {
                ((ApplicationManager) getActivity().getApplication()).setSocialManager(socialNetwork);
                startAbout();
            }
        }
    };

    @Override
    public void onLoginSuccess(int networkId) {

        ((ApplicationManager) getActivity().getApplication()).setSocialManager(socialNetwork);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id",networkId);
        editor.commit();

        startAbout();
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    public void startAbout(){
        Intent intent = new Intent(getActivity(), SliderMenu.class);
        startActivity(intent);
    }

}
