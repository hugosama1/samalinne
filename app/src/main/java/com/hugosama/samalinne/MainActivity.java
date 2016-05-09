package com.hugosama.samalinne;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;

public class MainActivity extends FragmentActivity {

    private static int TOTAL_IMAGE_FILES = 21;
    private static int TOTAL_SONG_FILES = 5;

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Typewriter writer = (Typewriter)findViewById(R.id.txt_message);
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText(getMessage());
        ImageView imgView = (ImageView) findViewById(R.id.imgBackgroud);
        int imgId = getRandomResource("img_","drawable",TOTAL_IMAGE_FILES);
        imgView.setImageResource(imgId);
        mediaPlayer = MediaPlayer.create(this, getRandomResource("song_","raw",TOTAL_SONG_FILES));
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    private int getRandomResource(String prefix, String res_folder, int total_files) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = 1 + (int)(Math.random() * total_files );
        int resId = getResources().getIdentifier(prefix+randomNum, res_folder, getPackageName());
        return resId;
    }

    private String getMessage() {
        String message = "Ups! no se pudo encontrar mensaje para hoy, pero hugo dejó el predeterminado: Te Amo";
        SQLiteDatabase db = new SamalinneDbHelper(this).getReadableDatabase();
        long currentTime = System.currentTimeMillis() / 1000;
        Cursor cursor = db.query(MessagesEntry.TABLE_NAME,
                new String[]{MessagesEntry.COLUMN_MESSAGE},
                "strftime('%m-%d', " + MessagesEntry.COLUMN_DATE + ", 'unixepoch' ) = " +
                        "strftime('%m-%d', '"+String.valueOf(currentTime)+"', 'unixepoch')",
                null,
                null,
                null,
                null
        );
        if(cursor.moveToNext()) {
            message = "\""+cursor.getString(0)+"\"";
        }
        cursor.close();
        cursor = db.query(MessagesEntry.TABLE_NAME,
                new String[]{MessagesEntry.COLUMN_MESSAGE,MessagesEntry.COLUMN_DATE,"strftime('%m-%d', " + MessagesEntry.COLUMN_DATE + ", 'unixepoch' )"},
                null,
                null,
                null,
                null,
                null
        );
        while( cursor.moveToNext()) {
            Log.e("", cursor.getString(0) +" | " + cursor.getString(1) +" | " + cursor.getString(2) + "\n");
        }
        return message;
    }


}
