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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class PartitaActivity extends Activity
{
    private IamApp app;

    private TextView nomeGiocatore;
    private TextView punteggioGiocatore;
    private ImageView quadro;
    private ImageView erroreUno;
    private ImageView erroreDue;
    private ImageView erroreTre;
    private ImageView listaErrori[];
    private Button responseOne;
    private Button responseTwo;
    private Button responseThree;
    private Button responseFour;
    private Button listaRisposte[];

    private ArrayList<Drawable> imageList;
    private JSONArray game;
    private int count = 0;
    private int round = 0;
    private int errorCount = 0;
    private int punteggio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partita);

        app = (IamApp) getApplication();

        nomeGiocatore = (TextView) findViewById(R.id.nomeGiocatore);
        punteggioGiocatore = (TextView) findViewById(R.id.punteggioGiocatore);
        quadro = (ImageView) findViewById(R.id.quadro);
        erroreUno = (ImageView) findViewById(R.id.erroreUno);
        erroreDue = (ImageView) findViewById(R.id.erroreDue);
        erroreTre = (ImageView) findViewById(R.id.erroreTre);
        responseOne = (Button) findViewById(R.id.responseOne);
        responseTwo = (Button) findViewById(R.id.responseTwo);
        responseThree = (Button) findViewById(R.id.responseThree);
        responseFour = (Button) findViewById(R.id.responseFour);

        listaErrori = new ImageView[] {erroreUno, erroreDue, erroreTre};
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
            Log.i("Fine", "Partita terminata! Hai fatto " + punteggio);
            app.setRecord("Antonio", punteggio);
            return;
        }

        quadro.setImageDrawable(imageList.get(indice));
        try {
            JSONArray round = game.getJSONObject(indice).getJSONArray("round");
            final String risposta = game.getJSONObject(indice).getJSONObject("risposta").getString("titolo");

            for(int i = 0; i < 4; i++) {
                String titolo = round.getJSONObject(i).getString("titolo");
                Log.i("PartitaActivity", titolo);

                listaRisposte[i].setText("" + titolo);
                listaRisposte[i].setTag(titolo);
                listaRisposte[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagButton = (String) v.getTag();
                        verificaRisposta(risposta, tagButton);
                    }
                });
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verificaRisposta(String rispostaCorretta, String rispostaUtente)
    {
        if(rispostaCorretta.equalsIgnoreCase(rispostaUtente)) {
            round++;
            punteggio += 100;
            createGame(round);
        }
        else {
            listaErrori[errorCount].setVisibility(View.GONE);
            errorCount++;
            punteggio -= 20;
        }

        punteggioGiocatore.setText("Punti: " + punteggio);

        if(errorCount >= listaErrori.length) {
            Toast.makeText(this, "Hai perso tutte le vite!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
