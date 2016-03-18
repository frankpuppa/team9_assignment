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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private ArrayList<TableRow> bodyRows;
    private TreeMap<String, Integer> sortReminder;
    private ArrayList<TextView> headers, rows;
    private ArrayList<String> defaultHeaders;
    List<List<String>> attendanceData;
    private String[] columnNames;

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
        bodyRows = new ArrayList<>();
        columnNames = new String[6];
        columnNames[0] = getString(R.string.date);
        columnNames[1] = getString(R.string.week);
        columnNames[2] = getString(R.string.weekday);
        columnNames[3] = getString(R.string.start_time);
        columnNames[4] = getString(R.string.class_type);
        columnNames[5] = getString(R.string.attended);

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

    private int compareInt(int a, int b) {
        int compared = 0;
        if(a > b)
            compared = 1;
        else if(a < b)
            compared = -1;

        return compared;
    }

    // refactored code into separate methods to make it more portable/accommodate sorting
    private void populateTable() {
        for (TableRow row : bodyRows) {
            row.removeAllViews();
        }
        //Determine which column should be sorted and in what order
        int column = 0, tempOrder = 0;
        for (Map.Entry<String, Integer> entry : sortReminder.entrySet()) {
            int value = entry.getValue();

            for (int i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(entry.getKey())) {
                    column = i;
                    break;
                }
            }
            if (value != 0) {
                tempOrder = value;
                break;
            }
        }
        if (tempOrder != 0) {
            //Order data appropriately
            if(column == 2) {
                column = 6;
            }
            final int orderBy = column;
            final int order = tempOrder;
            Collections.sort(attendanceData, new Comparator<List<String>>() {
                @Override
                public int compare(List<String> list1, List<String> list2) {
                    if(orderBy == 0) {
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date1, date2;
                        try {
                            date1 = dateFormat.parse(list1.get(orderBy));
                            date2 = dateFormat.parse(list2.get(orderBy));

                            if(order == 1) {
                                return date1.compareTo(date2);
                            }

                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if(orderBy == 1) {
                        int week1, week2;
                        week1 = Integer.parseInt(list1.get(orderBy));
                        week2 = Integer.parseInt(list2.get(orderBy));

                        int compared = compareInt(week1, week2);
                        if(order == 1) {
                            compared = -compared;
                        }

                        return compared;
                    }

                    if(orderBy == 3) {
                        int time1, time2;
                        time1 = Integer.parseInt(list1.get(orderBy).replace(":", ""));
                        time2 = Integer.parseInt(list2.get(orderBy).replace(":", ""));

                        int compared = compareInt(time1, time2);
                        if(order == 1) {
                            compared = -compared;
                        }

                        return compared;
                    }

                    if (order == 1) {
                        return list1.get(orderBy).compareTo(list2.get(orderBy));
                    }

                    return list2.get(orderBy).compareTo(list1.get(orderBy));
                }
            });
        }

        for (int i = 0; i < attendanceData.size(); i++) {
            TableRow tableRowInside = new TableRow(this);
            rows = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                rows.add(new TextView(this));
                rows.get(j).setTextColor(Color.WHITE);
                rows.get(j).setGravity(Gravity.CENTER);
                rows.get(j).setPadding(25, 0, 25, 0);
                rows.get(j).setText(attendanceData.get(i).get(j));
                tableRowInside.addView(rows.get(j));
            }
            tableLayout.addView(tableRowInside);
            bodyRows.add(tableRowInside);
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
        populateTable();
        return column;
    }
}
