package com.hugosama.samalinne;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.hugosama.samalinne.data.SamalinneDbHelper;
import com.hugosama.samalinne.data.entities.DaoMaster;
import com.hugosama.samalinne.data.entities.DaoSession;


/**
 * Created by hugo on 9/15/16.
 */
public class Samalinne extends Application {

    private DaoSession daoSession;

    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        SQLiteDatabase db = new SamalinneDbHelper(this).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}