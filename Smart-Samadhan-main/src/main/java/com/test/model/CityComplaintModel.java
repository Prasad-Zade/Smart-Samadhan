package com.test.model;

public class CityComplaintModel {
    private String cityName;
    private int complaintCount;

    public CityComplaintModel(String cityName, int complaintCount) {
        this.cityName = cityName;
        this.complaintCount = complaintCount;
    }

    public String getCityName() {
        return cityName;
    }

    public int getComplaintCount() {
        return complaintCount;
    }
}
