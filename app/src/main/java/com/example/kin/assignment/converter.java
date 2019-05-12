package com.example.kin.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class converter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner baseSpinner;
    Spinner targetSpinner;
    EditText etBase;
    Button btnConvert;
    TextView tvTarget;

    String baseCurrency;
    String targetCurrency;

    String responseString;
    JSONArray jsonArray;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        baseSpinner = findViewById(R.id.baseSpinner);
        targetSpinner = findViewById(R.id.targetSpinner);
        etBase = findViewById(R.id.etBase);
        btnConvert = findViewById(R.id.btnConvert);
        tvTarget = findViewById(R.id.tvTarget);

        etBase.setText("0");
        final String[] baseCurrencyList = new String[]{"EUR","BTC","GBP","USD","AUD","NZD","CHF","XAU","XAG"};
        String[] targetCurrencyList = new String[]{"USD","JPY","CHF","CAD","CNY","SGD","GBP","CNY","AUD","NZD"};

        ArrayAdapter<String> baseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, baseCurrencyList);
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, targetCurrencyList);

        baseSpinner.setAdapter(baseAdapter);
        targetSpinner.setAdapter(targetAdapter);

        baseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                baseCurrency = baseSpinner.getItemAtPosition(position).toString();
                Log.e("converter",baseCurrency);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.e("converter","Nothing selected at base spinner.");
            }
        });
        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                targetCurrency = targetSpinner.getItemAtPosition(position).toString();
                Log.e("converter",targetCurrency);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.e("converter","Nothing selected at target spinner.");
            }
        });
        btnConvert.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(baseCurrency.equals(targetCurrency)){
                    Toast.makeText(getBaseContext(),"Base and Target currency cannot be the same.",Toast.LENGTH_LONG).show();
                }else{
                    Double base = Double.parseDouble(etBase.getText().toString());
                    new httpClient().execute(base);
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
    public boolean onCreateOptionsMenu(Menu menu) {      // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.converter, menu);
        return true;
    }
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
        }else if (id == R.id.settings) {
            Intent intent = new Intent(this,settings.class);
            intent.putExtra("userid",userid);
            startActivity(intent);
            finish();
        }else if (id == R.id.about) {
            Intent intent = new Intent(this,about.class);
            startActivity(intent);
        }else if (id == R.id.privacy) {
            Intent intent = new Intent(this,privacy.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class httpClient extends AsyncTask<Double, Void, Double> {
        protected Double doInBackground(Double... params) {
            final Double baseValue = params[0];
            String urlString = "http://10.112.160.105:7777/getCurrencyRates?base="+baseCurrency+"&target="+targetCurrency;
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
                        Double bid=0.0;
                        jsonArray = new JSONArray(responseString);

                        for(int i=0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            bid = jsonobject.getDouble("bid");
                            //return ask/bid;
                            Log.e("Bid value",bid.toString());
                            Log.e("Params value",baseValue.toString());
                        }
                        return bid*baseValue;
                    }catch (Throwable t){
                        Log.e("myLog",t.toString());
                    }
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString());
                e.printStackTrace();
            }
            finally {
                if (connection != null) {     connection.disconnect();      }
            }
            return -1.0;
        }
        //return to UI thread
        protected void onPostExecute(Double result){
            super.onPostExecute(result);
            if(result==-1.0){
                Toast.makeText(getBaseContext(),"No such currency pairs rate records in this system.",Toast.LENGTH_LONG).show();
            }else if(result==0.0){
                Toast.makeText(getBaseContext(),"Enter the base currency amount.",Toast.LENGTH_LONG).show();
                tvTarget.setText(result.toString());
            }else{
                tvTarget.setText(result.toString());
            }

        }
    }
}
