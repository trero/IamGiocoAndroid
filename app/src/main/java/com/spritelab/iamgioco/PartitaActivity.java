package com.spritelab.iamgioco;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PartitaActivity extends Activity
{
    private ImageView quadro;
    private Button responseOne;
    private Button responseTwo;
    private Button responseThree;
    private Button responseFour;
    private Button listaRisposte[];

    private ArrayList<Drawable> imageList;
    private int count = 0;
    private JSONArray game;
    private int round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partita);

        quadro = (ImageView) findViewById(R.id.quadro);
        responseOne = (Button) findViewById(R.id.responseOne);
        responseTwo = (Button) findViewById(R.id.responseTwo);
        responseThree = (Button) findViewById(R.id.responseThree);
        responseFour = (Button) findViewById(R.id.responseFour);

        listaRisposte = new Button[] {responseOne, responseTwo, responseThree, responseFour};

        Intent intent = getIntent();
        dowloadImage(intent);
    }

    private void dowloadImage(Intent intent)
    {
        try {
            JSONObject result = new JSONObject(intent.getStringExtra("data"));
            game = result.getJSONArray("game");

            count = game.length();
            imageList = new ArrayList<Drawable>();

            for(int i = 0; i < game.length(); i++) {
                JSONObject risposta = game.getJSONObject(i).getJSONObject("risposta");

                String url = "http://www.iam-inquadrami.com/assets/quadri/" + risposta.getString("autore") + "/" + risposta.getString("url");

                ImageDownloadTask task = new ImageDownloadTask(url) {
                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        imageList.add(this.image);
                        count--;
                        if(count <= 0) {
                            startGame();
                        }
                    }
                };
                task.execute();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startGame()
    {
        createGame(0);
    }

    private void createGame(int indice)
    {
        if(indice >= game.length()) {
            Log.i("PartitaActivity", "Fine della partita");
            return;
        }

        quadro.setImageDrawable(imageList.get(indice));
        try {
            JSONArray round = game.getJSONObject(indice).getJSONArray("round");
//            Log.i("PartitaActivity", round.toString());

            for(int i = 0; i < 4; i++) {
                String titolo = round.getJSONObject(i).getString("titolo");
                Log.i("PartitaActivity", titolo);

                listaRisposte[i].setText("" + titolo);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
