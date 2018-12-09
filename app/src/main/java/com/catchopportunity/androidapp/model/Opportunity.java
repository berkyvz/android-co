package com.catchopportunity.androidapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Opportunity {

    @SerializedName("oid")
    @Expose
    private Integer oid;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("desc1")
    @Expose
    private String desc1;
    @SerializedName("desc2")
    @Expose
    private String desc2;
    @SerializedName("desc3")
    @Expose
    private String desc3;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("city")
    @Expose
    private String city;

    public Opportunity() {
    }

    public Opportunity(Integer oid, String latitude, String longitude, String count, String desc1, String desc2, String desc3, String price, String city) {
        this.oid = oid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.count = count;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.price = price;
        this.city = city;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}