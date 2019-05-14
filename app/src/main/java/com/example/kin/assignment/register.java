//register activity

package com.example.kin.assignment;

import android.content.Intent;
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
    //initial activity elements
    private sqlData SD = null;
    private Button btnRegister;
    private EditText userid;
    private EditText password;
    private EditText userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //define all elements
        SD = new sqlData(this);
        btnRegister = findViewById(R.id.btnRegister);
        userid = findViewById(R.id.etUserID);
        password = findViewById(R.id.etPassword);
        userName = findViewById(R.id.etUserName);

        btnRegister.setOnClickListener(new Button.OnClickListener(){    //user click register
            public void onClick(View v){    //add the user details to SQLite
                add(userid.getText().toString(),password.getText().toString(),userName.getText().toString());
                Intent intent = new Intent(v.getContext(),login.class);
                startActivity(intent);
            }
        });
    }
    private void add(String userid,String password,String username) {
        SQLiteDatabase db = SD.getWritableDatabase();
        try{    //add the user details to SQLite
            db.execSQL("insert into user ('userid','password','username') values ('"+userid+"','"+password+"','"+username+"')");
            Toast.makeText(getBaseContext(),"Register sucessful",Toast.LENGTH_LONG).show();
        }catch(SQLException se){
            Log.e("SQL Error:",se.getMessage());    //display error message
            Toast.makeText(getBaseContext(),"Register fail",Toast.LENGTH_LONG).show();
        }
    }
}
