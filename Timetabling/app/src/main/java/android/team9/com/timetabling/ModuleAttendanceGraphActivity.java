package android.team9.com.timetabling;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleAttendanceGraphActivity extends AppCompatActivity {

    private static final String MODULE_ATTENDANCE_URL = "http://10.0.2.2:8000/moduleAttendanceList/";

    public static final String KEY_MODULE_ID = "module_id";
    public static final String KEY_WEEK = "week";
    private String week;
    private String moduleId;
    private String moduleTitle;
    private String noOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_attendance_graph);

        // extract week number from the string "week x"
        String inputline = getIntent().getStringExtra("week");
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(inputline);
        if(m.find())
            week = m.group();

        moduleId = getIntent().getStringExtra("module");
        moduleTitle = getIntent().getStringExtra("moduleTitle");
        noOfStudents = getIntent().getStringExtra("noOfStudents");

        getModuleAttendance();

    }

    private void createChart() {

        BarChart barChart = (BarChart) findViewById(R.id.chart);

        // populate attendance count
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<ParseJSON.attendanceCount.length; i++) {
            entries.add(new BarEntry(ParseJSON.attendanceCount[i], i));
        }

        BarDataSet dataset = new BarDataSet(entries, "# of students (" + noOfStudents + " enrolled)");

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i<ParseJSON.classType.length; i++) {
            labels.add(ParseJSON.classType[i]);
        }

        BarData data = new BarData(labels, dataset);
        barChart.setDrawBarShadow(true);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Week " + week + " - " + moduleTitle);  // set the description
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(2000);
    }

    private void getModuleAttendance() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MODULE_ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParseJSON prsJson = new ParseJSON(response);
                        Log.v("ATTENDANCE", response.toString());
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
                        NetworkResponse networkResponse = error.networkResponse;
                        Toast.makeText(ModuleAttendanceGraphActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_MODULE_ID, moduleId);
                params.put(KEY_WEEK, week);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseModuleAttendanceByWeek();
        createChart();
    }
}
