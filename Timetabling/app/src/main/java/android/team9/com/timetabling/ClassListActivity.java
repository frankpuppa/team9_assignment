package android.team9.com.timetabling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {

    private ArrayAdapter<JSONObject> classAdapter;

    private String JSON_URL = "http://api.ouanixi.com/module_classes/";

    private ListView listView;

    private String moduleId;
    CustomClassList cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        listView = (ListView) findViewById(R.id.listView);

        Bundle extra = getIntent().getExtras();
        moduleId = extra.getString(StaffFirstSelectionActivity.MODULEID);

        sendRequest();

        //Click an item in the list and pass its value to 'Attendance' activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                String startTime = cl.getStartTime(position);
                String endTime = cl.getEndTime(position);
                String room = cl.getRoom(position);
                String building = cl.getBuilding(position);
                String classId = cl.getClassId(position);
                Log.i("start ", startTime);
                Log.i("end ", endTime);
                Log.i("room ", room);
                Log.i("building ", building);

                Bundle b = new Bundle();
                b.putString("start", startTime);
                b.putString("end", endTime);
                b.putString("classID", classId);
                b.putString("room", room);
                b.putString("building", building);
                Intent pass = new Intent(ClassListActivity.this, AttendanceActivity.class);
                pass.putExtras(b);
                startActivity(pass);

            }
        });

    }


    private void sendRequest(){

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
                        Toast.makeText(ClassListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSONClassList();
        cl = new CustomClassList(this, ParseJSON.startTime, ParseJSON.endTime, ParseJSON.classId, ParseJSON.room, ParseJSON.building);
        listView.setAdapter(cl);

    }
}