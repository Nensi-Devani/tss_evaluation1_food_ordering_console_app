package com.foodorder.model;

import com.foodorder.common.Identifiable;
import com.foodorder.enums.Status;

import java.io.Serializable;

public class Restaurant implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String ownerId;
    private String name;
    private String mobileNumber;
    private String address;
    private String city;
    private String pincode;
    private Status status;

    public Restaurant(){
    }

    public Restaurant(String ownerId, String name, String mobileNumber, String address, String city, String pincode, Status status) {
        this.ownerId = ownerId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
