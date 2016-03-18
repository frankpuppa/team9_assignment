package android.team9.com.timetabling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;

import java.util.ArrayList;

public class SemesterAttendanceGraphActivity extends AppCompatActivity {

    private static final String MODULE_ATTENDANCE_URL = "http://api.ouanixi.com/semesterModuleAttendance/";

    public static final String KEY_MODULE_ID = "module_id";
    public static final String KEY_WEEK = "week";
    private String week;
    private String moduleId;
    private String moduleTitle;
    private String noOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_attendance_graph);

        moduleId = getIntent().getStringExtra("module");
        moduleTitle = getIntent().getStringExtra("moduleTitle");
        noOfStudents = getIntent().getStringExtra("noOfStudents");

        getModuleAttendance();

    }

    // REFACTORED
    private void createChart() {

        BarChart barChart = (BarChart) findViewById(R.id.chart);
        float percentage;

        // populate attendance count
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < ParseJSON.attendanceCountInt.length; i++) {
            percentage = ((float) ParseJSON.attendanceCountInt[i] / (float) (31 * ParseJSON.classCount[i])) * 100;
            Log.v("%", String.valueOf(percentage));
            entries.add(new BarEntry(percentage, i));
        }

        BarDataSet dataset = new BarDataSet(entries, "# of students (" + noOfStudents + " enrolled)");

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < ParseJSON.classType.length; i++) {
            labels.add(ParseJSON.classType[i]);
        }

        BarData data = new BarData(labels, dataset);
        barChart.setDrawBarShadow(true);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Semester attendance % for " + moduleTitle);  // set the description
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(2000);
    }

    private void getModuleAttendance() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MODULE_ATTENDANCE_URL + moduleId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ATTENDANCE FOR SEMESTER", response.toString());
                        try {
                            parseJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Toast.makeText(SemesterAttendanceGraphActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseSemesterModuleAttendance();
        createChart();
    }
}
