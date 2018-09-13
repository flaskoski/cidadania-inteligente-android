package com.laskoski.f.felipe.cidadania_inteligente.connection;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by Felipe on 9/12/2018.
 */

public class SslRequestQueue {
    RequestQueue mRequestQueue;
    public SslRequestQueue(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, new SslSocketFactoryConfiguration(context).getSslSocketFactory()));
    }
    public RequestQueue getSslRequesQueue(){
        return mRequestQueue;
    }
}
