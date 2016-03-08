package com.app.market.api.models;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by OmarAli on 26/01/2016.
 *
 * * POJO class to map json response
 */
public class ProductResponse {
    @SerializedName("name")
    public String name;

    @SerializedName("salary")
    public String salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
