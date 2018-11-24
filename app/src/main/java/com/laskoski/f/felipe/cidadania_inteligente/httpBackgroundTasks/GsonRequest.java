package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.laskoski.f.felipe.cidadania_inteligente.model.AbstractTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.LocationTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private Gson taskGson;
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
        createTaskGson();
    }
    public GsonRequest(String url, Type clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = (Response.Listener<T>) listener;
        createTaskGson();
    }

    private void createTaskGson(){
        final RuntimeTypeAdapterFactory<AbstractTask> typeFactory = RuntimeTypeAdapterFactory
                .of(AbstractTask.class, "classType") // Here you specify which is the parent class and what field particularizes the child class.
                .registerSubtype(QuestionTask.class, "app.model.QuestionTask") // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
                .registerSubtype(LocationTask.class, "app.model.LocationTask");
        this.taskGson = new GsonBuilder().registerTypeAdapterFactory(typeFactory).create();
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
            assert json != null;
            if(new TypeToken<List<AbstractTask>>(){}.getType().equals(clazz))
                return (Response<T>)Response.success(
                        taskGson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            else
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