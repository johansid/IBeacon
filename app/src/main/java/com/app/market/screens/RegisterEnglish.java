package com.app.market.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.market.R;
import com.app.market.api.events.StoreUserEvent;
import com.app.market.api.models.ProductResponse;
import com.app.market.api.models.StatusRespone;
import com.app.market.application.ApplicationManager;
import com.app.market.bus.BusProvider;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEnglish extends AppCompatActivity implements Validator.ValidationListener,OnRequestSocialPersonCompleteListener {

    @NotEmpty(message = "please enter your name")
    EditText et_Name;
    @Email(message = "wrong email address")
    EditText et_mail;
    @NotEmpty(message = "wrong phone number")
    EditText et_phone;
    @NotEmpty(message = "please enter your company")
    EditText et_company;
    @NotEmpty(message = "please enter your code")
    EditText et_code;

    Button btn_submit;
    Validator validator;
    private Bus mBus= BusProvider.getInstance();
    SocialNetwork socialNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_english);
        et_Name = (EditText)findViewById(R.id.name);
        et_company = (EditText)findViewById(R.id.company);
        et_mail = (EditText)findViewById(R.id.email);
        et_phone = (EditText)findViewById(R.id.phone);
        et_code = (EditText)findViewById(R.id.code);

        btn_submit = (Button)findViewById(R.id.btn_submit);
        validator = new Validator(this);
        validator.setValidationListener(this);

        SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("RegisterFired",true);
        editor.commit();

        socialNetwork = ((ApplicationManager)getApplication()).getInstance();
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getProfileLink().equals("")){
                    socialNetwork.requestCurrentPerson();
                }else{
                    validator.validate();
                }

            }
        });

    }

    @Subscribe
    public void OnStoreUserEvent(StatusRespone respone){
        /**
         * do what ever we want with the product info
         */
        Toast.makeText(this,respone.getStatus(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onValidationSucceeded() {
        String phone = et_phone.getText().toString();
        String name = et_Name.getText().toString();
        String code = et_code.getText().toString();

        if (phone.matches("[0-9]+") && phone.length() == 11   && IsValid(name)){

            SharedPreferences preferences = getSharedPreferences("beacon",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RegisterDone",true);
            editor.commit();

            StoreUserEvent storeUserEvent = new StoreUserEvent(name
                    ,phone
                    ,et_mail.getText().toString()
                    ,et_company.getText().toString()
                    ,getProfileLink()
                    ,code);

            mBus.post(storeUserEvent);

        }else if(phone.matches("[0-9]+") && phone.length() != 11 ){

            et_phone.setError("wrong phone number");

        }else if(!IsValid(name)){
            et_Name.setError("wrong name");
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Boolean IsValid(String text) {
        Pattern p = Pattern.compile("([0-9])");
        Matcher m = p.matcher(text);

        if (m.find()) {
            return false;
        }
        return true;
    }

    /**
     * get profile link from shared pref
     * @return String user profile link
     */
    public String getProfileLink(){
        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String link = preferences.getString("link","");
        return link;
    }

    @Override
    public void onRequestSocialPersonSuccess(int socialNetworkId, SocialPerson socialPerson) {
        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("link",socialPerson.profileURL);
        editor.commit();

        validator.validate();
    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        Toast.makeText(this,"please check internet connection and try again",Toast.LENGTH_SHORT).show();
    }
}
