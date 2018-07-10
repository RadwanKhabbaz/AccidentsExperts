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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Button btLogin;
    private Button btForgot;
    private EditText edUN , edPass ;
    private RadioGroup rbLogin;
    private String kindUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        btLogin = (Button)findViewById( R.id.btLogin );
        btForgot = (Button)findViewById( R.id.btForgot );
        edPass = (EditText)findViewById( R.id.edPass );
        edUN = (EditText)findViewById( R.id.edUN ) ;
        rbLogin = (RadioGroup)findViewById( R.id.rbLogin );

        btLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kindUser = ((RadioButton)findViewById(rbLogin.getCheckedRadioButtonId())).getText().toString();
                if(kindUser.equals("Expert")){
                   String username = edUN.getText().toString();
                   String password = edPass.getText().toString();
                   Intent i = new Intent( Login.this, Expert.class );
                   i.putExtra("username" , username );
                   i.putExtra("password" , password );
                   i.putExtra("kind" , kindUser );
                   startActivity( i );
                   finish();
                }
                else {
                   String username = edUN.getText().toString();
                   String password = edPass.getText().toString();
                   Intent i = new Intent( Login.this, driverInterface.class );
                   i.putExtra("username" , username );
                   i.putExtra("password" , password );
                   i.putExtra("kind" , kindUser );
                    startActivity( i );
                    finish();
                }
            }
        } );

        btForgot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );
    }
}
