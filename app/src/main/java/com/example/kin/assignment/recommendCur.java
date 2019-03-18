package com.example.kin.assignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class recommendCur extends AppCompatActivity {
    private  static final String TAG="recommendCur.java";
    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;

    Geocoder geocoder;
    List<Address> addresses;

    TextView tvLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_cur);

        tvLocation = (TextView)findViewById(R.id.tvLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},locationRequestCode);
        } else {
            // already permission granted
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    //tvLocation.setText(String.format(Locale.CHINESE, "%s -- %s", wayLatitude, wayLongitude));
                    Log.d(TAG,"inside function:"+String.valueOf(wayLongitude));

                    geocoder = new Geocoder(recommendCur.this,Locale.TRADITIONAL_CHINESE);
                    //geocoder = new Geocoder(this,Locale.getDefault());
                    try {
                        tvLocation.setText(String.valueOf(wayLatitude)+String.valueOf(wayLongitude));
                        addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        tvLocation.setText(address);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });



    }
}