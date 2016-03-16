package android.team9.com.timetabling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;


public class StaffLandingActivity extends AppCompatActivity {

    public String JSON_URL = "http://api.ouanixi.com/staffModuleList/";
    private ListView listView;
    private int user_id;
    CustomModuleList cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_ui);
        listView = (ListView) findViewById(R.id.listView);
        Bundle extra = getIntent().getExtras();
        user_id = extra.getInt(LoginActivity.EXTRA_USERID);

        sendRequest();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                // changed how variables are accessed instead of taking them from a list that is already displayed
                // variables are taken directly from arrays in class, making it cleaner and modular
                String moduleTitle=cl.getModuleTitle(position);
                String moduleId=cl.getModuleId(position);
                String moduleCode=cl.getModuleCode(position);
                Bundle b = new Bundle();
                b.putString("moduleTitle", moduleTitle);
                b.putString("moduleId", moduleId);
                b.putString("moduleCode", moduleCode);
                Intent pass = new Intent(StaffLandingActivity.this, StaffFirstSelectionActivity.class);
                pass.putExtras(b);
                startActivity(pass);
            }
        });
    }

    public void sendRequest(){
        StringRequest stringRequest = new StringRequest(JSON_URL + user_id,
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
                        Toast.makeText(StaffLandingActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSONModuleList();
        // made class object global, to be accessible outside of the class for easier way to access module information
        cl = new CustomModuleList(this, ParseJSON.moduleCode, ParseJSON.moduleTitle, ParseJSON.moduleId);
        listView.setAdapter(cl);
    }
}