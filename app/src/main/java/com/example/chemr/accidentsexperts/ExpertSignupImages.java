package com.example.chemr.accidentsexperts;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class ExpertSignupImages extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageButton btExpert, btID_Expert, btCertificate_Expert;
    private TextView tvCert, tvID;
    private boolean expert = false, id = false, certificate = false;
    private Button btSubmitImages;
    private String url = "https://radwankhabbaz.000webhostapp.com/updateExpert.php";
    AlertDialog.Builder builder;
    private Bitmap profile, scannedId, scannedCertificate;
    String pr = " ", ID = " ", cert = " ";
    private ProgressBar progSignupi;
    private LocationManager locationManager;
    private Double lat, lng;
    private currentLocation currentLocation;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert_signup_images );
        btExpert = (ImageButton) findViewById( R.id.btExpert );
        btID_Expert = (ImageButton) findViewById( R.id.btID_Expert );
        btCertificate_Expert = (ImageButton) findViewById( R.id.btCertificate_Expert );
        btSubmitImages = (Button) findViewById( R.id.btSubmitImages );
        builder = new AlertDialog.Builder( ExpertSignupImages.this );
        progSignupi = (ProgressBar) findViewById( R.id.progSignupi );
        tvCert = (TextView) findViewById( R.id.tvCert );
        tvID = (TextView) findViewById( R.id.tvID );
        final String edFN = getIntent().getStringExtra( "edFN" );
        final String edMN = getIntent().getStringExtra( "edMN" );
        final String edLN = getIntent().getStringExtra( "edLN" );
        final String edUN = getIntent().getStringExtra( "edUN" );
        final String isInSyndicate = getIntent().getStringExtra( "isInSyndicate" );
        if (isInSyndicate.equals( "F" )) {
            btID_Expert.setVisibility( View.INVISIBLE );
            tvID.setVisibility( View.INVISIBLE );
            addOrRemoveProperty( btCertificate_Expert, RelativeLayout.CENTER_IN_PARENT, true );
            addOrRemoveProperty( tvCert, RelativeLayout.CENTER_HORIZONTAL, true );
        }



        btSubmitImages.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progSignupi.setVisibility( View.VISIBLE );
                RequestQueue rq = Volley.newRequestQueue( ExpertSignupImages.this );
                StringRequest sr = new StringRequest( Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                progSignupi.setVisibility( View.INVISIBLE );
                                builder.setTitle( "Server Response" );
                                builder.setMessage( "Response :"+response +"for "+edFN +" "+edLN );
                                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String y = response.substring(0, 6);
                                        if(y.equals( "Expert"))                                        {
                                            Intent i = new Intent( ExpertSignupImages.this, Login.class );
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
                        progSignupi.setVisibility( View.INVISIBLE );
                        Toast.makeText( ExpertSignupImages.this,"Error...",Toast.LENGTH_SHORT ).show();
                        error.printStackTrace();
                    }
                } ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>(  );
                        params.put( "E_UserName",edUN );
                        params.put( "E_ProfileImage", pr );
                        params.put( "E_ScannedCerteficate", cert );
                        if(isInSyndicate.equals( "T" ))
                            params.put( "E_SyndicateID", ID );
                        else params.put( "E_SyndicateID", "is not in syndicate" );
                        return  params;
                    }
                };

                int socketTimeout = 360000;//30 seconds - change to what you want
                  RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                  sr.setRetryPolicy(policy);
                rq.add( sr );
            }
        });
        btExpert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expert = true;
                id = false;
                certificate = false;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        } );
        btID_Expert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expert = false;
                id = true;
                certificate = false;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);

            }
        } );
        btCertificate_Expert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expert = false;
                id = false;
                certificate = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        } );

    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult( reqCode, resultCode, data );


        if (expert == true) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                    profile = selectedImage;
                    btExpert.setImageBitmap( selectedImage );
                    pr = toBase64( profile );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText( ExpertSignupImages.this, "Something went wrong", Toast.LENGTH_LONG ).show();
                }

            } else {
                Toast.makeText( ExpertSignupImages.this, "You haven't picked Image", Toast.LENGTH_LONG ).show();
            }
        }
        if (id == true) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImageId = BitmapFactory.decodeStream( imageStream );
                    scannedId = selectedImageId;
                    btID_Expert.setImageBitmap( selectedImageId );
                    ID =toBase64( scannedId );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText( ExpertSignupImages.this, "Something went wrong", Toast.LENGTH_LONG ).show();
                }

            } else {
                Toast.makeText( ExpertSignupImages.this, "You haven't picked Image", Toast.LENGTH_LONG ).show();
            }
        }
        if (certificate == true) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                    final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                    scannedCertificate = selectedImage;
                    btCertificate_Expert.setImageBitmap( selectedImage );
                    cert = toBase64( scannedCertificate );


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText( ExpertSignupImages.this, "Something went wrong", Toast.LENGTH_LONG ).show();
                }

            } else {
                Toast.makeText( ExpertSignupImages.this, "You haven't picked Image", Toast.LENGTH_LONG ).show();
            }
        }
    }
    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void addOrRemoveProperty(View view, int property, boolean flag){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(flag){
            layoutParams.addRule(property);
        }else layoutParams.removeRule( property );
        view.setLayoutParams(layoutParams);
    }
}
