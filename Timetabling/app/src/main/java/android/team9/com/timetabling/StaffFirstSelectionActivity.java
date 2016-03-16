package android.team9.com.timetabling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class StaffFirstSelectionActivity extends AppCompatActivity {

    private String moduleId;
    private String moduleTitle;
    private String moduleCode;
    private int noOfStudents;
    public final static String MODULEID = "android.team9.com.MODULEID";
    private static final String NO_OF_STUDENTS_URL = "http://10.0.2.2:8000/module_enrollments/";

    private Button buttonAttendance;
    private Button buttonGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_selection);
        moduleId = getIntent().getStringExtra("moduleId");
        moduleTitle = getIntent().getStringExtra("moduleTitle");
        moduleCode = getIntent().getStringExtra("moduleCode");

        EditText idText = (EditText) findViewById(R.id.moduleId);
        EditText nameText = (EditText) findViewById(R.id.moduleName);
        idText.setText(moduleCode);
        nameText.setText(moduleTitle);

        buttonAttendance = (Button) findViewById(R.id.attendanceButton);
        buttonGraph = (Button) findViewById(R.id.moduleGraphBtn);

        getStudentNumber();
        
        buttonAttendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v == buttonAttendance) {
                    viewAttendance();
                }
            }
        });

        buttonGraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v == buttonGraph) {
                    viewPopupMenu();
                }
            }
        });
    }

    private void getStudentNumber() {
        StringRequest stringRequest = new StringRequest(NO_OF_STUDENTS_URL + moduleId,
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
                        Toast.makeText(StaffFirstSelectionActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        noOfStudents = pj.parseNoOfEnrolledStudents();
    }

    private void viewPopupMenu() {

        PopupMenu popup = new PopupMenu(StaffFirstSelectionActivity.this, buttonGraph);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                attendanceGraphByWeek(item.getTitle().toString());
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private Bundle makeBundle() {
        Bundle b = new Bundle();
        b.putString("module", moduleId);
        b.putString("moduleCode", moduleCode);
        return b;
    }

    private void attendanceGraphByWeek(String week) {
        Bundle b = makeBundle();
        b.putString("moduleTitle", moduleTitle);
        b.putString("week", week);
        b.putString("noOfStudents", String.valueOf(noOfStudents));
        Intent intent = new Intent(StaffFirstSelectionActivity.this, ModuleAttendanceGraphActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    //click on button 'VIEW ENROLLED STUDENTS'
    public void view_enrolled_students(View view){
        Bundle b = makeBundle();
        Intent pass = new Intent(StaffFirstSelectionActivity.this, EnrolledStudents.class);
        pass.putExtras(b);
        startActivity(pass);
    }

    public void view_module_attendance(View view) {
        Bundle b = new Bundle();
        b.putString("moduleId", moduleId);
        Intent pass = new Intent(StaffFirstSelectionActivity.this, StaffModuleAttendanceActivity.class);
        pass.putExtras(b);
        startActivity(pass);
    }

    private void viewAttendance(){

        Intent intent = new Intent(this, ClassListActivity.class);
        intent.putExtra(MODULEID,moduleId);
        startActivity(intent);

    }
}
