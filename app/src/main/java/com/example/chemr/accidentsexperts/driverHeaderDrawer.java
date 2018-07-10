package com.example.chemr.accidentsexperts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;


public class driverHeaderDrawer extends AppCompatActivity {

    private ImageView driverIV;
    private TextView driverName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_driver_header_drawer );
        driverIV = (ImageView)findViewById( R.id.driverIV );
        driverName = (TextView)findViewById( R.id.driverName );

        driverIV.setImageBitmap( StringToBitMap( getIntent().getStringExtra("image")));
        driverName.setText( getIntent().getStringExtra( "name" ) );
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
