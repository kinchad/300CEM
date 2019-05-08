package com.example.kin.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    private EditText etUserID;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    public sqlData SD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SD = new sqlData(this);
        etUserID = (EditText)findViewById(R.id.etUserID);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        //deleteAll();
        //displayToLog();

        btnLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                boolean login = false;

                String userid = etUserID.getText().toString();
                String password = etPassword.getText().toString();

                if(loginUser(userid,password)){
                    Intent intent = new Intent(v.getContext(),homePage.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(),"Welcome...",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(),"Wrong login id or password",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRegister.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),register.class);
                startActivity(intent);
            }
        });
    }
    private void add(String userid,String password,String username) {
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("insert into user ('userid','password','username') values ('"+userid+"','"+password+"','"+username+"')");
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
    private boolean loginUser(String userid,String password){
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"userid", "password","username"}, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i=0;i<cursor.getCount();i++){
            Log.e("DB user:",cursor.getString(0) +" / "+cursor.getString(1));
            if(userid.equals(cursor.getString(0)) && password.equals(cursor.getString(1))){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
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
}