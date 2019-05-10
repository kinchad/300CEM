package com.example.kin.assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class settings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText etUserName;
    EditText etPassword;
    Button btnUpdateAcc;
    Button btnChi;
    Button btnEng;
    Button btnLogout;

    String userid;
    public sqlData SD = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        SD = new sqlData(this);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnUpdateAcc = findViewById(R.id.btnUpdateAcc);
        btnChi = findViewById(R.id.btnChi);
        btnEng = findViewById(R.id.btnEng);
        btnLogout = findViewById(R.id.btnLogout);

        //Log.e("Settings", "Locale = " + Locale.getDefault().toLanguageTag());
        btnUpdateAcc.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                builder.setTitle("Ensure to edit the username and password?");

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("click ok in dialog",etPassword.getText().toString()+" , "+etUserName.getText().toString());
                        update(userid,etPassword.getText().toString(),etUserName.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        btnChi.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //setLocale("zh-Hant-HK");
            }
        });
        btnEng.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //setLocale("en");
            }
        });
        btnLogout.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //finish();
                //System.exit(0);
            }
        });
        getUser(userid);
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
        getMenuInflater().inflate(R.menu.settings, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, settings.class);
        startActivity(refresh);
        finish();
    }
    private void add(String userid,String password,String username) {
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("insert into user ('userid','password','username') values ('"+userid+"','"+password+"','"+username+"')");
        db.close();
    }
    private void update(String userid,String password,String username){
        SQLiteDatabase db = SD.getWritableDatabase();
        String sql = "update user set password='"+password+"', username='"+username+"' where userid='"+userid+"'";
        db.execSQL(sql);
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
    private void deleteAll(){
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("delete from user");
        db.close();
    }
    private void getUser(String userid){
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.rawQuery("select password,username from user where userid='"+userid+"'",null);
        while(cursor.moveToNext()){
            String password = cursor.getString(0);
            String username = cursor.getString(1);

            etUserName.setText(username);
            etPassword.setText(password);
        }
        cursor.close();
        db.close();
    }
}
