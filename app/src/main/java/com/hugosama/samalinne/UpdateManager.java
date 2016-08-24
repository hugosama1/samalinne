package com.hugosama.samalinne;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.hugosama.samalinne.api.update.ServiceGenerator;
import com.hugosama.samalinne.api.update.UpdateService;
import com.hugosama.samalinne.api.update.entities.Version;
import com.hugosama.samalinne.events.ErrorEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

/**
 * Created by hugo on 8/22/16.
 */
public class UpdateManager extends AsyncTask<Void,Void,Void>{
    Context context;
    private static final String TAG = UpdateManager.class.getSimpleName();

    public UpdateManager( Context context ) {
        this.context = context;
    }


    public void update(){
        Version version = getCurrentVersion();
        if( version == null || version.getVersion() <= BuildConfig.VERSION_CODE ) {
            return;
        }
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = BuildConfig.APK_NAME;
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()){
            file.delete();
        }

        //get url of app on server
        String url =  BuildConfig.UPDATE_URL;


        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(context.getString(R.string.notification_description));
        request.setTitle(context.getString(R.string.app_name));

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri,
                        manager.getMimeTypeForDownloadedFile(downloadId));
                context.startActivity(install);
                context.unregisterReceiver(this);
            }
        };
        //register receiver for when .apk download is compete
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private Version getCurrentVersion() {
        Version version = null;
        try {
            UpdateService updateService = ServiceGenerator.createService(UpdateService.class);
            version = updateService.getVersion().execute().body();
        }catch (IOException ex) {
            Log.e(TAG, "update: ",ex);
            ErrorEvent error = new ErrorEvent();
            error.setDevMessage(ex.getMessage());
            error.setMessage("Error al checar la version, dile al Hugo :)");
            EventBus.getDefault().post(error);
        }
        return version;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.update();
        return null;
    }
}