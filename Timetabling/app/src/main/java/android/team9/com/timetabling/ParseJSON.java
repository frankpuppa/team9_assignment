package android.team9.com.timetabling;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseJSON {
    public static String[] moduleCode;
    public static String[] moduleTitle;
    public static String[] moduleId;


    public static String[] matricNumber;
    public static String[] email;
    public static String[] fName;
    public static String[] lName;

    public static String[] classId;
    public static String[] qrCode;
    public static String[] startTime;
    public static String[] endTime;
    public static String[] room;
    public static String[] building;
    public static String[] module;

    public static String[] classType;
    public static int[] attendanceCount;

    public static String[] attendancePercentage;

    public static int user_id;
    public static String role;

    public static final String KEY_MODULE_CODE = "module_code";
    public static final String KEY_MODULE_TITLE = "module_title";
    public static final String KEY_MODULE_ID = "moduleid";

    public static final String KEY_MATRIC_NUMBER = "matric_number";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";

    public static final String KEY_HASH_CODE = "hash";
    public static final String KEY_STUDENT_ID = "matric_number";
    public static final String KEY_STAFF_ID = "staffid";

    public static final String KEY_CLASS_ID = "id";
    public static final String KEY_QR_CODE = "qrCode";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_ROOM = "room_id";
    public static final String KEY_BUILDING = "building";
    public static final String KEY_MODULE = "module";

    public static final String KEY_CLASS_TYPE = "class_type";
    public static final String KEY_CLASS_REGISTER = "class_register";

    public static final String KEY_PERCENTAGE = "percentage";

    private String json;

    public ParseJSON(String json) { this.json = json; }

    protected void parseJSONModuleList() throws JSONException{
        try {

            JSONArray jsonArray = new JSONArray(json);
            Log.v("JSON", jsonArray.toString());

            moduleCode = new String[jsonArray.length()];
            moduleTitle = new String[jsonArray.length()];
            moduleId = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                moduleCode[i] = jsonobject.getString(KEY_MODULE_CODE);
                moduleTitle[i] = jsonobject.getString(KEY_MODULE_TITLE);
                moduleId[i] = jsonobject.getString(KEY_MODULE_ID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void parseJSONEnrolledStudents() throws JSONException{
        try {

            JSONArray jsonArray = new JSONArray(json);
            Log.v("JSON", jsonArray.toString());

            matricNumber = new String[jsonArray.length()];
            email = new String[jsonArray.length()];
            fName = new String[jsonArray.length()];
            lName = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                matricNumber[i] = jsonobject.getString(KEY_MATRIC_NUMBER);
                email[i] = jsonobject.getString(KEY_EMAIL);
                fName[i] = jsonobject.getString(KEY_FIRST_NAME);
                lName[i] = jsonobject.getString(KEY_LAST_NAME);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void parseJSONClassList() throws JSONException{
        try {

            JSONArray jsonArray = new JSONArray(json);
            Log.v("JSON", jsonArray.toString());

            classId = new String[jsonArray.length()];
            qrCode = new String[jsonArray.length()];
            startTime = new String[jsonArray.length()];
            endTime = new String[jsonArray.length()];
            room = new String[jsonArray.length()];
            building = new String[jsonArray.length()];
            module = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                classId[i] = jsonobject.getString(KEY_CLASS_ID);
                Log.i("Class ID ", classId[i]);
                qrCode[i] = jsonobject.getString(KEY_QR_CODE);
                startTime[i] = jsonobject.getString(KEY_START_TIME);
                endTime[i] = jsonobject.getString(KEY_END_TIME);
                room[i] = jsonobject.getString(KEY_ROOM);
                building[i] = jsonobject.getString(KEY_BUILDING);
                module[i] = jsonobject.getString(KEY_MODULE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void parseJSONLogin() throws JSONException {
        try {
            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.has(KEY_STUDENT_ID) && !jsonObj.isNull(KEY_STUDENT_ID))
                user_id = jsonObj.getInt(KEY_STUDENT_ID);
            else {
                user_id = jsonObj.getInt(KEY_STAFF_ID);
            }
            role = jsonObj.getString(KEY_HASH_CODE);

        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected int parseNoOfEnrolledStudents() throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray(json);
            int noOfStudents = jsonArray.length();
            return noOfStudents;
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void parseModuleAttendanceByWeek() throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray(json);
            classType = new String[jsonArray.length()];
            attendanceCount = new int[jsonArray.length()];
            int count;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                classType[i] = jsonobject.getString(KEY_CLASS_TYPE);
                attendanceCount[i] = jsonobject.getJSONArray(KEY_CLASS_REGISTER).length();
                Log.v("class: ", classType[i]);
                Log.v("count: ", String.valueOf(attendanceCount[i]));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected List<List<String>> parseJSONModuleAttendance() throws JSONException {
        try {
            List<List<String>> attendanceData = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            Log.v("JSON", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<String> temp = new ArrayList<>();
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                temp.add(jsonobject.getString(KEY_MATRIC_NUMBER));
                temp.add(jsonobject.getString(KEY_FIRST_NAME));
                temp.add(jsonobject.getString(KEY_LAST_NAME));
                temp.add(jsonobject.getString(KEY_PERCENTAGE));

                attendanceData.add(temp);
            }

            Log.i("A size", Integer.toString(attendanceData.size()));
            Log.i("A size", Integer.toString(attendanceData.get(0).size()));
            return attendanceData;
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }
}