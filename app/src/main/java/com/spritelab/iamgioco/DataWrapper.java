package com.spritelab.iamgioco;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AntonioVirgone on 08/02/16.
 */
public class DataWrapper implements Serializable
{
    private ArrayList<ImageView> data;

    public DataWrapper(ArrayList<ImageView> data)
    {
        this.data = data;
    }

    public ArrayList<ImageView> getData()
    {
        return data;
    }
}
