package com.example.myapplication2.app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by foxefj on 4/14/14.
 */
public class VolleyDao {
    private RequestQueue requestQueue;
    private static VolleyDao dao;
    private static final String RANDOM_URL = "http://api.icndb.com/jokes/random";
    private static final String JOKE_URL = "http://api.icndb.com/jokes/";

    private VolleyDao(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyDao getInstance(Context context) {
        if (dao == null)
            dao = new VolleyDao(context);

        return dao;
    }

    public void getRandomJoke(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RANDOM_URL, null, responseListener, errorListener);
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void getJokeById(int id, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        if (id <= 0)
            return;

        String url = JOKE_URL + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        request.setShouldCache(true);
        requestQueue.add(request);
    }

    public void getImmediateJokeById(int id, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        if (id <= 0)
            return;

        String url = JOKE_URL + id;
        PriorityJsonObjectRequest request = new PriorityJsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        request.setShouldCache(true);
        request.setPriority(Request.Priority.IMMEDIATE);
        requestQueue.add(request);
    }

    public String getCachedJokeById(int id) {
        if (id <= 0)
            return null;

        String url = JOKE_URL + id;
        if(requestQueue.getCache().get(url) != null){
            try {
                JSONObject obj = new JSONObject(new String(requestQueue.getCache().get(url).data));
                return "From Cache: \n" + obj.getJSONObject("value").getString("joke");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void clearCache() {
        requestQueue.getCache().clear();
    }
}
