package com.hugosama.samalinne;

import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hugo on 8/24/16.
 */
public class Utils {

    public static boolean isWifiConnected(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi != null && mWifi.isConnected();
    }

    public static boolean isOnSilence(Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() < AudioManager.RINGER_MODE_NORMAL;
    }

}
