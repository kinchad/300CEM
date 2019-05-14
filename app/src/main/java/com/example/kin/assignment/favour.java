package com.example.kin.assignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class favour extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userid;  //store login user id
    private sqlData SD = null;  //initial SQLite database

    //initial activity elements
    private ListView lvCurrency;
    private List<HashMap<String , String>> list;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create the navigation drawer interface
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //indentify the login user
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        //create database connection, define activity elements
        SD = new sqlData(this);
        list = new ArrayList<>();
        lvCurrency = findViewById(R.id.lvCurrency);

        //get the favour list from SQLite database
        getAll();
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
    public boolean onCreateOptionsMenu(Menu menu) {    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favour, menu);
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
    public void getAll(){   //get all favourite currency from table "favour"
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from favour",null);
        while(cursor.moveToNext()){
            String userid = cursor.getString(0);
            String currencyName  =cursor.getString(1);
            String remarks = cursor.getString(2);
            Log.e("UserID:",userid);
            Log.e("currencyName:",currencyName);
            Log.e("remarks:",remarks);

            HashMap<String , String> hashMap = new HashMap<>();
            hashMap.put("currencyName" , currencyName);
            hashMap.put("remarks" , remarks);
            list.add(hashMap);
        }
        cursor.close();
        db.close();

        //add the list to listView element
        adapter = new SimpleAdapter(this,list,android.R.layout.simple_list_item_2 ,new String[]{"currencyName" , "remarks"} ,new int[]{android.R.id.text1 , android.R.id.text2});
        lvCurrency.setAdapter(adapter);
        lvCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.remove(i);
                Log.e("favour.java",lvCurrency.getItemAtPosition(i).toString());
                ((BaseAdapter)adapter).notifyDataSetChanged();
            }
        });
    }
}
