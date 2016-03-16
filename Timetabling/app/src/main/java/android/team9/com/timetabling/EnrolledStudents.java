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
import android.widget.TextView;
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

public class EnrolledStudents extends AppCompatActivity {

    private String JSON_URL = "http://api.ouanixi.com/module_enrollments/";
    private ListView lv;
    private String moduleID,moduleCode;
    CustomStudentList sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_students);

        lv = (ListView) findViewById(R.id.listView);

        moduleID = getIntent().getStringExtra("module");
        moduleCode = getIntent().getStringExtra("moduleCode");
        Log.i("module ID ", moduleID);
        TextView nameText = (TextView) findViewById(R.id.enrolledTitle);
        nameText.setText("Students enrolled in "+moduleCode);
        sendRequest();
    }

    private void sendRequest(){
        StringRequest stringRequest = new StringRequest(JSON_URL + moduleID,
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
                        Toast.makeText(EnrolledStudents.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
