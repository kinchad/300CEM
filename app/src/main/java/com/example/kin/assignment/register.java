package com.example.kin.assignment;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {
    private sqlData SD = null;
    private Button btnRegister;
    private EditText userid;
    private EditText password;
    private EditText userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SD = new sqlData(this);
        btnRegister = findViewById(R.id.btnRegister);
        userid = findViewById(R.id.etUserID);
        password = findViewById(R.id.etPassword);
        userName = findViewById(R.id.etUserName);

        btnRegister.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                add(userid.getText().toString(),password.getText().toString(),userName.getText().toString());
                displayToLog();
                Intent intent = new Intent(v.getContext(),login.class);
                startActivity(intent);
            }
        });
    }
    private void add(String userid,String password,String username) {
        SQLiteDatabase db = SD.getWritableDatabase();
        try{
            db.execSQL("insert into user ('userid','password','username') values ('"+userid+"','"+password+"','"+username+"')");
            Toast.makeText(getBaseContext(),"Register sucessful",Toast.LENGTH_LONG).show();
        }catch(SQLException se){
            Log.e("SQL Error:",se.getMessage());
            Toast.makeText(getBaseContext(),"Register fail",Toast.LENGTH_LONG).show();
        }

    }
    private void displayToLog(){
        SQLiteDatabase db = SD.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"userid", "password","username"}, null, null, null, null, null);
        cursor.moveToFirst();
        String str="";

        for (int i = 0; i < cursor.getCount(); i++) {
            str = str +i+":"+ cursor.getString(0) +","+cursor.getString(1)+","+cursor.getString(2)+".";
            Log.e("DB log:",str);
            cursor.moveToNext();
        }
    }
}
