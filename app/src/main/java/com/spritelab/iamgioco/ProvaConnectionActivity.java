package com.spritelab.iamgioco;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;


public class ProvaConnectionActivity extends Activity {

    public WebServiceClient serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova_connection);

        serverUrl = new WebServiceClient("http://www.iam-inquadrami.com/");
        serverUrl.callServer("game/getpartita", new ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject result) {
                TextView textView = (TextView) findViewById(R.id.textViewProvaConnection);
                textView.setText(result.toString());
            }

            @Override
            public void onError(String errorCode, JSONObject extras) {

            }
        });

    }
}
