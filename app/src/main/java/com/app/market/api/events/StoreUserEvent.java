package com.app.market.api.events;

/**
 * Created by OmarAli on 15/02/2016.
 */
public class StoreUserEvent {

    private String name;
    private String phone;
    private String email;
    private String company;
    private String profile;
    private String code;

    public StoreUserEvent(String name,String phone,String mail,String company,String profile,String code){
        this.name = name;
        this.phone = phone;
        this.email = mail;
        this.company = company;
        this.profile = profile;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getProfile() {
        return profile;
    }

    public String getCode() {
        return code;
    }
}
