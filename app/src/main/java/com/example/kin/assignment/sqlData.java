package com.example.kin.assignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlData extends SQLiteOpenHelper {
    private final static String DB="forex.db";  //database name
    private final static int VS=2;  //version

    public sqlData(Context context) {
        super(context,DB, null, VS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //String SQL="CREATE TABLE IF NOT EXISTS "+TB+"(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_title VARCHAR(50))";
        String SQL="create table if not exists  user(userid varchar(50) PRIMARY KEY,password varchar(50), username varchar(50))";
        sqLiteDatabase.execSQL(SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //String SQL="DROP TABLE"+TB;
        //sqLiteDatabase.execSQL(SQL);
    }
}
