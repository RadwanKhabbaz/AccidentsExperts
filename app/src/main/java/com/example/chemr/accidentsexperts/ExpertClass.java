package com.example.chemr.accidentsexperts;

public class ExpertClass {
    private String Name;
    private String Phone;
    private String Address;
    private String Lat;
    private String Log;
    private String isSyndicate;
    private String image;
    private String certificateScanned;
    private String syndicateIDScanned;

    public ExpertClass(String name, String phone, String address, String lat, String log, String isSyndicate, String image) {
        Name = name;
        Phone = phone;
        Address = address;
        Lat = lat;
        Log = log;
        this.isSyndicate = isSyndicate;
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLog() {
        return Log;
    }

    public void setLog(String log) {
        Log = log;
    }

    public String getIsSyndicate() {
        return isSyndicate;
    }

    public void setIsSyndicate(String isSyndicate) {
        this.isSyndicate = isSyndicate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCertificateScanned() {
        return certificateScanned;
    }

    public void setCertificateScanned(String certificateScanned) {
        this.certificateScanned = certificateScanned;
    }

    public String getSyndicateIDScanned() {
        return syndicateIDScanned;
    }

    public void setSyndicateIDScanned(String syndicateIDScanned) {
        this.syndicateIDScanned = syndicateIDScanned;
    }
}
