package id.co.bspguard.android.bravo.functions;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by kangyasin on 11/1/17.
 */

public interface VolleyArrayResult {

    public void resSuccess(String requestType, JSONArray response);
    public void resError(String requestType, VolleyError error);

}
