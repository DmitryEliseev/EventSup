package com.example.user.eventsupbase.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class FragRegister extends Fragment {

    EditText etPwd, etLogin;
    Intent intent;
    String login, pwd;
    DbToken dbToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_register, container, false);

        etLogin = (EditText) v.findViewById(R.id.etReg_Login);
        etPwd = (EditText) v.findViewById(R.id.etReg_Pwd);
        CheckBox showPwd = (CheckBox) v.findViewById(R.id.cbShowPwd);

        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etPwd.setInputType(129);
                }
            }
        });

        Button btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (HttpClient.hasConnection(getActivity())) {
                    String url_register = "";
                    login = etLogin.getText().toString();
                    pwd = etPwd.getText().toString();
                    if ((!login.isEmpty()) && (!pwd.isEmpty())) {
                        EncryptionClient ec = new EncryptionClient();
                        login = ec.md5(etLogin.getText().toString());
                        pwd = ec.md5(etPwd.getText().toString());
                        url_register = String.format("http://diploma.welcomeru.ru/reg/%s/%s", login, pwd);
                        new GetJsonInfo().execute(url_register);
                    } else
                        Toast.makeText(getActivity(), "Некоторые поля незаполнены!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Для регистрации необходимо соединение с интернетом!", Toast.LENGTH_SHORT).show();
            }
        });

        intent = new Intent(FragRegister.this.getActivity(), MainActivity.class);
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
                    Toast.makeText(getActivity(), "Логин и пароль не должны содержать в себе \"/\"", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":
                    Toast.makeText(getActivity(), "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
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

                        intent.putExtra(SplashActivity.AUTH_STATUS, "Регистрация произведена успешно!");
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
