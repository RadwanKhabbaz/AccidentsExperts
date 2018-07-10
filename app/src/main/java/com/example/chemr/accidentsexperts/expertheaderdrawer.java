package com.example.chemr.accidentsexperts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class expertheaderdrawer extends AppCompatActivity {

    private ImageView expertIV ;
    private TextView expertName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expertheaderdrawer );
        expertIV = (ImageView)findViewById( R.id.expertIV );
        expertName = (TextView)findViewById( R.id.expertName );
        expertIV.setImageBitmap(((shareData) this.getApplication()).getProfile());





    }



}
