package com.example.user.eventsupbase.Fragments;

import android.content.Intent;
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
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.EncryptionClient;
import com.example.user.eventsupbase.R;

/**
 * Created by User on 13.05.2016.
 */
public class FragRegister extends Fragment {

    EditText etPwd, etLogin;
    Intent intent;
    String login, pwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_register, container, false);

        etLogin = (EditText)v.findViewById(R.id.etReg_Login);
        etPwd = (EditText)v.findViewById(R.id.etReg_Pwd);
        CheckBox showPwd = (CheckBox)v.findViewById(R.id.cbShowPwd);

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

        Button btnRegister = (Button)v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener(){
            public void onClick (View view){
                String url_register = "";
                login = etLogin.getText().toString();
                pwd = etPwd.getText().toString();
                if((!login.isEmpty())&&(!pwd.isEmpty())) {
                    EncryptionClient ec = new EncryptionClient();
                    login = ec.md5(etLogin.getText().toString());
                    pwd = ec.md5(etPwd.getText().toString());
                    url_register = String.format("http://diploma.welcomeru.ru/reg/%s/%s", login, pwd);
                    new GetJsonInfo().execute(url_register);
                }
                else Toast.makeText(getActivity(), "Некоторые поля незаполнены!", Toast.LENGTH_SHORT).show();
            }
        });

        intent = new Intent(FragRegister.this.getActivity(), MainActivity.class);
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
                    Toast.makeText(getActivity(), "Логин и пароль не должны содержать в себе \"/\"", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":
                    Toast.makeText(getActivity(), "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
                    break;
                case "0":
                    Toast.makeText(getActivity(), "Ошибка или нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    User.login = etLogin.getText().toString();
                    User.md5_login = login;
                    intent.putExtra("Status", "Регистрация произведена успешно!");
                    startActivity(intent);
                default:
                    break;
            }
        }
    }
}
