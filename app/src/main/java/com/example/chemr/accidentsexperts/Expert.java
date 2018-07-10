package com.example.chemr.accidentsexperts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Expert extends AppCompatActivity {

    private Button btnEditExpert;
    private ImageView ivExpertImage , ivCertificate , ivSyndicateID;
    private TextView tvExpertName , tvExpertPhone , tvExpertAddress;
    private ProgressBar prog1;
    private String FName , LName, MName, isInSyndicate , username;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert );

        tvExpertAddress = (TextView)findViewById( R.id.tvExpertAddress );
        tvExpertName = (TextView)findViewById( R.id.tvExpertName );
        tvExpertPhone = (TextView)findViewById( R.id.tvExpertPhone );
        btnEditExpert = (Button)findViewById( R.id.btnEditExpert );
        ivCertificate = (ImageView)findViewById( R.id.ivCertificate );
        ivExpertImage = (ImageView)findViewById( R.id.ivExpertImage );
        ivSyndicateID = (ImageView)findViewById( R.id.ivSyndicateID );
        prog1 = (ProgressBar)findViewById( R.id.prog1 );
        username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String kind = getIntent().getStringExtra( "kind" );
        final String json_url = "https://radwankhabbaz.000webhostapp.com/accidentsExperts.php?UserName="+username+"&Password="+password+"&kind="+kind;
        prog1.setVisibility( View.VISIBLE );
        RequestQueue rq = Volley.newRequestQueue( Expert.this );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.POST, json_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            prog1.setVisibility( View.INVISIBLE );//progresspar
                            JSONObject jsonObject = response.getJSONObject( 0 );
                            FName = jsonObject.getString( "E_FName" );
                            MName = jsonObject.getString( "E_MName" );
                            LName = jsonObject.getString( "E_LName" );
                            tvExpertName.setText(FName+" "+MName+" "+LName);
                            tvExpertPhone.setText( jsonObject.getString( "E_Phone" ) );
                            tvExpertAddress.setText(jsonObject.getString( "E_Address" ) );
                            isInSyndicate =  jsonObject.getString( "isInSyndicate" ) ;
                            ivExpertImage.setImageBitmap( StringToBitMap( jsonObject.getString( "E_Image" ) ) );
                            ivCertificate.setImageBitmap( StringToBitMap( jsonObject.getString( "E_Certificate") ) );
                            ivSyndicateID.setImageBitmap( StringToBitMap( jsonObject.getString( "E_SyndicateID" ) ) );
                        }

                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },  new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prog1.setVisibility( View.INVISIBLE );
                        Toast.makeText( Expert.this,"Invalid inputs try again please",Toast.LENGTH_SHORT ).show();
                        Intent i = new Intent( Expert.this, Login.class );
                        error.printStackTrace();
                        startActivity( i );
                        finish();
                    }
                } );
            rq.add( jsonArrayRequest );

        btnEditExpert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Expert.this, ExpertSignupImages.class);
                i.putExtra( "edFN" , FName);
                i.putExtra( "edMN" , MName);
                i.putExtra( "edLN" , LName);
                i.putExtra( "edUN", username );
                i.putExtra( "isInSyndicate" , isInSyndicate );
                startActivity( i );
            }
        } );
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode( encodedString, Base64.DEFAULT );
            Bitmap bitmap = BitmapFactory.decodeByteArray( encodeByte, 0,
                    encodeByte.length );
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
