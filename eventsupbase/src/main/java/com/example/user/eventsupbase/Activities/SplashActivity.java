package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class SplashActivity extends AppCompatActivity {

    final int millisecondsDelayed = 4000;
    DbToken dbToken;

    String TAG = "MY_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        Date dateOfLastToken = null;
        Date dateTimeNow = Calendar.getInstance().getTime();

        try{
            dbToken = new DbToken(this);
            SQLiteDatabase db = dbToken.getWritableDatabase();
            String[]last_token_info = dbToken.GetDateOfLastToken(db);
            if(last_token_info!=null) {
                String token = last_token_info[0];
                String stringDateOfLastToken = last_token_info[1];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    dateOfLastToken = dateFormat.parse(stringDateOfLastToken);
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
                    User.token = token;
                    User.login = last_token_info[2];
                    final Intent intent = new Intent(this, MainActivity.class);
                    String message = "Вход произведен успешно! Пользователь: " + User.login;
                    intent.putExtra("Status", message);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, millisecondsDelayed);
                }
            }
            //нет токена в бд
            else{
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, StartActivity.class));
                            finish();
                        }
                    }, millisecondsDelayed);
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }finally {
            dbToken.close();
        }
    }
}
