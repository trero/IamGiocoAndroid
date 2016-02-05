package com.spritelab.iamgioco;

import org.json.JSONObject;

/**
 * Created by Thor on 05/02/2016.
 */
public interface ServerResponseListener {
    public void onSuccess(JSONObject result);
    public void onError(String errorCode, JSONObject extras);
}
