package com.example.chemr.accidentsexperts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class driverSignup extends AppCompatActivity {


    private Toolbar toolbar;
    private Button btDriverNext;
    private EditText edD_FN, edD_LN, edD_MN, edD_UN, edD_Pass, edD_Ph;
    private  String url = "https://radwankhabbaz.000webhostapp.com/insertDataOfAccidentsDrivers.php" ;
    AlertDialog.Builder builder;
    private ProgressBar progressBar ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_driver_signup );
        btDriverNext = (Button)findViewById( R.id.btDriverNext );
        edD_FN = (EditText)findViewById( R.id.edD_FN );
        edD_LN = (EditText)findViewById( R.id.edD_LN );
        edD_MN = (EditText)findViewById( R.id.edD_MN );
        edD_UN = (EditText)findViewById( R.id.edD_UN );
        edD_Pass = (EditText)findViewById( R.id.edD_Pass );
        edD_Ph = (EditText)findViewById( R.id.edD_Ph );
        progressBar = (ProgressBar)findViewById( R.id.progD );
        builder = new AlertDialog.Builder( driverSignup.this );


        btDriverNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility( View.VISIBLE );
                RequestQueue rq = Volley.newRequestQueue( driverSignup.this );
                StringRequest sr = new StringRequest( Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                progressBar.setVisibility( View.INVISIBLE );
                                builder.setTitle( "Server Response" );
                                builder.setMessage( "Response :"+response );
                                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String y = response.substring( 2,8);
                                        if(y.equals( "Driver"))
                                        {
                                            Intent i = new Intent( driverSignup.this,DriverSignupImages.class );
                                            i.putExtra( "DedFN", edD_FN.getText().toString() );
                                            i.putExtra( "DedMN", edD_MN.getText().toString() );
                                            i.putExtra( "DedLN", edD_LN.getText().toString() );
                                            i.putExtra( "DedUN", edD_UN.getText().toString() );
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
                        progressBar.setVisibility( View.INVISIBLE );
                        Toast.makeText( driverSignup.this,"Error..." +error.toString(),Toast.LENGTH_SHORT ).show();
                        error.printStackTrace();
                    }
                } ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>(  );
                        params.put( "D_UserName",edD_UN.getText().toString() );
                        params.put( "D_Password",edD_Pass.getText().toString() );
                        params.put( "D_FName",edD_FN.getText().toString() );
                        params.put( "D_MName",edD_LN.getText().toString() );
                        params.put( "D_LName",edD_MN.getText().toString() );
                        params.put( "D_Phone",edD_Ph.getText().toString() );
                        params.put( "D_Image", "Not uploaded yet" );
                        params.put( "D_insuranceScanned", "Not uploaded yet" );
                        return  params;
                    }
                };
//                int socketTimeout = 360000;//30 seconds - change to what you want
//                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                sr.setRetryPolicy(policy);
                rq.add( sr );

            }
        } );
    }
}

