package com.example.myapplication2.app;

import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by foxefj on 4/15/14.
 */
public class PriorityJsonObjectRequest extends JsonObjectRequest {
    private Priority priority = Priority.IMMEDIATE;

    public PriorityJsonObjectRequest(int method, java.lang.String url, org.json.JSONObject jsonRequest, com.android.volley.Response.Listener<org.json.JSONObject> listener, com.android.volley.Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Priority getPriority(){
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
