package android.team9.com.timetabling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class StaffFirstSelectionActivity extends AppCompatActivity {
        private String moduleId;
        private String moduleTitle;
        private String moduleCode;
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
    }
    // for button clicks
     private void viewStudents(){

     }
    private void viewAttendance(){

    }


}
