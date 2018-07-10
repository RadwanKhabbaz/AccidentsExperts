package com.example.chemr.accidentsexperts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class choosingLocation extends AppCompatActivity {

    private EditText edLat , edLong;
    private Button btExpertLocNext;
    private ProgressBar proLoc;
    private String url = "https://radwankhabbaz.000webhostapp.com/insertDataOfAccidentsExperts.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_choosing_location );
        edLat = (EditText)findViewById( R.id.edLat );
        edLong = (EditText)findViewById(R.id.edLong );
        proLoc = (ProgressBar)findViewById( R.id.proLoc  );
        btExpertLocNext = (Button)findViewById( R.id.btExpertLocNext );
        final String edFN = getIntent().getStringExtra( "edFN" );
        final String edMN = getIntent().getStringExtra( "edMN" );
        final String edLN = getIntent().getStringExtra( "edLN" );
        final String edUN = getIntent().getStringExtra( "edUN" );
        final String edPass = getIntent().getStringExtra( "edPass" );
        final String edPh = getIntent().getStringExtra( "edPh" );
        final String isSyndicate = getIntent().getStringExtra( "isSyndicate" );
        final String edAddress = getIntent().getStringExtra( "edAddress" );
        Toast.makeText( choosingLocation.this, "Name: "+edFN+" "+ edMN+" "+edLN+"\n" + "username and pas:" + edUN + edPass+ "\n"+edPh +"\n"+isSyndicate+"\n"+"\n"+edAddress , Toast.LENGTH_LONG).show();
        builder = new AlertDialog.Builder( choosingLocation.this );

        btExpertLocNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proLoc.setVisibility( View.VISIBLE );
                RequestQueue rq = Volley.newRequestQueue( choosingLocation.this );
                StringRequest sr = new StringRequest( Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                Toast.makeText( choosingLocation.this,"reach",Toast.LENGTH_LONG ).show();
                                proLoc.setVisibility( View.INVISIBLE );
                                builder.setTitle( "Server Response" );
                                builder.setMessage( "Response :"+response );
                                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String y = response.substring( 2,8);
                                        Toast.makeText( choosingLocation.this,response.toString(),Toast.LENGTH_LONG ).show();
                                        if(y.equals( "Expert"))                                        {
                                            Intent i = new Intent( choosingLocation.this, ExpertSignupImages.class );
                                            i.putExtra( "edFN", edFN );
                                            i.putExtra( "edMN", edMN );
                                            i.putExtra( "edLN", edLN );
                                            i.putExtra( "edUN", edUN );
                                            //isSyndicate = ((RadioButton)findViewById(rbExpert.getCheckedRadioButtonId())).getText().toString();
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
                        proLoc.setVisibility( View.INVISIBLE );
                        Toast.makeText( choosingLocation.this,"Error..." + error.toString() ,Toast.LENGTH_SHORT ).show();
                        error.printStackTrace();
                    }
                } ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>(  );
                        params.put( "E_UserName",edUN );
                        params.put( "E_Password",edPass );
                        params.put( "E_FName",edFN);
                        params.put( "E_MName",edMN);
                        params.put( "E_LName",edLN);
                        params.put( "E_Phone",edPh);
                        params.put( "E_Address",edAddress );
                        if(isSyndicate.equals( "Yes" ))
                            params.put( "E_isInSyn", "T" );
                        else params.put( "E_isInSyn", "F" );
                        params.put( "E_lat", edLat.getText().toString() );
                        params.put( "E_lon", edLong.getText().toString() );
                        params.put( "E_ProfileImage", "Not upload yet" );
                        params.put( "E_ScannedCerteficate", "Not upload ye" );
                        params.put( "E_SyndicateID", "Not upload ye" );
                        return  params;
                    }
                };
                rq.add( sr );
            }
            });
        }
    }
//test
