package com.example.user.eventsupbase.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 24.05.2016.
 */
public class DbToken extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "tokens";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOKEN= "token";
    public static final String COLUMN_TIME= "timed_add";
    public static final String COLUMN_USER_LOGIN= "user_login";

    private static final String DATABASE_NAME = "tokens.db";
    private static final int DATABASE_VERSION = 2;

    //Строка для создания таблицы
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TOKEN + " varchar(36) not null, "
            + COLUMN_TIME + " varchar(19) not null, "
            + COLUMN_USER_LOGIN + " varchar(36) not null);";

    public DbToken(Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public String[] GetDateOfLastToken(SQLiteDatabase db){
        String[] results = new String [3];
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+COLUMN_ID+" DESC LIMIT 1", null);
        if(c.moveToFirst()){
            do{
                results[0] = c.getString(c.getColumnIndex(COLUMN_TOKEN));
                results[1] = c.getString(c.getColumnIndex(COLUMN_TIME));
                results[2] = c.getString(c.getColumnIndex(COLUMN_USER_LOGIN));
            }while (c.moveToNext());
        }else{
            c.close();
            return null;
        }
        c.close();
        return results;
    }
}
