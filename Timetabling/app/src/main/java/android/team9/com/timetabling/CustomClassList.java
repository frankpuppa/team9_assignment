package android.team9.com.timetabling;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Dominic on 2/23/2016.
 */
public class CustomClassList extends ArrayAdapter<String> {
    private static String[] startTime;
    private static String[] endTime;
    private static String[] classId;
    private static String[] room;
    private static String[] building;

    private Activity context;

    public CustomClassList(Activity context, String[] startTime, String[] endTime, String[] classId, String[] room, String[] building) {

        super(context, R.layout.class_list_layout, startTime);
        this.context = context;
        this.startTime = startTime;
        //Log.i("start time ", startTime[]);
        this.endTime = endTime;
        this.classId = classId;
        this.room = room;
        this.building = building;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.class_list_layout, null, true);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.textViewStartTime);
        TextView textViewEndTime = (TextView) listViewItem.findViewById(R.id.textViewEndTime);
        TextView textViewRoom = (TextView) listViewItem.findViewById(R.id.textViewRoom);
        TextView textViewBuilding = (TextView) listViewItem.findViewById(R.id.textViewBuilding);

        textViewStartTime.setText(startTime[position]);
        textViewEndTime.setText(endTime[position]);
        textViewRoom.setText(room[position]);
        textViewBuilding.setText(building[position]);

        return listViewItem;
    }

    public String getStartTime(int pos){ return startTime[pos]; }
    public String getEndTime(int pos){ return endTime[pos]; }
    public String getRoom(int pos){ return room [pos]; }
    public String getBuilding(int pos){ return building [pos]; }
    public String getClassId(int pos){ return classId [pos]; }

}