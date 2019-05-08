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
import android.widget.EditText;

public class favour extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public sqlData SD = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour);

        SD = new sqlData(this);

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

        getAll();
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
        getMenuInflater().inflate(R.menu.favour, menu);
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
        }else if(id==R.id.favour) {
            Intent intent = new Intent(this,favour.class);
            startActivity(intent);
        }else if (id == R.id.recommend) {
            Intent intent = new Intent(this,recommendCur.class);
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
    private void add(String userid,String password,String username) {
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("insert into favour ('userid','password','username') values ('"+userid+"','"+password+"','"+username+"')");
        db.close();
    }
    private void update(String userid,String password){
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("update user set password='"+password+"' where userid='"+userid+"'");
        db.close();
    }
    private void delete(String userid){
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("delete from user WHERE userid = '"+userid+"'");
        db.close();
    }
    private void displayToLog(){
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"userid", "password","username"}, null, null, null, null, null);
        cursor.moveToFirst();
        String str="";

        for (int i = 0; i < cursor.getCount(); i++) {
            str = str +i+":"+ cursor.getString(0) +","+cursor.getString(1)+","+cursor.getString(2)+";";
            cursor.moveToNext();
        }
        Log.e("DB data:",str);
        db.close();
    }
    private void getAll(){
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from favour",null);
        while(cursor.moveToNext()){
            String userid = cursor.getString(0);
            String currencyName  =cursor.getString(1);
            String remarks = cursor.getString(2);
            Log.e("UserID:",userid);
            Log.e("currencyName:",currencyName);
            Log.e("remarks:",remarks);
        }
        cursor.close();
        db.close();
    }
    private void deleteAll(){
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("delete from user");
        db.close();
    }
}
