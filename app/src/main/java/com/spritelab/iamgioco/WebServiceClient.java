package com.spritelab.iamgioco;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thor on 05/02/2016.
 */
public class WebServiceClient {

    private static final String LOG_TAG = WebServiceClient.class.getSimpleName();
    private static final int CONNECTION_TIMEOUT = 10000;

    private String serverUrl;

    public WebServiceClient(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public void callServer(String service, ServerResponseListener listener)
    {
        Map<String, String> params = new HashMap<String, String>();

        callServer(service, params, listener);
    }

    public void callServer(final String service, final Map<String, String> paramsMap, final ServerResponseListener responseListener)
    {
        final String serverUrl = this.serverUrl + service;
        final String httpUsername = "";
        final String httpPassword = "";
        final String base64Auth = (httpUsername.length() > 0 && httpPassword.length() > 0) ? Base64.encodeToString((httpUsername + ":" + httpPassword).getBytes(), Base64.DEFAULT) : null;

        if(serverUrl.length() > 0) {
            new AsyncTask<Void, Void, Object>() {
                @Override
                protected void onPreExecute()
                {
                }

                @Override
                protected Object doInBackground(Void... parameters)
                {
                    JSONObject result = null;
                    Object error = null;

                    HttpURLConnection urlConnection = null;
                    error = null;

                    try {
                        StringBuilder params = new StringBuilder();
                        for(Map.Entry<String, String> entry: paramsMap.entrySet()) {
                            params.append(params.length() == 0 ? "?" : "&");
                            params.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
                        }

                        URL url = new URL(serverUrl + params);

                        Log.i(LOG_TAG, serverUrl + params);

                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                        urlConnection.setReadTimeout(CONNECTION_TIMEOUT);

                        if(base64Auth != null) {
                            urlConnection.setRequestProperty("Authorization", "Basic " + base64Auth);
                        }

                        StringBuilder jsonData = new StringBuilder();
                        byte[] buffer = new byte[1024];
                        int size;

                        InputStream in = urlConnection.getInputStream();
                        while(true) {
                            size = in.read(buffer);
                            if(size < 0) break;
                            jsonData.append(new String(buffer, 0, size));
                        }
                        in.close();

                        Log.d("jsonData", jsonData.toString());
                        result = new JSONObject(jsonData.toString());
                    }
                    catch(MalformedURLException e) {
                        e.printStackTrace();
//                        error = getString(R.string.malformed_url_error_message);
                    }
                    catch(IOException e) {
                        e.printStackTrace();
//                        error = getString(R.string.connection_error_message);
                    }
                    catch(JSONException e) {
                        e.printStackTrace();
//                        error = getString(R.string.corrupted_error_message);
                    }

                    return error != null ? error : result;
                }

                @Override
                protected void onPostExecute(Object backgroundResult)
                {
                    if(responseListener != null) {
                        if(backgroundResult instanceof JSONObject) {
                            JSONObject result = (JSONObject)backgroundResult;
                            if(result.optBoolean("success", false)) {
                                responseListener.onSuccess(result);
                            }
                            else {
                                responseListener.onError(result.optString("error", "Unknow error!"), result.optJSONObject("extras"));
                            }
                        }
                        else responseListener.onError(backgroundResult.toString(), null);
                    }
                }
            }.execute();
        }
        else {
//            if(responseListener != null) responseListener.onError(getString(R.string.malformed_url_error_message), null);
        }
    }
}

