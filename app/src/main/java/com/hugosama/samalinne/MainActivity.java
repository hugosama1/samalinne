package com.hugosama.samalinne;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Typewriter writer = (Typewriter)findViewById(R.id.txt_message);
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText(getMessage());
        ImageView imgView = (ImageView) findViewById(R.id.imgBackgroud);
        int imgId = getRandomImage();
        imgView.setImageResource(imgId);
    }
    private int getRandomImage() {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = 1 + (int)(Math.random() * 21 );
            int imgId = getResources().getIdentifier("img_"+5, "drawable", getPackageName());
        return imgId;
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
