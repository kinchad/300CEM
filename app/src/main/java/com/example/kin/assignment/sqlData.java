package com.example.kin.assignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class sqlData extends SQLiteOpenHelper {
    private final static String DB="forex.db";  //database name
    private final static int VS=2;  //version

    public sqlData(Context context) {
        super(context,DB, null, VS);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("sqlData: ","creating table...  A");

        String SQL="create table if not exists user(userid varchar(50) PRIMARY KEY,password varchar(50), username varchar(50))";
        db.execSQL(SQL);
        String SQL2="create table if not exists favour(userid varchar(50),currencyName varchar(50),remarks varchar(255))";
        db.execSQL(SQL2);

        Log.e("sqlData: ","creating table...  B");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //String SQL="DROP TABLE"+TB;
        //sqLiteDatabase.execSQL(SQL);
    }
}
