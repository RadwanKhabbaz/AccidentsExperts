package com.example.chemr.accidentsexperts;

import android.graphics.Bitmap;

public class DriverClass {
    String name ;
    String phone ;
    String driverImage ;
    String insuranceScanned ;

    public DriverClass(String name, String phone, String driverImage, String insuranceScanned) {
        this.name = name;
        this.phone = phone;
        this.driverImage = driverImage;
        this.insuranceScanned = insuranceScanned;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public String getInsuranceScanned() {
        return insuranceScanned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public void setInsuranceScanned(String insuranceScanned) {
        this.insuranceScanned = insuranceScanned;
    }
}
