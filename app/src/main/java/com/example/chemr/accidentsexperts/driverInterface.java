package com.example.chemr.accidentsexperts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class driverInterface extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnFindExpert;
    private Button  btnEditDriver;
    private TextView tvDriverName , tvDriverPhone;
    private ImageView ivDriverImage;
    private ProgressBar prog1;
    private DriverClass dc;
    private String FName , LName, MName ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_driver_interface );
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        btnFindExpert = (Button)findViewById( R.id.btnFindExpert );
        btnEditDriver = (Button)findViewById( R.id.btnEditDriver );
        ivDriverImage = (ImageView)findViewById( R.id.ivDriverImage );
        tvDriverName = (TextView)findViewById( R.id.tvDriverName );
        tvDriverPhone = (TextView)findViewById( R.id.tvDriverPhone );
        prog1 = (ProgressBar)findViewById( R.id.prog1 );
        final String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String kind = getIntent().getStringExtra( "kind" );
        final String json_url = "https://radwankhabbaz.000webhostapp.com/accidentsExperts.php?UserName="+username+"&Password="+password+"&kind="+kind;
        prog1.setVisibility( View.VISIBLE );
        RequestQueue rq = Volley.newRequestQueue( driverInterface.this );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.POST, json_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            prog1.setVisibility( View.INVISIBLE );
                            JSONObject jsonObject = response.getJSONObject( 0 );
                            FName = jsonObject.getString( "D_FName" );
                            MName = jsonObject.getString( "D_MName" );
                            LName = jsonObject.getString( "D_LName" );
                            dc = new DriverClass(
                                    FName+" "+MName+" "+LName
                                    ,jsonObject.getString( "D_Phone" )
                                    ,jsonObject.getString( "D_Image" )
                                    ,jsonObject.getString( "D_insuranceScanned" )
                            );
                            tvDriverName.setText( dc.getName() );
                            tvDriverPhone.setText( dc.getPhone() );
                            ivDriverImage.setImageBitmap( StringToBitMap( dc.getDriverImage() ));
                            Intent dr = new Intent( driverInterface.this,driverHeaderDrawer.class );
                            dr.putExtra( "name",dc.getName() );
                            dr.putExtra( "image", dc.getDriverImage() );
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog1.setVisibility( View.INVISIBLE );
                Toast.makeText( driverInterface.this,"Invalid inputs try again please",Toast.LENGTH_SHORT ).show();
                Intent i = new Intent( driverInterface.this, Login.class );
                error.printStackTrace();
                startActivity( i );
                finish();
            }
        } );
        rq.add( jsonArrayRequest );


        btnEditDriver.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( driverInterface.this, DriverSignupImages.class );
                i.putExtra( "DedUN" , username );
                i.putExtra( "DedFN" , FName);
                i.putExtra("DedMN" , MName );
                i.putExtra( "DedLN", LName );
                startActivity( i );
            }
        } );

        btnFindExpert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( driverInterface.this, MapsActivity.class );
                startActivity( i );
            }
        });

    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
