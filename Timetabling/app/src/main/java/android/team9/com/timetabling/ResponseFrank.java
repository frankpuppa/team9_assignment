package android.team9.com.timetabling;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
/**
 * Created by frank on 22/02/16.
 */
public class ResponseFrank implements Response.Listener {
    private Context context;

    public ResponseFrank(Context con){
        context=con;
    }

    @Override
    public void onResponse(Object response) {
        String result=(String) response;
        // Result handling
      System.out.println(result);
        Toast.makeText(context, "Successfully singed in", Toast.LENGTH_SHORT).show();
    }
}
