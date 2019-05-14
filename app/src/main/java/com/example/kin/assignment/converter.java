//An activity to calculate the result for exchanging different currency pairs

package com.example.kin.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import javax.net.ssl.HttpsURLConnection;

public class converter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userid;      //store login user id
    private Spinner baseSpinner;    //drop down list of base currency name
    private Spinner targetSpinner;  //drop down list of target currency name
    private EditText etBase;        //input box for base currency amount
    private TextView tvTarget;      //disply result
    private Button btnConvert;     //convert base to target currency amount

    //parameters  used in http request client
    private String baseCurrency;
    private String targetCurrency;
    private String responseString;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create the navigation drawer interface
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //indentify the login user
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        //define activity elements
        baseSpinner = findViewById(R.id.baseSpinner);
        targetSpinner = findViewById(R.id.targetSpinner);
        etBase = findViewById(R.id.etBase);
        tvTarget = findViewById(R.id.tvTarget);
        btnConvert = findViewById(R.id.btnConvert);

        etBase.setText("0");    //default base currency amount set to 0

        //define both list content , and set up adapter
        final String[] baseCurrencyList = new String[]{"EUR","BTC","GBP","USD","AUD","NZD","CHF","XAU","XAG"};
        final String[] targetCurrencyList = new String[]{"USD","JPY","CHF","CAD","CNY","SGD","GBP","CNY","AUD","NZD"};
        ArrayAdapter<String> baseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, baseCurrencyList);
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, targetCurrencyList);
        baseSpinner.setAdapter(baseAdapter);
        targetSpinner.setAdapter(targetAdapter);

        baseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //store selected base currency name
                baseCurrency = baseSpinner.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //store selected target currency name
                targetCurrency = targetSpinner.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        btnConvert.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(baseCurrency.equals(targetCurrency)){    //check if the selected currencies are invalid
                    Toast.makeText(getBaseContext(),"Base and Target currency cannot be the same.",Toast.LENGTH_LONG).show();
                }else{      //valid selection, make http request to currency API with parameter base currency amount
                    Double base = Double.parseDouble(etBase.getText().toString());
                    new httpClient().execute(base);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {   //press back button
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {    // Handle navigation view item clicks here.
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
    class httpClient extends AsyncTask<Double, Void, Double> {      //make request to currency API server
        protected Double doInBackground(Double... params) {
            final Double baseValue = params[0];     //for counting the target currency result
            String urlString = "http://10.112.160.105:7777/getCurrencyRates?base="+baseCurrency+"&target="+targetCurrency;      //request URL
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();       // create URL & connection
                connection.setReadTimeout(1500);
                connection.setConnectTimeout(1500);     // set timeout
                // simulate Chrome's user agent, mobile browser may have compatible problems
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
                connection.setInstanceFollowRedirects(true);    // set auto-redirect

                if( connection.getResponseCode() == HttpsURLConnection.HTTP_OK ){   //return 200 -> request successful
                    InputStream inputStream = connection.getInputStream();      //read the website
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
                        jsonArray = new JSONArray(responseString);      //convert string to JSON array

                        for(int i=0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);     //get JSON object from JSON array
                            bid = jsonobject.getDouble("bid");      //get the bid amount
                        }
                        return bid*baseValue;       //calculate and return the result
                    }catch (Throwable t){
                        Log.e("Error Log",t.toString());   //display the error in logcat
                    }
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString());     //display exception in logcat
            }
            finally {
                if (connection != null) {     connection.disconnect();      }
            }
            return -1.0;       //return -1.0 with no related bid found
        }
        protected void onPostExecute(Double result){    //return to UI thread
            super.onPostExecute(result);
            if(result==-1.0){       //No result
                Toast.makeText(getBaseContext(),"No such currency pairs rate records in this system.",Toast.LENGTH_LONG).show();
            }else if(result==0.0){  //base amount is 0
                Toast.makeText(getBaseContext(),"Enter the base currency amount.",Toast.LENGTH_LONG).show();
                tvTarget.setText(result.toString());
            }else{      //return the result to target textview
                tvTarget.setText(result.toString());
            }
        }
    }
}
