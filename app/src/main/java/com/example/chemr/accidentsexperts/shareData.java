package com.example.chemr.accidentsexperts;

import android.app.Application;
import android.graphics.Bitmap;

public class shareData extends Application {
    private String Name;
    private Bitmap profile;

    public String getSomeVariable() {
        return Name;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }

    public void setSomeVariable(String Name) {
        this.Name = Name;
    }
}
