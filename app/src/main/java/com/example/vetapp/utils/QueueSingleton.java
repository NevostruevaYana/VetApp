package com.example.vetapp.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class QueueSingleton {

    private static QueueSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private QueueSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized QueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new QueueSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
