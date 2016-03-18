package android.team9.com.timetabling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StaffModuleStudentAttendanceActivity extends AppCompatActivity {
    private String matric_number;
    private String moduleId;
    private String studentName;
    private String JSON_URL = "http://api.ouanixi.com/studentAttendance/";
    private TableLayout tableLayout;
    private TableRow headingRow;
    private TreeMap<String, Integer> sortReminder;
    private ArrayList<TextView> headers, rows;
    private ArrayList<String> defaultHeaders;
    List<List<String>> attendanceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_module_student_attendance);
        matric_number = getIntent().getStringExtra("matricNumber");
        moduleId = getIntent().getStringExtra("moduleId");
        studentName = getIntent().getStringExtra("studentName");

        EditText nameText = (EditText) findViewById(R.id.studentName);
        nameText.setText(studentName);

        // int 0 neutral, 2 ascending, 1 descending
        sortReminder = new TreeMap<>();
        sortReminder.put((getString(R.string.class_type)), 0);
        sortReminder.put((getString(R.string.date)), 0);
        sortReminder.put((getString(R.string.week)), 0);
        sortReminder.put((getString(R.string.start_time)), 0);
        sortReminder.put((getString(R.string.weekday)), 0);
        sortReminder.put((getString(R.string.attended)), 0);
        sendRequest();
    }

    public void init() {
        tableLayout = (TableLayout) findViewById(R.id.tableMain);
        tableLayout.setPadding(0, 25, 0, 25);
        headingRow = new TableRow(this);
        headers = new ArrayList<>();
        defaultHeaders = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            headers.add(new TextView(this));
            headers.get(i).setPadding(25, 0, 25, 25);
            headers.get(i).setTextColor(Color.WHITE);
            headers.get(i).setGravity(Gravity.CENTER);
            headers.get(i).setTextSize(15);
            final int finalI = i;
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    headers.get(finalI).setText(changeSortDirection(finalI, headers.get(finalI).getText().toString()));
                }
            });
            headingRow.addView(headers.get(i));
        }


        headers.get(0).setText(R.string.date);
        defaultHeaders.add(getString(R.string.date));
        headers.get(1).setText(R.string.week);
        defaultHeaders.add(getString(R.string.week));
        headers.get(2).setText(R.string.weekday);
        defaultHeaders.add(getString(R.string.weekday));
        headers.get(3).setText(R.string.start_time);
        defaultHeaders.add(getString(R.string.start_time));
        headers.get(4).setText(R.string.class_type);
        defaultHeaders.add(getString(R.string.class_type));
        headers.get(5).setText(R.string.attended);
        defaultHeaders.add(getString(R.string.attended));
        tableLayout.addView(headingRow);
    }

    // refactored code into separate methods to make it more portable/accommodate sorting
    private void populateTable() {
        for (int i = 0; i < attendanceData.get(0).size(); i++) {
            TableRow tableRowInside = new TableRow(this);
            rows = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                rows.add(new TextView(this));
                rows.get(j).setTextColor(Color.WHITE);
                rows.get(j).setGravity(Gravity.CENTER);
                rows.get(j).setPadding(25, 0, 25, 0);
                rows.get(j).setText(attendanceData.get(j).get(i));
                tableRowInside.addView(rows.get(j));
            }
            tableLayout.addView(tableRowInside);
        }
    }

    private void sendRequest() {
        Log.i("string", JSON_URL + moduleId + "/" + matric_number);
        StringRequest stringRequest = new StringRequest(JSON_URL + moduleId + "/" + matric_number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            showJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StaffModuleStudentAttendanceActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        Log.i("inside", "inside json");
        ParseJSON pj = new ParseJSON(json);
        attendanceData = pj.parseJSONStudentAttendance();
        init();
        populateTable();
    }

    // changing arrows/updating map values to track which way to sort
    private String changeSortDirection(int header, String column) {
        for (Map.Entry<String, Integer> entry : sortReminder.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (column.regionMatches(0, key, 0, column.length() - 2)) {
                if (value == 0) {
                    column = column.replace("✦", "▼");
                }
                if (value == 1) {
                    column = column.replace("▼", "▲");
                }
                if (value == 2) {
                    entry.setValue(-1);
                    column = column.replace("▲", "✦");
                }
                value = entry.getValue();
                value++;
                entry.setValue(value);
            } else entry.setValue(0);
        }
        for (int i = 0; i < 6; i++) {
            if (header != i)
                headers.get(i).setText(defaultHeaders.get(i));
        }
        return column;
    }
}
