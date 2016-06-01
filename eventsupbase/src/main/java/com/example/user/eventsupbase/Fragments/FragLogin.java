package com.example.user.eventsupbase.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.eventsupbase.Activities.MainActivity;
import com.example.user.eventsupbase.Activities.SplashActivity;
import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.EncryptionClient;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.Token;
import com.example.user.eventsupbase.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 13.05.2016.
 */
public class FragLogin extends Fragment {

    EditText etPwd, etLogin;
    Intent intent;
    String login, pwd;
    DbToken dbToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_login, container, false);

        etLogin = (EditText) v.findViewById(R.id.etLog_Login);
        etPwd = (EditText) v.findViewById(R.id.etLog_Pwd);

        Button btnLogin = (Button) v.findViewById(R.id.btnLogIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (HttpClient.hasConnection(getActivity())) {
                    String url_login = "";
                    login = etLogin.getText().toString();
                    pwd = etPwd.getText().toString();
                    if ((!login.isEmpty()) && (!pwd.isEmpty())) {
                        EncryptionClient ec = new EncryptionClient();
                        login = ec.md5(etLogin.getText().toString());
                        pwd = ec.md5(etPwd.getText().toString());
                        url_login = String.format("http://diploma.welcomeru.ru/log/%s/%s", login, pwd);
                        new GetJsonInfo().execute(url_login);
                    } else
                        Toast.makeText(getActivity(), "Некоторые поля незаполнены!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Для входа необходимо соединение с интернетом!", Toast.LENGTH_SHORT).show();
            }
        });

        intent = new Intent(FragLogin.this.getActivity(), MainActivity.class);
        return v;
    }

    class GetJsonInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.getOrSendData();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "-2":
                case "-1":
                    Toast.makeText(getActivity(), "Неверный пароль или логин!", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    Toast.makeText(getActivity(), "Ошибка или потеряно соединение с интернетом!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    try {
                        //Формирование токена
                        Token.token = response.split(";")[1];
                        Token.login = etLogin.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Token.dateOfCreation = dateFormat.format(new Date());

                        //Сохранение токена в SQLite
                        dbToken = new DbToken(getActivity());
                        SQLiteDatabase db = dbToken.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DbToken.COLUMN_TOKEN, Token.token);
                        values.put(DbToken.COLUMN_TIME, Token.dateOfCreation);
                        values.put(DbToken.COLUMN_USER_LOGIN, Token.login);
                        db.insert(DbToken.TABLE_NAME, null, values);

                        intent.putExtra(SplashActivity.AUTH_STATUS, "Вход произведен успешно!");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        dbToken.close();
                    }
            }
        }
    }
}
