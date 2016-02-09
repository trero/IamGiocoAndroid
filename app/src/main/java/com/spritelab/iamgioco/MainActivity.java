package com.spritelab.iamgioco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

public class MainActivity extends Activity
{
    private Button nuovaPartita;
    private Button negozio;
    private IamApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (IamApp) getApplication();

        nuovaPartita = (Button) findViewById(R.id.buttonNuovaPartita);
        negozio = (Button) findViewById(R.id.buttonNegozio);

        nuovaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getPartita(new ServerResponseListener() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Intent intent = new Intent(MainActivity.this, PartitaActivity.class);
                        intent.putExtra("data", result.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorCode, JSONObject extras) {

                    }
                });
            }
        });

        negozio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProvaConnectionActivity.class);
                startActivity(intent);
            }
        });
    }
}
