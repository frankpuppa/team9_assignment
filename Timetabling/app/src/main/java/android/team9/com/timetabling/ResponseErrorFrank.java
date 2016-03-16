package android.team9.com.timetabling;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by frank on 22/02/16.
 */
public  class ResponseErrorFrank implements Response.ErrorListener{
    private Context context;

    public ResponseErrorFrank(Context con){
        context=con;
    }
    private boolean error_bool;
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this.context, "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
        System.out.println("Something went wrong!");
        error.printStackTrace();
    }
}
