package com.example.kin.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class recommend extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userid;
    private static final String TAG="recommend.java";
    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private String recommendCur="CNY";

    //parameters  used in http request client
    private String responseString;
    private JSONArray jsonArray;
    private ArrayList<String> stringArray = new ArrayList<String>();
    private ListView lvCurrency;

    private Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create the navigation drawer interface
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //indentify the login user
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},locationRequestCode);
        } else {  } // already permission granted
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
            if (location != null) {      // Got last known location. In some rare situations this can be null.
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();

                geocoder = new Geocoder(recommend.this, Locale.TRADITIONAL_CHINESE);
                try {   //check the user location, recommend the related currency
                    addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
                    String country = addresses.get(0).getCountryName();

                    if(country.equals("美國")){
                        recommendCur = "USD";
                    }else if(country.equals("中國") || country.equals("香港")){
                        recommendCur = "CNY";
                    }else if(country.equals("日本")){
                        recommendCur = "JPY";
                    }

                    new httpClient().execute();    //make http request to API
                    lvCurrency = findViewById(R.id.lvCurrency);

                    lvCurrency.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getBaseContext(),currencyDetail.class);
                                    intent.putExtra("currencyName",stringArray.get(position));
                                    intent.putExtra("userid",userid);
                                    startActivity(intent);
                                }
                            }
                    );
                }catch (IOException e){
                    Log.e("Connection fail:",e.toString());     //display exception in logcat
                }
            }
            }
        });
    }
    @Override
    public void onBackPressed() {      //press back button
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {      // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recommend, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {     // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homepage) {
            Intent intent = new Intent(this,homePage.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }else if(id==R.id.favour) {
            Intent intent = new Intent(this,favour.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }else if (id == R.id.recommend) {
            Intent intent = new Intent(this,recommend.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }else if (id == R.id.converter) {
            Intent intent = new Intent(this,converter.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }else if (id == R.id.settings) {
            Intent intent = new Intent(this,settings.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
            finish();
        }else if (id == R.id.about) {
            Intent intent = new Intent(this,about.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }else if (id == R.id.privacy) {
            Intent intent = new Intent(this,privacy.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class httpClient extends AsyncTask<Void, Void, ArrayList<String>> {
        protected ArrayList<String> doInBackground(Void... params) {
            Log.e("Http background service",recommendCur);
            String urlString = "http://10.112.160.105:7777/getCurrencyBySimilarName?name="+recommendCur;
            HttpURLConnection connection = null;
            try {
                // create URL & connection
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                // set request timeout
                connection.setReadTimeout(1500);
                connection.setConnectTimeout(1500);
                // simulate Chrome's user agent, mobile browser may have compatible problems
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
                // set auto-redirect
                connection.setInstanceFollowRedirects(true);

                //return 200 -> request successful
                if( connection.getResponseCode() == HttpsURLConnection.HTTP_OK ){
                    // read the website
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader  = new BufferedReader( new InputStreamReader(inputStream) );

                    String tempStr;
                    StringBuffer stringBuffer = new StringBuffer();

                    while( ( tempStr = bufferedReader.readLine() ) != null ) {
                        stringBuffer.append( tempStr );
                    }

                    bufferedReader.close();
                    inputStream.close();

                    // get the string from website
                    responseString = stringBuffer.toString();

                    try{
                        jsonArray = new JSONArray(responseString);

                        for(int i=0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String name = jsonobject.getString("name");
                            stringArray.add(name);
                        }
                    }catch (Throwable t){
                        Log.e("Exception catch",t.toString());  //display error log to logcat
                    }
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString()); //display exception in logcat
            }
            finally {
                if (connection != null) {                    connection.disconnect();                }
            }
            return stringArray;
        }
        protected void onPostExecute(ArrayList<String> arrayList){  //return to UI thread
            super.onPostExecute(arrayList);
            ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,arrayList);
            lvCurrency.setAdapter(adapter);     //set up the favour currency list
        }
    }
}
