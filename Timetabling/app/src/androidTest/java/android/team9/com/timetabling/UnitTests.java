package android.team9.com.timetabling;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by jamie on 22/02/16.
 */

public class UnitTests {

    private ParseJSON parseJSON;
    private String jsonArray;
    private String jsonStudents;
    private String jsonStudentObject;
    private String jsonStaffObject;
    private String jsonClassList;
    private String jsonAttendanceByWeek;
    private String jsonModuleAttendance;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        jsonArray = "[{\"moduleid\": 10012, \"module_code\": \"AC30001\", \"module_title\": \"Introduction to Java\"}, " +
                "{\"moduleid\": 10013, \"module_code\": \"AC30002\", \"module_title\": \"Data Visualisation\"}]";

        jsonStudents = "[{\"matric_number\": 101010, \"email\": \"bil@test.com\", \"first_name\": \"Bill\", \"last_name\": \"Kill\"}, " +
                "{\"matric_number\": 101011, \"email\": \"john@test\", \"first_name\": \"John\", \"last_name\": \"Wick\"}]";

        jsonStudentObject = "{\"matric_number\": 234242, \"hash\": \"AD3435SF\"}";
        jsonStaffObject = "{\"staffid\": 1000, \"hash\": \"AJ232ASFA\"}";

        jsonClassList = "[{\"id\": 1, \"qrCode\": 0, \"start_time\": \"2016-02-22T17:00:00\", \"end_time\": \"2016-02-22T18:00:00\", \"room_id\": \"5\", \"building\": \"QMB\", \"module\": \"10012\"}, " +
                "{\"id\": 2, \"qrCode\": 123, \"start_time\": \"2016-02-23T16:00:00\", \"end_time\": \"2016-02-23T17:00:00\", \"room_id\": \"2S14\", \"building\": \"Dalhousie\", \"module\": \"10012\"}]";

        jsonModuleAttendance = "[{\"matric_number\": 101010, \"first_name\": \"Bill\", \"last_name\": \"Kill\", \"percentage\": \"100%\"}, " +
                "{\"matric_number\": 101011,  \"first_name\": \"John\", \"last_name\": \"Wick\",\"percentage\": \"50%\"}]";

        jsonAttendanceByWeek = "[{\"week\": 1, \"class_type\": \"Seminar\", \"class_register\": [1, 2, 3]}," +
                                "{\"week\": 1, \"class_type\": \"Lab\", \"class_register\": [1, 2]}," +
                                "{\"week\": 1, \"class_type\": \"Lecture\", \"class_register\": []}]";
    }

    @Test
    public void testParseJSONModuleAttendanceByWeek() throws JSONException {

        parseJSON = new ParseJSON(jsonAttendanceByWeek);
        parseJSON.parseModuleAttendanceByWeek();

        assertEquals("Seminar", ParseJSON.classType[0]);
        assertEquals("Lab", ParseJSON.classType[1]);
        assertEquals("Lecture", ParseJSON.classType[2]);
        assertEquals(3, ParseJSON.attendanceCount[0]);
        assertEquals(2, ParseJSON.attendanceCount[1]);
        assertEquals(0, ParseJSON.attendanceCount[2]);

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
    public void testParseJSONEnrolledStudents() throws JSONException {

        parseJSON = new ParseJSON(jsonStudents);
        parseJSON.parseJSONEnrolledStudents();

        assertEquals("101010", ParseJSON.matricNumber[0]);
        assertEquals("101011", ParseJSON.matricNumber[1]);
        assertEquals("bil@test.com", ParseJSON.email[0]);
        assertEquals("john@test", ParseJSON.email[1]);
        assertEquals("Bill", ParseJSON.fName[0]);
        assertEquals("John", ParseJSON.fName[1]);
        assertEquals("Kill", ParseJSON.lName[0]);
        assertEquals("Wick", ParseJSON.lName[1]);
    }


    @Test
    public void testParseJSONClassList() throws JSONException {

        parseJSON = new ParseJSON(jsonClassList);
        parseJSON.parseJSONClassList();

        assertEquals("1", ParseJSON.classId[0]);
        assertEquals("2", ParseJSON.classId[1]);
        assertEquals("0", ParseJSON.qrCode[0]);
        assertEquals("123", ParseJSON.qrCode[1]);
        assertEquals("2016-02-22T17:00:00", ParseJSON.startTime[0]);
        assertEquals("2016-02-23T16:00:00", ParseJSON.startTime[1]);
        assertEquals("2016-02-22T18:00:00", ParseJSON.endTime[0]);
        assertEquals("2016-02-23T17:00:00", ParseJSON.endTime[1]);
        assertEquals("5", ParseJSON.room[0]);
        assertEquals("2S14", ParseJSON.room[1]);
        assertEquals("QMB", ParseJSON.building[0]);
        assertEquals("Dalhousie", ParseJSON.building[1]);
        assertEquals("10012", ParseJSON.module[0]);
        assertEquals("10012", ParseJSON.module[1]);
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

    @Test
    public void testParseJSONStudentListThrowsJSONException() throws JSONException {
        parseJSON = new ParseJSON("not valid json");
        thrown.expect(JSONException.class);
        parseJSON.parseJSONEnrolledStudents();
    }

    @Test
    public void testParseJSONClassListThrowsJSONException() throws JSONException {
        parseJSON = new ParseJSON("not valid json");
        thrown.expect(JSONException.class);
        parseJSON.parseJSONClassList();
    }

    @Test
    public void testParseJSONModuleAttendance() throws JSONException {

        parseJSON = new ParseJSON(jsonModuleAttendance);
        parseJSON.parseJSONModuleAttendance();

        assertEquals("101010", ParseJSON.matricNumber[0]);
        assertEquals("101011", ParseJSON.matricNumber[1]);
        assertEquals("Bill", ParseJSON.fName[0]);
        assertEquals("John", ParseJSON.fName[1]);
        assertEquals("Kill", ParseJSON.lName[0]);
        assertEquals("Wick", ParseJSON.lName[1]);
        assertEquals("100%", ParseJSON.attendancePercentage[0]);
        assertEquals("50%", ParseJSON.attendancePercentage[1]);
    }

    @After
    public void tearDown() {
        parseJSON = null;
        jsonStaffObject = null;
        jsonStudentObject = null;
        jsonArray = null;
        jsonClassList = null;
    }
}
