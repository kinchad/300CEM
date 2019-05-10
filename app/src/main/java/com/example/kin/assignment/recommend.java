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
    private  static final String TAG="recommend.java";
    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private String recommendCur="CNY";
    String responseString;
    JSONArray jsonArray;
    ArrayList<String> stringArray = new ArrayList<String>();
    private ListView lvCurrency;
    private String userid;

    Geocoder geocoder;
    List<Address> addresses;

    TextView tvLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    Log.d(TAG,"inside function:"+String.valueOf(wayLongitude));

                    geocoder = new Geocoder(recommend.this, Locale.TRADITIONAL_CHINESE);
                    try {
                        addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        String country = addresses.get(0).getCountryName();
                        String area = addresses.get(0).getAdminArea();
                        String locality = addresses.get(0).getLocality();
                        String street = addresses.get(0).getThoroughfare();
                        String streetNumber = addresses.get(0).getFeatureName();
                        String postalCode = addresses.get(0).getPostalCode();

                        //tvLocation.setText(String.valueOf(wayLatitude)+String.valueOf(wayLongitude));
                        Log.e("Country",country);
                        if(country.equals("美國")){
                            recommendCur = "USD";
                        }else if(country.equals("中國") || country.equals("香港")){
                            recommendCur = "CNY";
                        }else if(country.equals("日本")){
                            recommendCur = "JPY";
                        }

                        new httpClient().execute();
                        lvCurrency = findViewById(R.id.lvCurrency);

                        lvCurrency.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(getBaseContext(), stringArray.get(position)+" Item clicked!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getBaseContext(),currencyDetail.class);
                                        intent.putExtra("currencyName",stringArray.get(position));
                                        intent.putExtra("userid",userid);
                                        startActivity(intent);
                                    }
                                }
                        );
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recommend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
            startActivity(intent);
        }else if (id == R.id.nav_manage) {

        }else if (id == R.id.nav_share) {

        }else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try{
                                jsonArray = new JSONArray(responseString);
                                //Log.e("myLog",jsonArray.toString());

                                for(int i=0; i < jsonArray.length(); i++) {
                                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                                    String name = jsonobject.getString("name");
                                    stringArray.add(name);

                                    for(String str: stringArray){
                                        //Log.e("Currency Pairs:",str);
                                    }
                                }
                            }catch (Throwable t){
                                Log.e("Exception t catch",t.toString());
                            }
                        }
                    });
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString());
                e.printStackTrace();
            }
            finally {
                if (connection != null) {                    connection.disconnect();                }
            }
            return stringArray;
        }
        //return to UI thread
        protected void onPostExecute(ArrayList<String> arrayList){
            super.onPostExecute(arrayList);
            ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,arrayList);
            lvCurrency.setAdapter(adapter);
        }
    }
}
