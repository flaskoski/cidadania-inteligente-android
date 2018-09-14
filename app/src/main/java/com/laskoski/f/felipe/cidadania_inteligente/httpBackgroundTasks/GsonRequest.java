package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;


public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Type clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private Map<String, String> requestParams;

    /**
     * Make a GET request and return a parsed object from JSON.
     *  @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param requestParams String to be inside the request body
     * @param listener
     */
    public GsonRequest(String url, Type clazz, Map<String, String> headers, Map<String, String> requestParams,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.requestParams = requestParams;
        this.listener = (Response.Listener<T>) listener;
    }
    public GsonRequest(String url, Type clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = (Response.Listener<T>) listener;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return requestParams;
    };

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>)Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}