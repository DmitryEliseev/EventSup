package com.example.user.eventsupbase.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.eventsupbase.Activities.MainActivity;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

/**
 * Created by User on 13.05.2016.
 */
public class FragLogin extends Fragment {

    EditText etPwd;
    EditText etLogin;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_login, container, false);

        etLogin = (EditText)v.findViewById(R.id.etLog_Login);
        etPwd = (EditText)v.findViewById(R.id.etLog_Pwd);

        //тестовые данные
        etLogin.setText("admin");
        etPwd.setText("admin");

        Button btnLogin = (Button)v.findViewById(R.id.btnLogIn);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                String url_register = "";
                String login = etLogin.getText().toString();
                String pwd = etPwd.getText().toString();
                if((!login.isEmpty())&&(!pwd.isEmpty())) {
                    url_register = String.format("http://diploma.welcomeru.ru/log/%s/%s", login, pwd);
                    new GetJsonInfo().execute(url_register);
                }
                else Toast.makeText(getActivity(), "Некоторые поля незаполнены!", Toast.LENGTH_SHORT).show();
            }
        });

        intent = new Intent(FragLogin.this.getActivity(), MainActivity.class);
        return  v;
    }

    class GetJsonInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.SendData();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "-3":
                    Toast.makeText(getActivity(), "Логин и пароль не должны содержать в себе \"/\"", Toast.LENGTH_SHORT).show();
                    break;
                case "-2":
                    Toast.makeText(getActivity(), "Нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":
                    Toast.makeText(getActivity(), "Неверный пароль или логин!", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    Toast.makeText(getActivity(), "Ошибка:( Попробуйте еще раз!", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    User.login = etLogin.getText().toString();
                    intent.putExtra("Status", "Вход произведен успешно!");
                    startActivity(intent);
                default:
                    break;
            }
        }
    }
}
