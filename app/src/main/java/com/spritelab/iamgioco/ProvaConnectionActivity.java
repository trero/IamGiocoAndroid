package com.spritelab.iamgioco;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvaConnectionActivity extends Activity
{
    private ImageView quadro;
    private ArrayList<Drawable> imageList;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova_connection);

        quadro = (ImageView) findViewById(R.id.quadroImageView);

        Intent intent = getIntent();

        try {
            JSONObject result = new JSONObject(intent.getStringExtra("data"));
            JSONArray game = result.getJSONArray("game");

            count = game.length();
            imageList = new ArrayList<Drawable>();

            for(int i = 0; i < game.length(); i++) {
                JSONObject risposta = game.getJSONObject(i).getJSONObject("risposta");

                String url = "http://www.iam-inquadrami.com/assets/quadri/" + risposta.getString("autore") + "/" + risposta.getString("url");

                ImageView imageView = new ImageView(ProvaConnectionActivity.this);
                ImageDownloadTask task = new ImageDownloadTask(url) {
                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        imageList.add(this.image);
                        count--;
                        prepareForIntent();
                    }
                };
                task.execute();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareForIntent()
    {
        if(count <= 0) {
            quadro.setImageDrawable(imageList.get(0));
        }
    }
}
