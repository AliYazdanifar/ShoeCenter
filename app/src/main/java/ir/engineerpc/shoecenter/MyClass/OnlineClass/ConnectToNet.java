package ir.engineerpc.shoecenter.MyClass.OnlineClass;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.MyClass.G;


public class ConnectToNet {

    ResponseFromNet responseFromNet = null;

    private int method;

    Activity activity;

    private String url, opValue = "a", key1 = "a", value1 = "a", key2 = "a", value2 = "a",
            key3 = "a", value3 = "a", key4 = "a", value4 = "a", key5 = "a", value5 = "a";

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue) {
        this.responseFromNet = responseFromNet;
        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
    }

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue, String key1, String value1) {
        this.responseFromNet = responseFromNet;

        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
        this.key1 = key1;
        this.value1 = value1;
    }

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue, String key1, String value1, String key2, String value2) {
        this.responseFromNet = responseFromNet;
        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;

    }

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue, String key1, String value1, String key2, String value2, String key3, String value3) {
        this.responseFromNet = responseFromNet;
        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
        this.key3 = key3;
        this.value3 = value3;

    }

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4) {
        this.responseFromNet = responseFromNet;
        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
        this.key3 = key3;
        this.value3 = value3;
        this.key4 = key4;
        this.value4 = value4;

    }

    public ConnectToNet(Activity activity, ResponseFromNet responseFromNet, int method, String url, String opValue, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5) {
        this.responseFromNet = responseFromNet;
        this.method = method;
        this.activity = activity;
        this.url = url;
        this.opValue = opValue;
        this.key1 = key1;
        this.value1 = value1;
        this.key2 = key2;
        this.value2 = value2;
        this.key3 = key3;
        this.value3 = value3;
        this.key4 = key4;
        this.value4 = value4;
        this.key5 = key5;
        this.value5 = value5;

    }

    public void getFromNet() {
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseFromNet.onRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseFromNet.onRequestError(error.getMessage());
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            Map<String, String> map = new HashMap<>();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("op", opValue);
                map.put(key1, value1);
                map.put(key2, value2);
                map.put(key3, value3);
                map.put(key4, value4);
                map.put(key5, value5);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(G.context);
        queue.add(request);
    }


}
