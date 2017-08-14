package com.example.arsalankhan.synchronizesqlitemysql;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class MySingleton {

    private static Context context;
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;


    private MySingleton(Context context){
        this.context = context;

        mRequestQueue = getRequestQueue(context);
    }

    public RequestQueue getRequestQueue(Context context) {

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return mRequestQueue;
    }

    public static synchronized  MySingleton getInstance(Context context){

        if(mInstance == null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }


    public <T> void addToRequestQueue(Request<T> request){
        mRequestQueue.add(request);
    }
}
