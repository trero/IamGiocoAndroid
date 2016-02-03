package com.spritelab.iamgioco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PunteggioFinale extends Activity {

    private Button tastoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punteggio_finale);

        tastoHome = (Button) findViewById(R.id.buttonHome);
        tastoHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PunteggioFinale.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }



}
