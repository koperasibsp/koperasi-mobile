package id.co.bspguard.android.bravo.functions;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by kangyasin on 11/1/17.
 */

public interface VolleyObjectResult {

    public void resSuccess(String requestType, JSONObject response);
    public void resError(String requestType, VolleyError error);

}
