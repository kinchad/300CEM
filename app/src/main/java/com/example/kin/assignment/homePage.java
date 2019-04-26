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
import android.widget.ArrayAdapter;
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
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class homePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvCurrency;
    private String[] currencyRates;

    String responseString;
    JSONArray jsonArray;
    ArrayList<String> stringArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

        new httpClient().execute();

        lvCurrency = (ListView)findViewById(R.id.lvCurrency);
        currencyRates = getResources().getStringArray(R.array.currencyRates);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stringArray);
        lvCurrency.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.home_page, menu);
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
            startActivity(intent);
        } else if (id == R.id.recommend) {
            Intent intent = new Intent(this,recommendCur.class);
            startActivity(intent);
        } else if (id == R.id.converter) {
            Intent intent = new Intent(this,converter.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class httpClient extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            String urlString = "http://10.112.160.67:7777/getLatestCurrency/";
            HttpURLConnection connection = null;
            try {
                // 初始化 URL
                URL url = new URL(urlString);
                // 取得連線物件
                connection = (HttpURLConnection) url.openConnection();
                // 設定 request timeout
                connection.setReadTimeout(1500);
                connection.setConnectTimeout(1500);
                // 模擬 Chrome 的 user agent, 因為手機的網頁內容較不完整
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
                // 設定開啟自動轉址
                connection.setInstanceFollowRedirects(true);

                // 若要求回傳 200 OK 表示成功取得網頁內容
                if( connection.getResponseCode() == HttpsURLConnection.HTTP_OK ){
                    // 讀取網頁內容
                    InputStream inputStream     = connection.getInputStream();
                    BufferedReader bufferedReader  = new BufferedReader( new InputStreamReader(inputStream) );

                    String tempStr;
                    StringBuffer stringBuffer = new StringBuffer();

                    while( ( tempStr = bufferedReader.readLine() ) != null ) {
                        stringBuffer.append( tempStr );
                    }

                    bufferedReader.close();
                    inputStream.close();

                    // 網頁內容字串
                    responseString = stringBuffer.toString();
                    //Log.e("myLog",responseString);

                    try{
                        jsonArray = new JSONArray(responseString);
                        //Log.e("myLog",jsonArray.toString());

                        for(int i=0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String name = jsonobject.getString("name");
                            String ask = jsonobject.getString("ask");
                            String bid = jsonobject.getString("bid");
                            String time = jsonobject.getString("time");

                            stringArray.add(name);
                            for(String str: stringArray){
                                Log.e("Currency Pairs:",str);
                            }
                        }
                        /*
                        for(int i = 0; i < jsonArray.length();  i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            stringArray.add(jsonObject.toString());
                        }*/
                    }catch (Throwable t){
                        Log.e("myLog","catch error");
                    }

/*                    runOnUiThread(new Runnable() {
                        public void run() {
                            //myText.setText(responseString);
                            //Toast.makeText(getBaseContext(), "run on ui thread", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            } catch (IOException e) {                e.printStackTrace();            }
            finally {
                if (connection != null) {                    connection.disconnect();                }
            }
            return null;
        }
    }
}
