package com.example.weatherappmvc;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueManager {
    private RequestQueue mRequestQueue;

    public RequestQueueManager(Context context) {
        this.mRequestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
