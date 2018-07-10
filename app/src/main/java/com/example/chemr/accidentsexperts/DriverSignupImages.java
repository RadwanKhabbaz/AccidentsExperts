package com.example.chemr.accidentsexperts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DriverSignupImages extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageButton btInsurance, btDriver ;
    private Button btSubmitImages;
    private boolean driver=false , insurance=false ;
    private  String url = "https://radwankhabbaz.000webhostapp.com/updateDriver.php" ;
    AlertDialog.Builder builder;
    private Bitmap driverImage , insurenceImage;
    String drIm = " ", inIm= " ";
    private ProgressBar progressBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_driver_signup_images );
        btDriver = (ImageButton) findViewById( R.id.btDriver );
        btInsurance = (ImageButton) findViewById( R.id.btInsurance );
        btSubmitImages = (Button)findViewById( R.id.btSubmitImages );
        progressBar = (ProgressBar)findViewById( R.id.progressBar );
        builder = new AlertDialog.Builder( DriverSignupImages.this );
        final String edFN = getIntent().getStringExtra( "DedFN" );
        final String edMN = getIntent().getStringExtra( "DedMN" );
        final String edLN = getIntent().getStringExtra( "DedLN" );
        final String edUN = getIntent().getStringExtra( "DedUN" );

        btSubmitImages.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility( View.VISIBLE );
                RequestQueue rq = Volley.newRequestQueue( DriverSignupImages.this );
                StringRequest sr = new StringRequest( Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                progressBar.setVisibility( View.INVISIBLE );
                                builder.setTitle( "Server Response" );
                                builder.setMessage( "Response :"+response +"for "+edFN +" "+edLN );
                                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String y = response.substring( 0,6);
                                        if(y.equals( "Driver"))
                                        {
                                            Intent i = new Intent( DriverSignupImages.this,Login.class );

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
                        Toast.makeText( DriverSignupImages.this,"Error..." + error.toString() ,Toast.LENGTH_SHORT ).show();
                        error.printStackTrace();
                    }
                } ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>(  );
                        params.put( "D_UserName",edUN );
                        params.put( "D_Image", drIm );
                        params.put( "D_insuranceScanned", inIm );
                        return  params;
                    }
                };
                int socketTimeout = 360000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                sr.setRetryPolicy(policy);
                rq.add( sr );

            }
        } );

        btDriver.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driver = true;
                insurance = false;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        } );
        btInsurance.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driver = false;
                insurance = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        } );

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult( reqCode, resultCode, data );


        if (driver == true) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                    btDriver.setImageBitmap( selectedImage );
                    driverImage = selectedImage;
                    drIm  = toBase64( driverImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText( DriverSignupImages.this, "Something went wrong", Toast.LENGTH_LONG ).show();
                }

            } else {
                Toast.makeText( DriverSignupImages.this, "You haven't picked Image", Toast.LENGTH_LONG ).show();
            }
        }
        if (insurance == true) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                    btInsurance.setImageBitmap( selectedImage );
                    insurenceImage = selectedImage;
                    inIm = toBase64( insurenceImage );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText( DriverSignupImages.this, "Something went wrong", Toast.LENGTH_LONG ).show();
                }

            } else {
                Toast.makeText( DriverSignupImages.this, "You haven't picked Image", Toast.LENGTH_LONG ).show();
            }
        }
    }
    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}
