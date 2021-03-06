/*
This is the first page after user login the application.
This activity show all currency get from API and allow user to search by name.
 */

package com.example.kin.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListView;
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

public class homePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userid;      //store login user id

    //initial activity elements
    private EditText etSearch;
    private ListView lvCurrency;

    //parameters used in http request client
    private String responseString;
    private JSONArray jsonArray;
    private ArrayList<String> stringArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

        //define activity elements
        lvCurrency = findViewById(R.id.lvCurrency);
        etSearch = findViewById(R.id.etSearch);

        //make http request to currency API
        new httpClient().execute();

        lvCurrency.setOnItemClickListener(      //when user click on a list item
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
        etSearch.addTextChangedListener(new TextWatcher() {     //when user input something in the textbox
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {     }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("") ) {    //searching currency name from the list
                    searching(s.toString());
                }
            }
            public void afterTextChanged(Editable s) {       }
        });
    }
    @Override
    public void onBackPressed() {       //press back button
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {       // Handle navigation view item clicks here.
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
            String urlString = "http://10.112.160.105:7777/getLatestCurrency/";
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
                    InputStream inputStream = connection.getInputStream();      // read the website
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
                        Log.e("Error Log","catch error");   //display error log to logcat
                    }
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString());     //display exception in logcat
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
    public void searching(String key){      //searching through the key words
        ArrayList<String> searchArray = new ArrayList<String>();
        for(int i=0;i<stringArray.size();i++){
            if(stringArray.get(i).toLowerCase().contains(key.toLowerCase())){
                searchArray.add(stringArray.get(i));    //add the element to array that contains key word
            }
        }
        //set the array to be new listView
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,searchArray);
        lvCurrency.setAdapter(adapter);
    }
}
