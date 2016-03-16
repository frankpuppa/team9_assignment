package android.team9.com.timetabling;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomStudentList extends ArrayAdapter<String> {
    private static String[] matricNumber;
    private static String[] email;
    private static String[] fName;
    private static String[] lName;


    private Activity context;

    public CustomStudentList(Activity context, String[] matricNumber, String[] email, String[] fName, String[] lName) {

        super(context, R.layout.activity_enrolled_students, matricNumber);
        this.context = context;
        this.matricNumber = matricNumber;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.student_list_view, null, true);
        TextView textViewMatricNumber = (TextView) listViewItem.findViewById(R.id.textViewMatricNumber);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        String name = new StringBuilder(fName[position]).append(" ").append(lName[position]).toString();

        textViewMatricNumber.setText(matricNumber[position]);
        textViewEmail.setText(email[position]);
        textViewName.setText(name);

        return listViewItem;
    }

    public String getMatricNumber(int pos){return matricNumber[pos];}
    public String getEmail(int pos){
        return email[pos];
    }
    public String getFirstName(int pos){
        return fName[pos];
    }
    public String getLastName(int pos){return lName[pos];}
}