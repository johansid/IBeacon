package com.app.market.screens;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.market.R;
import com.app.market.application.ApplicationManager;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnRequestSocialPersonCompleteListener {

    private SocialNetwork socialNetwork;
    private ImageView photo;
    private TextView name;
    private TextView mail;
    private TextView info;
    private RelativeLayout frame;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        frame = (RelativeLayout) rootView.findViewById(R.id.frame);
        photo = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);
        info = (TextView) rootView.findViewById(R.id.info);
        mail = (TextView) rootView.findViewById(R.id.tv_mail);

        socialNetwork = ((ApplicationManager)getActivity().getApplication()).getInstance();
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    @Override
    public void onRequestSocialPersonSuccess(int socialNetworkId, SocialPerson socialPerson) {
        name.setText(socialPerson.name);
        mail.setText(socialPerson.email);

        SharedPreferences preferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(preferences.getString("link","").equals("")){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("link",socialPerson.profileURL);
            editor.commit();
        }

        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(photo);
    }


    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}
