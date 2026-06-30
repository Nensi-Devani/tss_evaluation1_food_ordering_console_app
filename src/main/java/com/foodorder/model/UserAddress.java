package com.foodorder.model;

import com.foodorder.common.Identifiable;

import java.io.Serializable;

public class UserAddress implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String mobileNumber;
    private String address;
    private String city;
    private String pincode;

    public UserAddress(String userId, String mobileNumber, String address, String city, String pincode) {
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
