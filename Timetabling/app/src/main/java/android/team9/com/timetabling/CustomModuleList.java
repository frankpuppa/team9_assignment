package android.team9.com.timetabling;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomModuleList extends ArrayAdapter<String> {
    private static String[] moduleCode;
    private static String[] moduleTitle;
    private static String[] moduleId;

    private Activity context;

    public CustomModuleList(Activity context, String[] moduleCode, String[] moduleTitle, String[] moduleId) {

        super(context, R.layout.module_list_view_layout, moduleCode);
        this.context = context;
        this.moduleCode = moduleCode;
        this.moduleTitle = moduleTitle;
        this.moduleId = moduleId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.module_list_view_layout, null, true);
        TextView textViewModuleCode = (TextView) listViewItem.findViewById(R.id.textViewModuleCode);
        TextView textViewModuleTitle = (TextView) listViewItem.findViewById(R.id.textViewModuleTitle);

        textViewModuleCode.setText(moduleCode[position]);
        textViewModuleTitle.setText(moduleTitle[position]);

        return listViewItem;
    }

    public String getModuleCode(int pos){
        return moduleCode[pos];
    }
    public String getModuleId(int pos){
        return moduleId[pos];
    }
    public String getModuleTitle(int pos){
        return moduleTitle[pos];
    }
}