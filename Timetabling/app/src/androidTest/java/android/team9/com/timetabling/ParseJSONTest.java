package android.team9.com.timetabling;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;

/**
 * Created by jamie on 22/02/16.
 */

public class ParseJSONTest{

    private ParseJSON parseJSON;
    private String jsonArray;
    private String jsonStudentObject;
    private String jsonStaffObject;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        jsonArray = "[{\"moduleid\": 10012, \"module_code\": \"AC30001\", \"module_title\": \"Introduction to Java\"}, " +
                "{\"moduleid\": 10013, \"module_code\": \"AC30002\", \"module_title\": \"Data Visualisation\"}]";

        jsonStudentObject = "{\"matric_number\": 234242, \"hash\": \"AD3435SF\"}";
        jsonStaffObject = "{\"staffid\": 1000, \"hash\": \"AJ232ASFA\"}";

    }

    @Test
    public void testParseJSONModuleList() throws JSONException {

        parseJSON = new ParseJSON(jsonArray);
        parseJSON.parseJSONModuleList();

        assertEquals("AC30001", ParseJSON.moduleCode[0]);
        assertEquals("AC30002", ParseJSON.moduleCode[1]);
        assertEquals("10012", ParseJSON.moduleId[0]);
        assertEquals("10013", ParseJSON.moduleId[1]);
        assertEquals("Introduction to Java", ParseJSON.moduleTitle[0]);
        assertEquals("Data Visualisation", ParseJSON.moduleTitle[1]);
    }

    @Test
    public void testParseJSONLoginStudent() throws JSONException {

        parseJSON = new ParseJSON(jsonStudentObject);
        parseJSON.parseJSONLogin();
        assertEquals(234242, ParseJSON.user_id);
        assertEquals("AD3435SF", ParseJSON.role);

    }

    @Test
    public void testParseJSONLoginStaff() throws JSONException {

        parseJSON = new ParseJSON(jsonStaffObject);
        parseJSON.parseJSONLogin();
        assertEquals(1000, ParseJSON.user_id);
        assertEquals("AJ232ASFA", ParseJSON.role);
    }

    @Test
    public void testParseJSONLoginThrowsJSONException() throws JSONException {
        parseJSON = new ParseJSON("not valid json");
        thrown.expect(JSONException.class);
        parseJSON.parseJSONLogin();
    }

    @Test
    public void testParseJSONModuleListThrowsJSONException() throws JSONException {
        parseJSON = new ParseJSON("not valid json");
        thrown.expect(JSONException.class);
        parseJSON.parseJSONModuleList();
    }

    @After
    public void tearDown() {
        parseJSON = null;
        jsonStaffObject = null;
        jsonStudentObject = null;
        jsonArray = null;
    }
}
