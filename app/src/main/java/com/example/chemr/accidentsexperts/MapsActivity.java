package com.example.chemr.accidentsexperts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private Double lat, lng;
    private final int FINE_LOCATION_PERMISSION = 9999;
    private currentLocation currentLocation;
    public ExpertClass []experts ;
    final String json_url ="https://radwankhabbaz.000webhostapp.com/getAllExperts.php";
    private Spinner sp1;
    private int type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        sp1 = (Spinner)findViewById( R.id.MapsspType );


        //get all experts
        RequestQueue rq = Volley.newRequestQueue( MapsActivity.this );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Request.Method.POST, json_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            experts = new ExpertClass[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject( i );
                                experts[i] = ( new ExpertClass(
                                        jsonObject.getString( "E_FName" ) + " " + jsonObject.getString( "E_MName" ) + " " + jsonObject.getString( "E_LName" )
                                        , jsonObject.getString( "E_Phone" )
                                        , jsonObject.getString( "E_Address" )
                                        , jsonObject.getString( "E_longitude" )
                                        , jsonObject.getString( "E_latitude" )
                                        , jsonObject.getString( "isInSyndicate" )
                                        , jsonObject.getString( "E_Image" )
                                ));}
                            try{
                                for (int i=0 ; i< experts.length ; i++){
                                    double lat = Double.parseDouble( experts[i].getLat() );
                                    double log = Double.parseDouble( experts[i].getLog() );
                                    LatLng expertPosition = new LatLng( lat,log);
                                    mMap.addMarker( new MarkerOptions().position( expertPosition ).title("Name:"+experts[i].getName()).snippet("Phone : "+experts[i].getPhone() ) );
                                }

                                sp1.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (position){
                                            case 0 :
                                                mMap.setMapType( GoogleMap.MAP_TYPE_TERRAIN );
                                                break;
                                            case 1 :
                                                mMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE );
                                                break;
                                            case 2 :
                                                mMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                } );



                            }catch (Exception e){
                                Toast.makeText( MapsActivity.this,e.toString(),Toast.LENGTH_LONG ).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText( MapsActivity.this,e.toString() ,Toast.LENGTH_SHORT ).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( MapsActivity.this, "There is an error, Cann't find experts\n try again pleas", Toast.LENGTH_SHORT ).show();
            }
        });
        rq.add( jsonArrayRequest );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION );
        }

        //Location Manager
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        provider = locationManager.getBestProvider( new Criteria(), false );
        Location location = locationManager.getLastKnownLocation( locationManager.NETWORK_PROVIDER );
        if (location != null)
            Log.i( "log info", "location Archived" );
        else Log.i( "log info", "No Location " );
      //  onLocationChanged( location );
         currentLocation = new currentLocation( location.getLatitude(), location.getLongitude());
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates( this );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION );
        }
        locationManager.requestLocationUpdates( provider, 400, 1, this );
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng myPosition = new LatLng( currentLocation.getLat(),currentLocation.getLng());
        Toast.makeText( MapsActivity.this,currentLocation.getLat() +" and long is "+ currentLocation.getLng() , Toast.LENGTH_SHORT).show();
        mMap.addMarker( new MarkerOptions().position( myPosition ).title( "You are here" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(myPosition,17) );
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.aaa);
       // mMap.moveCamera( CameraUpdateFactory.newLatLng( myPosition ) );

//        mMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE );
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
