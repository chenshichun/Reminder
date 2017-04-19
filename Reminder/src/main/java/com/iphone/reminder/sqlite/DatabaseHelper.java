package com.iphone.reminder.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION =  1;
 
    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
    }
 
    public DatabaseHelper(Context context, String name)
    {
        this(context, name, VERSION);
    }
 
    public DatabaseHelper(Context context, String name, int version)
    {
        this(context, name, null, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        System.out.println("create a database!");
//        db.execSQL("create table info(number int,note varchar(2000))");
//        db.execSQL("create table info(title varchar(2000))");
        db.execSQL("CREATE TABLE table_one(_id INTEGER PRIMARY KEY AUTOINCREMENT, title text, time text , location text , note text ,repeat int ,is_check int)");
        db.execSQL("CREATE TABLE table_two(_id INTEGER PRIMARY KEY AUTOINCREMENT, title text, time text , location text , note text ,repeat int,is_check int)");
        db.execSQL("CREATE TABLE table_three(_id INTEGER PRIMARY KEY AUTOINCREMENT, title text, time text , location text , note text ,repeat int,is_check int)");
        db.execSQL("CREATE TABLE table_four(_id INTEGER PRIMARY KEY AUTOINCREMENT, title text, time text , location text , note text ,repeat int,is_check int)");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
        System.out.println("upgrade a database!");
    }
}