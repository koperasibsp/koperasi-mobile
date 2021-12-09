package id.co.bspguard.android.bravo.functions;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by kangyasin on 11/1/17.
 */

public class VolleyArrayService {

    VolleyArrayResult volleyArrayResult = null;
    Context context;

    public VolleyArrayService(VolleyArrayResult volleyArrayResult, Context context) {
        this.context = context;
        this.volleyArrayResult = volleyArrayResult;
    }

    public void postJsonArray(final String requestType, String url, JSONArray jsonArray){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(volleyArrayResult != null)
                        volleyArrayResult.resSuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(volleyArrayResult != null)
                        volleyArrayResult.resError(requestType, error);
                }
            });
            arrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(arrayRequest);
        } catch (Exception e){

        }
    }

    public void getArrayRequest(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(volleyArrayResult != null)
                        volleyArrayResult.resSuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(volleyArrayResult != null)
                        volleyArrayResult.resError(requestType, error);
                }
            });
            arrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(arrayRequest);
        } catch (Exception e) {

        }
    }

}
