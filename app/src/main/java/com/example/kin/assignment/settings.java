//An activity for user change some settings of the system

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
    String userid;  //store login user id
    public sqlData SD = null;

    //initial activity elements
    EditText etUserName;
    EditText etPassword;
    Button btnUpdateAcc;
    Button btnChi;
    Button btnEng;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        SD = new sqlData(this);

        //indentify the login user
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        //define activity elements
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnUpdateAcc = findViewById(R.id.btnUpdateAcc);
        btnChi = findViewById(R.id.btnChi);
        btnEng = findViewById(R.id.btnEng);
        btnLogout = findViewById(R.id.btnLogout);

        //click update button
        btnUpdateAcc.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                builder.setTitle("Ensure to edit the username and password?");

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //Click OK
                        Log.e("click ok in dialog",etPassword.getText().toString()+" , "+etUserName.getText().toString());
                        update(userid,etPassword.getText().toString(),etUserName.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {    //Click Cancel
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        btnChi.setOnClickListener(new Button.OnClickListener(){     //Click chi button
            public void onClick(View v){
                setLocale("zh-Hant-HK");
            }
        });
        //Click eng button
        btnEng.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                setLocale("en");
            }
        });
        //Click logout button
        btnLogout.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(),login.class);
                startActivity(intent);
                finish();
            }
        });
        getUser(userid);    //put the login user details to the activity elements
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
    public boolean onCreateOptionsMenu(Menu menu) {    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
    public void setLocale(String lang) {       //set up the language
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
    private void update(String userid,String password,String username){     //update user details
        SQLiteDatabase db = SD.getWritableDatabase();
        String sql = "update user set password='"+password+"', username='"+username+"' where userid='"+userid+"'";
        db.execSQL(sql);
        db.close();
    }
    private void getUser(String userid){        //get user name and password
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
