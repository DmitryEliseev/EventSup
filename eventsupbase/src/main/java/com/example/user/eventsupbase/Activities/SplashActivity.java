package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Token;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class SplashActivity extends AppCompatActivity {
    final int millisecondsDelayed = 4000;
    public static final String AUTH_STATUS = "Status";
    DbToken dbToken;
    Intent intent;

    final String TAG = "MY_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);


        Date dateOfLastToken = null;
        Date dateTimeNow = Calendar.getInstance().getTime();

        try {
            dbToken = new DbToken(this);
            SQLiteDatabase db = dbToken.getWritableDatabase();
            Token token = dbToken.GetDateOfLastToken(db);
            if (token != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    dateOfLastToken = dateFormat.parse(token.dateOfCreation);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                int difference = dateOfLastToken.getHours() - dateTimeNow.getHours();

                if (difference > 148) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, StartActivity.class));
                            finish();
                        }
                    }, millisecondsDelayed);
                } else {
                    User.token = token.token;
                    User.login = token.userLogin;
                    intent = new Intent(this, MainActivity.class);
                    String message = "Вход произведен успешно! Пользователь: " + User.login;
                    intent.putExtra(AUTH_STATUS, message);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, millisecondsDelayed);
                }
            }
            //нет токена в бд
            else {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, StartActivity.class));
                        finish();
                    }
                }, millisecondsDelayed);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            dbToken.close();
        }
    }
}
