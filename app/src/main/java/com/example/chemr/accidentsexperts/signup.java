package com.example.chemr.accidentsexperts;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class signup extends AppCompatActivity {


    private Button btnExpert ,btnClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        btnExpert = (Button)findViewById( R.id.btnExpert );
        btnClient = (Button)findViewById( R.id.btnClient );


        btnExpert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent( signup.this, ExpertSignup.class );
                startActivity( i );
            }
        } );

        btnClient.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( signup.this , driverSignup.class );
                startActivity( i );
            }
        } );

    }
}
