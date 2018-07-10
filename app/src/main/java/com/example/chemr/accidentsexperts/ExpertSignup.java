package com.example.chemr.accidentsexperts;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ExpertSignup extends AppCompatActivity {

    private Button btExpertNext;
    private EditText edFN, edLN, edMN, edUN, edPass, edPh, edAddress ;
    private RadioGroup rbExpert , rbLocationExpert;
    private String isSyndicate , usingCurrentLocation ;
    private String url = "https://radwankhabbaz.000webhostapp.com/insertDataOfAccidentsExperts.php";
    AlertDialog.Builder builder;
    private ProgressBar progSignup;
    private LocationManager locationManager;
    private Double lat, lng;
    private currentLocation currentLocation;
    private String provider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert_signup );
        btExpertNext = (Button)findViewById( R.id.btExpertNext );
        edFN = (EditText)findViewById( R.id.edFN );
        edMN = (EditText)findViewById( R.id.edMN );
        edLN = (EditText)findViewById( R.id.edLN );
        edUN = (EditText)findViewById( R.id.edUN );
        edPass = (EditText)findViewById( R.id.edPass );
        edPh = (EditText)findViewById( R.id.edPh );
        edAddress = (EditText)findViewById( R.id.edAddress );
        rbExpert = (RadioGroup)findViewById( R.id.rbExpert );
        rbLocationExpert = (RadioGroup)findViewById( R.id.rbLocationExpert );
        progSignup = (ProgressBar)findViewById( R.id.progSignup );
        builder = new AlertDialog.Builder( ExpertSignup.this );
        btExpertNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usingCurrentLocation = ((RadioButton)findViewById(rbLocationExpert.getCheckedRadioButtonId())).getText().toString();
                if(usingCurrentLocation.equals( "Yes" )){
                progSignup.setVisibility( View.VISIBLE );
                locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                provider = locationManager.getBestProvider( new Criteria(), false );
                if (ActivityCompat.checkSelfPermission( ExpertSignup.this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( ExpertSignup.this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation( locationManager.NETWORK_PROVIDER );
                if (location != null)
                    Log.i( "log info", "location Archived" );
                else Log.i( "log info", "No Location " );
                //  onLocationChanged( location );
                currentLocation = new currentLocation( location.getLatitude(), location.getLongitude());
                RequestQueue rq = Volley.newRequestQueue( ExpertSignup.this );
                StringRequest sr = new StringRequest( Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                progSignup.setVisibility( View.INVISIBLE );
                                builder.setTitle( "Server Response" );
                                builder.setMessage( "Response :"+response );
                                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String y = response.substring( 2,8);
                                        if(y.equals( "Expert"))                                        {
                                            Intent i = new Intent( ExpertSignup.this, ExpertSignupImages.class );
                                            i.putExtra( "edFN", edFN.getText().toString() );
                                            i.putExtra( "edMN", edMN.getText().toString() );
                                            i.putExtra( "edLN", edLN.getText().toString() );
                                            i.putExtra( "edUN", edUN.getText().toString() );
                                            if(isSyndicate.equals( "Yes" ))
                                                i.putExtra( "isInSyndicate", "T" );
                                            else i.putExtra( "isInSyndicate", "F" );
                                            startActivity( i );
                                            finish();
                                        }
                                    }
                                } );
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progSignup.setVisibility( View.INVISIBLE );
                        Toast.makeText( ExpertSignup.this,"Error..." + error.toString() ,Toast.LENGTH_SHORT ).show();
                        error.printStackTrace();
                    }
                } ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>(  );
                            params.put( "E_UserName",edUN.getText().toString() );
                            params.put( "E_Password",edPass.getText().toString() );
                            params.put( "E_FName",edFN.getText().toString() );
                            params.put( "E_MName",edMN.getText().toString() );
                            params.put( "E_LName",edLN.getText().toString() );
                            params.put( "E_Phone",edPh.getText().toString() );
                            params.put( "E_Address",edAddress.getText().toString() );
                        isSyndicate = ((RadioButton)findViewById(rbExpert.getCheckedRadioButtonId())).getText().toString();
                        if(isSyndicate.equals( "Yes" ))
                                params.put( "E_isInSyn", "T" );
                            else params.put( "E_isInSyn", "F" );
                            params.put( "E_lat", currentLocation.getLat().toString() );
                            params.put( "E_lon", currentLocation.getLng().toString() );
                            params.put( "E_ProfileImage", "Not upload yet" );
                            params.put( "E_ScannedCerteficate", "Not upload ye" );
                            params.put( "E_SyndicateID", "Not upload ye" );
                        return  params;
                    }
                };
                rq.add( sr );
            }
            else {
                    Intent i = new Intent( ExpertSignup.this , choosingLocation.class  );
                    i.putExtra( "edFN", edFN.getText().toString() );
                    i.putExtra( "edMN", edMN.getText().toString() );
                    i.putExtra( "edLN", edLN.getText().toString() );
                    i.putExtra( "edUN", edUN.getText().toString() );
                    i.putExtra( "edPass", edPass.getText().toString() );
                    i.putExtra( "edPh", edPh.getText().toString() );
                    isSyndicate = ((RadioButton)findViewById(rbExpert.getCheckedRadioButtonId())).getText().toString();
                    i.putExtra( "isSyndicate", isSyndicate );
                    i.putExtra( "edAddress", edAddress.getText().toString() );
                    startActivity( i );

                }
            }
        } );
    }
}
