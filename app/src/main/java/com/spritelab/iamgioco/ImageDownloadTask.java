package com.spritelab.iamgioco;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by AntonioVirgone on 08/02/16.
 */
public class ImageDownloadTask extends AsyncTask<Void, Void, Void>
{
    private String imageUrl;
    protected Drawable image;

    public ImageDownloadTask(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try {
            image = downloadTask(imageUrl);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable downloadTask(String url) throws IOException
    {
        InputStream inputStream = (InputStream) new URL(url).getContent();

        return Drawable.createFromStream(inputStream, "src name");
    }
}
