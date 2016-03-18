package android.team9.com.timetabling;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

    public static int[] attendanceCount;

    public static String[] class_type;
    public static String[] date;
    public static String[] week;
    public static String[] weekday;
    public static String[] weekdays;
    public static String[] attended;

    public static String[] classType;
    public static Integer[] attendanceCountInt;
    public static Integer[] classCount;

    public static List<List<String>> attendanceData;

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

    public static final String KEY_DATE = "date";
    public static final String KEY_WEEK = "week";
    public static final String KEY_WEEKDAY = "weekday";
    public static final String KEY_ATTENDED = "attended";

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
            attendanceData = new ArrayList<>();
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

            return attendanceData;
        } catch (JSONException e) {
            e.printStackTrace();

            throw e;
        }
    }


    protected void parseSemesterModuleAttendance() throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray(json);

            Set<String> classTypeList = new HashSet<>();
            ArrayList<Integer> classCountList = new ArrayList<>();
            ArrayList<Integer> attendanceCountList = new ArrayList<>();

            int count = 0;
            int attendance = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);

                classTypeList.add(jsonobject.getString(KEY_CLASS_TYPE));
                attendance += jsonobject.getJSONArray(KEY_CLASS_REGISTER).length();

                if (i < jsonArray.length() - 1 &&
                        jsonobject.getString(KEY_CLASS_TYPE).equals(jsonArray.getJSONObject(i + 1).getString(KEY_CLASS_TYPE))) {
                    count++;
                } else {
                    count++;
                    classCountList.add(0, count);
                    attendanceCountList.add(0, attendance);
                    count = 0;
                    attendance = 0;
                }
            }
            Log.v("CLASS TYPE", classTypeList.toString());
            Log.v("CLASS COUNT", classCountList.toString());
            Log.v("ATTENDANCE COUNT", attendanceCountList.toString());
            classCount = classCountList.toArray(new Integer[classCountList.size()]);
            classType = classTypeList.toArray(new String[classTypeList.size()]);
            attendanceCountInt = attendanceCountList.toArray(new Integer[attendanceCountList.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    protected List<List<String>> parseJSONStudentAttendance() throws JSONException {
        try {
            attendanceData = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            Log.v("JSON", jsonArray.toString());

            class_type = new String[jsonArray.length()];
            date = new String[jsonArray.length()];
            week = new String[jsonArray.length()];
            startTime = new String[jsonArray.length()];
            weekday = new String[jsonArray.length()];
            weekdays = new String[jsonArray.length()];
            attended = new String[jsonArray.length()];

            Map<String, Integer> weekdaysMap = new TreeMap<String, Integer>() {
            };
            weekdaysMap.put("Monday", 0);
            weekdaysMap.put("Tuesday", 1);
            weekdaysMap.put("Wednesday", 2);
            weekdaysMap.put("Thursday", 3);
            weekdaysMap.put("Friday", 4);
            weekdaysMap.put("Saturday", 5);
            weekdaysMap.put("Sunday", 6);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                class_type[i] = jsonobject.getString(KEY_CLASS_TYPE);
                date[i] = jsonobject.getString(KEY_DATE);
                week[i] = jsonobject.getString(KEY_WEEK);
                startTime[i] = jsonobject.getString(KEY_START_TIME);
                weekday[i] = jsonobject.getString(KEY_WEEKDAY);
                for (Map.Entry<String, Integer> entry : weekdaysMap.entrySet()) {
                    if (entry.getValue() == Integer.parseInt(weekday[i]))
                        weekdays[i] = entry.getKey();
                }
                attended[i] = jsonobject.getString(KEY_ATTENDED);
            }

            attendanceData.add(Arrays.asList(date));
            attendanceData.add(Arrays.asList(week));
            attendanceData.add(Arrays.asList(weekdays));
            attendanceData.add(Arrays.asList(startTime));
            attendanceData.add(Arrays.asList(class_type));
            attendanceData.add(Arrays.asList(attended));
            attendanceData.add(Arrays.asList(weekday));

            return attendanceData;
        } catch (JSONException e) {
            e.printStackTrace();

            throw e;
        }
    }
}