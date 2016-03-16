package android.team9.com.timetabling;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceActivity extends AppCompatActivity {

    private String JSON_URL = "http://api.ouanixi.com/class_register/";
    private ListView lv;
    private String classID;
    CustomStudentList sl;

    private String startTime;
    private String endTime;
    private String room;
    private String building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        lv = (ListView) findViewById(R.id.listView);

        startTime = getIntent().getStringExtra("start");
        Log.i("Start Time ", startTime);
        endTime = getIntent().getStringExtra("end");
        room = getIntent().getStringExtra("room");
        building = getIntent().getStringExtra("building");
        classID = getIntent().getStringExtra("classID");

        EditText startText = (EditText) findViewById(R.id.startTime);
        EditText endText = (EditText) findViewById(R.id.endTime);
        EditText roomText = (EditText) findViewById(R.id.room);
        EditText buildingText = (EditText) findViewById(R.id.building);

        startText.setText(startTime);
        endText.setText(endTime);
        roomText.setText(room);
        buildingText.setText(building);

        sendRequest();

    }


    private void sendRequest(){
        StringRequest stringRequest = new StringRequest(JSON_URL + classID,
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
                        Toast.makeText(AttendanceActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSONEnrolledStudents();
        sl = new CustomStudentList(this, ParseJSON.matricNumber, ParseJSON.email, ParseJSON.fName, ParseJSON.lName);
        lv.setAdapter(sl);

    }
}
