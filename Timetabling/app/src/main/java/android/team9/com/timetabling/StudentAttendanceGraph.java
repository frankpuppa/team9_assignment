package android.team9.com.timetabling;

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

public class StudentAttendanceGraph extends AppCompatActivity {

    private static final String STUDENT_ATTENDANCE_URL = "http://api.ouanixi.com/studentAttendance/";

    public static final String KEY_MODULE_ID = "module_id";
    public static final String KEY_STUDENT_ID = "student_id";

    private String studentId;
    private String studentName;
    private String moduleId;
    private String moduleTitle;
    private String[] labels = {"Seminars", "Tutorials", "Labs"};

    private String noOfStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_graph);

        Bundle extra = getIntent().getExtras();
        moduleId = getIntent().getStringExtra("moduleId");
        moduleTitle = getIntent().getStringExtra("moduleTitle");
        studentName = getIntent().getStringExtra("studentName");
        studentId = getIntent().getStringExtra("matricNumber");


        getModuleAttendance();
    }

    private void getModuleAttendance() {

        StringRequest stringRequest = new StringRequest(STUDENT_ATTENDANCE_URL + moduleId + "/" + studentId,
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
                        Toast.makeText(StudentAttendanceGraph.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createChart() {

        BarChart barChart = (BarChart) findViewById(R.id.chart);

        // populate entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(ParseJSON.seminarAttended, 0));
        entries.add(new BarEntry(ParseJSON.tutorialAttended, 1));
        entries.add(new BarEntry(ParseJSON.labAttended, 2));

        BarDataSet dataset = new BarDataSet(entries, "classes attended");

        BarData data = new BarData(labels, dataset);
        barChart.setDrawBarShadow(true);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription(studentName + " - " + moduleTitle);  // set the description
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(2000);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseStudentAttendance();
        createChart();
    }
}