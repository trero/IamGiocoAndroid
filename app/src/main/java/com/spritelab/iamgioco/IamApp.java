package com.spritelab.iamgioco;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AntonioVirgone on 08/02/16.
 */
public class IamApp extends Application
{
    public static final String SERVER_HOST = "http://www.iam-inquadrami.com/";

    private SharedPreferences preferences;
    private  WebServiceClient wsc = new WebServiceClient(SERVER_HOST);

    @Override
    public void onCreate()
    {
        super.onCreate();

        preferences = getSharedPreferences("iam.dat", MODE_PRIVATE);
    }

    public void getPartita(final ServerResponseListener listener)
    {
        wsc.callServer("game/getpartita", new ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject result)
            {
                listener.onSuccess(result);
            }

            @Override
            public void onError(String errorCode, JSONObject extras)
            {
                listener.onError(errorCode, extras);
            }
        });
    }

    public void setRecord(String username, int punteggio)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("punteggio", "" + punteggio);

        wsc.callServer("game/setRecord", params, new ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject result)
            {
                boolean success = result.optBoolean("success", false);
                if(success) {
                    Log.i("Risposta", "" + success);
                }
            }

            @Override
            public void onError(String errorCode, JSONObject extras)
            {

            }
        });
    }

    public void getRecord()
    {
        wsc.callServer("game/getrecord", new ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onError(String errorCode, JSONObject extras) {

            }
        });
    }
}
