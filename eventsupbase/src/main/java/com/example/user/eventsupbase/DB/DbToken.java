package com.example.user.eventsupbase.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.eventsupbase.Models.Token;

/**
 * Created by User on 24.05.2016.
 */
public class DbToken extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "tokens";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_TIME = "time_added";
    public static final String COLUMN_USER_LOGIN = "login";

    private static final String DATABASE_NAME = "tokens.db";
    private static final int DATABASE_VERSION = 4;

    //Строка для создания таблицы
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TOKEN + " TEXT not null, "
            + COLUMN_TIME + " TEXT not null, "
            + COLUMN_USER_LOGIN + " TEXT not null);";

    public DbToken(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 3 && newVersion == 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void GetDateOfLastToken(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);
        if (c.moveToFirst()) {
            do {
                Token.token = c.getString(c.getColumnIndex(COLUMN_TOKEN));
                Token.timeAdded = c.getString(c.getColumnIndex(COLUMN_TIME));
                Token.login = c.getString(c.getColumnIndex(COLUMN_USER_LOGIN));
            } while (c.moveToNext());
        } else {
            c.close();
        }
        c.close();
    }
}
