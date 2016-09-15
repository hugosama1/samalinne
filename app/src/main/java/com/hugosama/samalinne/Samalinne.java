package com.hugosama.samalinne;

import android.app.Application;

import com.facebook.stetho.Stetho;


/**
 * Created by hugo on 9/15/16.
 */
public class Samalinne extends Application {
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}