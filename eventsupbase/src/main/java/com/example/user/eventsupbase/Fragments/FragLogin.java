package com.example.user.eventsupbase.Fragments;

import android.content.Intent;
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
                String url_login = "";
                String login = etLogin.getText().toString();
                String pwd = etPwd.getText().toString();
                if((!login.isEmpty())&&(!pwd.isEmpty())) {
                    url_login = String.format("http://diploma.welcomeru.ru/log/%s/%s", login, pwd);
                    new GetJsonInfo().execute(url_login);
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
            return httpClient.SendDataOrReturnVisitedReports();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "-2":
                case "-1":
                    Toast.makeText(getActivity(), "Неверный пароль или логин!", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    Toast.makeText(getActivity(), "Ошибка или нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
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
