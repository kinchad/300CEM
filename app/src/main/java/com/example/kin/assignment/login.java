//Login activity

package com.example.kin.assignment;

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
    //initial activity elements
    private EditText etUserID;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    private sqlData SD = null;  //initial SQLite database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //define all elements
        SD = new sqlData(this);
        etUserID = findViewById(R.id.etUserID);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        //click login button
        btnLogin.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                String userid = etUserID.getText().toString();
                String password = etPassword.getText().toString();

                if(loginUser(userid,password)){
                    Intent intent = new Intent(v.getContext(),homePage.class);
                    intent.putExtra("userid",userid);
                    startActivity(intent);
                    finish();
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
    public boolean loginUser(String userid,String password){    //authentication function for login
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"userid", "password","username"}, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i=0;i<cursor.getCount();i++){  //check if userid and password exist in SQLite table "user"
            Log.e("DB user:",cursor.getString(0) +" / "+cursor.getString(1));
            if(userid.equals(cursor.getString(0)) && password.equals(cursor.getString(1))){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }
    private void deleteAll(){
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("delete from user");
        db.close();
    }
}