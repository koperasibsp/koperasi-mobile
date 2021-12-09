package id.co.bspguard.android.bravo.functions;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.bspguard.android.bravo.accounts.Login;

/**
 * Created by kangyasin on 11/1/17.
 */

public class VolleyObjectService {

    VolleyObjectResult volleyObjectResult = null;
    VolleyArrayResult volleyArrayResult = null;
    Context context;
    Fungsi fn = new Fungsi();

    public VolleyObjectService(VolleyObjectResult volleyObjectResult, Context context) {
        this.volleyObjectResult = volleyObjectResult;
        this.context = context;
    }

    public void postJsonObject(final String requestType, String url, JSONObject params){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest objectRequest = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if(volleyObjectResult != null)
                        volleyObjectResult.resSuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(volleyObjectResult != null)
                        volleyObjectResult.resError(requestType, error);
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
              final String access_token = fn.getdatalogin(context);

              Map<String, String> headers = new HashMap<>();
              headers.put("Content-Type", "application/json; charset=UTF-8");
              headers.put("Authorization", "Bearer " + access_token);
              return headers;
            }
          };

            objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(objectRequest);
        } catch (Exception e){

        }
    }

    public void getJsonObject(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(volleyObjectResult != null)
                        volleyObjectResult.resSuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(volleyObjectResult != null)
                        volleyObjectResult.resError(requestType, error);
                }
            }) {
              @Override
              public Map<String, String> getHeaders() throws AuthFailureError {
                final String access_token = fn.getdatalogin(context);

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Bearer " + access_token);
                return headers;
              }
            };
            jsonObj.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObj);

        }catch(Exception e){

        }
    }
}
