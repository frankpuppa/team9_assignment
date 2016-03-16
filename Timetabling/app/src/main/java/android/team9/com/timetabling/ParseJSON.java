package android.team9.com.timetabling;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {
    public static String[] moduleCode;
    public static String[] moduleTitle;
    public static String[] moduleId;


    public static int user_id;
    public static String role;

    public static final String KEY_MODULE_CODE = "module_code";
    public static final String KEY_MODULE_TITLE = "module_title";
    public static final String KEY_MODULE_ID = "moduleid";


    public static final String KEY_HASH_CODE = "hash";
    public static final String KEY_STUDENT_ID = "matric_number";
    public static final String KEY_STAFF_ID = "staffid";

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
}