package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.eventsupbase.Adapters.EventAdapter;
import com.example.user.eventsupbase.DB.DbToken;
import com.example.user.eventsupbase.HttpClient;
import com.example.user.eventsupbase.Models.Report;
import com.example.user.eventsupbase.Models.User;
import com.example.user.eventsupbase.R;
import com.example.user.eventsupbase.Adapters.ReportAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    Intent intent, intent2;
    String event_address;
    List<Report> reports;
    CoordinatorLayout coordinatorLayout;
    ReportAdapter adapter;
    String TAG = "MY_TAG", url_add_visited_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent2 = new Intent(this, ConcreteReportActivity.class);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.report_coordLayout);

        intent = getIntent();
        event_address = intent.getStringExtra("EventAddress");
        reports = (List<Report>) intent.getSerializableExtra("Reports");

        adapter = new ReportAdapter(this, reports, event_address);
        ListView lvReport = (ListView) findViewById(R.id.lvReport);
        lvReport.setLongClickable(true);
        lvReport.setAdapter(adapter);
        registerForContextMenu(lvReport);
    }

    public void onGridClick(View v) {
        int id = v.getId();
        intent2.putExtra("Reports", (Serializable) reports);
        intent2.putExtra("ReportID", id);
        intent2.putExtra("EventAddress", event_address);
        startActivity(intent2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_visited:
                    Intent intent = new Intent(this, VisitedReportsActivity.class);
                    startActivity(intent);
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
                    db.execSQL("DELETE FROM "+DbToken.TABLE_NAME);
                }finally {
                    dbToken.close();
                }

                Intent intent3 = new Intent(this, StartActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.lvReport) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String menu_title = reports.get(info.position).report_name.substring(0, 18)+"...";
            menu.setHeaderTitle(menu_title);

            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd");
            try {
                Date date_finish = format.parse(reports.get(info.position).time);
                Date current_date = EventAdapter.trim(Calendar.getInstance().getTime());
                if (date_finish.before(current_date)) {
                    menu.add(0, v.getId(), 0, "Отметить посещенным");
                }
            }
            catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String report_id = reports.get(info.position).id_report;
        url_add_visited_report = String.format("http://diploma.welcomeru.ru/add/%s/%s", User.token, report_id);
        new AddingVisitedReport().execute(url_add_visited_report);
        return true;
    }

    class AddingVisitedReport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new HttpClient(params[0]);
            return httpClient.SendDataOrReturnVisitedReports();
        }

        protected void onPostExecute(String response) {
            switch (response) {
                case "-1":
                    Snackbar.make(coordinatorLayout, "Этот доклад уже отмечен вами как посещенный!", Snackbar.LENGTH_LONG).show();
                    break;
                case "":
                    Snackbar.make(coordinatorLayout, "Выйдите и войдите в систему снова", Snackbar.LENGTH_SHORT).show();
                    break;
                case "-2":
                case "0":
                    Snackbar.make(coordinatorLayout, "Ошибка или нет соединения с интернетом!", Snackbar.LENGTH_LONG).show();
                    break;
                case "1":
                    Snackbar.make(coordinatorLayout, "Доклад добавлен в список посещенных", Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }
}
