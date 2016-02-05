package com.spritelab.iamgioco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public WebServiceClient serverUrl;
    public JSONArray risposta;

    private Button nuovaPartita;
    private Button negozio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nuovaPartita = (Button) findViewById(R.id.buttonNuovaPartita);
        nuovaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PartitaActivity.class);
                startActivity(intent);
            }
        });

        negozio = (Button) findViewById(R.id.buttonNegozio);
        negozio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProvaConnectionActivity.class);
                intent.putExtra("name", list);
                String id = intent.getStringExtra("id");
                startActivity(intent);
            }
        });

        serverUrl = new WebServiceClient("http://www.iam-inquadrami.com/");
        serverUrl.callServer("game/getpartita", new ServerResponseListener() {

            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject game = result.getJSONObject("game");
                    risposta = game.getJSONArray("risposta");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //TextView textView = (TextView) findViewById(R.id.textViewProvaConnection);
                //textView.setText(result.toString());

            }

            @Override
            public void onError(String errorCode, JSONObject extras) {

            }

        });
    }
}
