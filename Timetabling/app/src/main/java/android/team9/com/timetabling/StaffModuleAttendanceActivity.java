package android.team9.com.timetabling;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StaffModuleAttendanceActivity extends AppCompatActivity {
    private String moduleId, moduleTitle;
    private String JSON_URL = "http://api.ouanixi.com/moduleAttendance/";
    private TableLayout tableLayout;
    private TableRow headingRow;
    private ArrayList<TableRow> bodyRows;
    private TreeMap<String, Integer> sortReminder;
    private ArrayList<TextView> headers, rows;
    private ArrayList<String> defaultHeaders;
    private List<List<String>> attendanceData;
    private String[] columnNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_module_attendance);
        moduleId = getIntent().getStringExtra("moduleId");

        moduleTitle = getIntent().getStringExtra("moduleTitle");
        // int 0 neutral, 2 ascending, 1 descending
        sortReminder = new TreeMap<>();
        sortReminder.put((getString(R.string.matricNumber)), 0);
        sortReminder.put((getString(R.string.forename)), 0);
        sortReminder.put((getString(R.string.surname)), 0);
        sortReminder.put((getString(R.string.attendance)), 0);
        sendRequest();
    }

    public void init() {
        tableLayout = (TableLayout) findViewById(R.id.tableMain);
        tableLayout.setPadding(0, 25, 0, 25);
        headingRow = new TableRow(this);
        headers = new ArrayList<>();
        defaultHeaders = new ArrayList<>();
        bodyRows = new ArrayList<>();
        columnNames = new String[4];
        columnNames[0] = getString(R.string.matricNumber);
        columnNames[1] = getString(R.string.forename);
        columnNames[2] = getString(R.string.surname);
        columnNames[3] = getString(R.string.attendance);

        for (int i = 0; i < 4; i++) {
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
        // refactored to use string resources
        headers.get(0).setText(R.string.matricNumber);
        defaultHeaders.add(getString(R.string.matricNumber));
        headers.get(1).setText(R.string.forename);
        defaultHeaders.add(getString(R.string.forename));
        headers.get(2).setText(R.string.surname);
        defaultHeaders.add(getString(R.string.surname));
        headers.get(3).setText(R.string.attendance);
        defaultHeaders.add(getString(R.string.attendance));
        tableLayout.addView(headingRow);
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
            final int orderBy = column;
            final int order = tempOrder;
            Collections.sort(attendanceData, new Comparator<List<String>>() {
                @Override
                public int compare(List<String> list1, List<String> list2) {
                    if (order == 1) {
                        return list1.get(orderBy).compareTo(list2.get(orderBy));
                    }

                    return list2.get(orderBy).compareTo(list1.get(orderBy));
                }
            });
        }

        for (int i = 0; i < attendanceData.size(); i++) {
            final TableRow tableRowInside = new TableRow(this);
            tableRowInside.setClickable(true);

            tableRowInside.setOnClickListener(new View.OnClickListener() {

                public void onClick(final View v) {
                    v.setBackgroundColor(Color.GRAY);
                    //get the data you need
                    TableRow tablerow = (TableRow) v;
                    TextView matric = (TextView) tablerow.getChildAt(0);
                    String matricNumber = matric.getText().toString();
                    TextView fname = (TextView) tablerow.getChildAt(1);
                    TextView lname = (TextView) tablerow.getChildAt(2);
                    String name = fname.getText().toString() + " " + lname.getText().toString();
                    // call single student (based on matric number) attendance activity here
                    final Bundle b = new Bundle();
                    b.putString("matricNumber", matricNumber);
                    b.putString("moduleId", moduleId);
                    b.putString("moduleTitle", moduleTitle);
                    b.putString("studentName", name);

                    PopupMenu popup = new PopupMenu(StaffModuleAttendanceActivity.this, tableRowInside);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.selection_popup, popup.getMenu());
                    final String[] selection = new String[1];
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            selection[0] = (item.getTitle().toString());
                            if (selection[0].contains("Table")) {
                                Intent pass = new Intent(StaffModuleAttendanceActivity.this, StaffModuleStudentAttendanceActivity.class);
                                pass.putExtras(b);
                                startActivity(pass);
                            } else if (selection[0].contains("Graph")) {
                                // Dominic call your activity from here
                                Intent pass = new Intent(StaffModuleAttendanceActivity.this, StaffModuleStudentAttendanceActivity.class);
                                pass.putExtras(b);
                                startActivity(pass);
                            }
                            v.setBackgroundColor(Color.rgb(61, 69, 91));
                            return true;
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popup.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    popup.show(); //showing popup menu
                    popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            v.setBackgroundColor(Color.rgb(61, 69, 91));
                        }
                    });
                }
            });
            rows = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                rows.add(new TextView(this));
                rows.get(j).setTextColor(Color.WHITE);
                rows.get(j).setGravity(Gravity.CENTER);
                rows.get(j).setPadding(25, 10, 25, 10);
                rows.get(j).setText(attendanceData.get(i).get(j));

                tableRowInside.addView(rows.get(j));
            }
            tableLayout.addView(tableRowInside);
            bodyRows.add(tableRowInside);
        }
    }


    private void sendRequest() {
        Log.i("string", JSON_URL + moduleId);
        StringRequest stringRequest = new StringRequest(JSON_URL + moduleId,
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
                        Toast.makeText(StaffModuleAttendanceActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        attendanceData = pj.parseJSONModuleAttendance();
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
        for (int i = 0; i < 4; i++) {
            if (header != i)
                headers.get(i).setText(defaultHeaders.get(i));
        }
        populateTable();
        return column;
    }
}
