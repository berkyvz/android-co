package com.catchopportunity.androidapp.helpermodel;

import com.catchopportunity.androidapp.model.Opportunity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpportunityItem {

    @SerializedName("opportunity")
    @Expose
    private Opportunity opportunity;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("name")
    @Expose
    private String name;

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
