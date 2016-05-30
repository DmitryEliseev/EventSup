package com.example.user.eventsupbase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.eventsupbase.Adapters.VisitedReportsAdapter;
import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.JsonParsing;
import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class VisitedReportsActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String url_get_all_visited_reports;
    List<Report> reports;
    CoordinatorLayout coordinatorLayout;
    VisitedReportsAdapter adapter;
    ListView lvVisitedReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_reports);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.visited_coordLayout);

        url_get_all_visited_reports = String.format("http://diploma.welcomeru.ru/visited/%s", User.token);
        new GetJsonInfo().execute(url_get_all_visited_reports);
    }

    class GetJsonInfo extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VisitedReportsActivity.this);
            pDialog.setMessage("Загрузка данных");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.getData();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            switch (response) {
                case "[]":
                    Snackbar.make(coordinatorLayout, "Вы еще не посетили ни одного события", Snackbar.LENGTH_SHORT).show();
                    break;
                case "":
                    Snackbar.make(coordinatorLayout, "Такого юзера в системе нет", Snackbar.LENGTH_SHORT).show();
                    break;
                case "-2":
                case "0":
                    Snackbar.make(coordinatorLayout, "Подключитесь к интернету, чтобы работать с данной вкладкой!", Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    JsonParsing parsing = new JsonParsing();
                    reports = parsing.GetReportsFromJsonString(response);
                    adapter = new VisitedReportsAdapter(getApplicationContext(), reports);
                    lvVisitedReports = (ListView) findViewById(R.id.lvVisitedReports);
                    lvVisitedReports.setLongClickable(true);
                    lvVisitedReports.setAdapter(adapter);
                    registerForContextMenu(lvVisitedReports);
                    break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvVisitedReports) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String menu_title = reports.get(info.position).report_name.substring(0, 18) + "...";
            menu.setHeaderTitle(menu_title);
            menu.add(0, v.getId(), 0, "Отменить посещение");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String report_id = reports.get(info.position).id_report;
        String url_remove_visited_report = String.format("http://diploma.welcomeru.ru/remove/%s/%s", User.token, report_id);
        new RemovingVisitedReport().execute(url_remove_visited_report);
        return true;
    }

    class RemovingVisitedReport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.SendDataOrReturnVisitedReports();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "[]":
                    Snackbar.make(coordinatorLayout, "Вы еще не посетили ни одного события", Snackbar.LENGTH_SHORT).show();
                    break;
                case "":
                    Snackbar.make(coordinatorLayout, "Такого юзера в системе нет", Snackbar.LENGTH_SHORT).show();
                    break;
                case "-2":
                case "0":
                    Snackbar.make(coordinatorLayout, "Для этого действия необходимо соединение с интернетом!", Snackbar.LENGTH_SHORT).show();
                    break;
                case "1":
                    finish();
                    startActivity(getIntent());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_user:
                String message = String.format("Username: %s", User.login);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                DbToken dbToken = null;
                try {
                    dbToken = new DbToken(this);
                    SQLiteDatabase db = dbToken.getWritableDatabase();
                    db.execSQL("DELETE FROM " + DbToken.TABLE_NAME);
                } finally {
                    dbToken.close();
                }

                Intent intent3 = new Intent(this, StartActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_user_exit, menu);
        return true;
    }


}
