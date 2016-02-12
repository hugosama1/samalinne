package com.hugosama.samalinne;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Typewriter writer = (Typewriter)findViewById(R.id.txt_message);
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText(getMessage());
    }

    private String getMessage() {
        String message = "Ups! no se pudo encontrar mensaje para hoy, pero hugo dejó el predeterminado: Te Amo";
        SQLiteDatabase db = new SamalinneDbHelper(this).getReadableDatabase();
        Cursor cursor = db.query(MessagesEntry.TABLE_NAME,
                new String[]{MessagesEntry.COLUMN_MESSAGE},
                "strftime('%m-%d', " + MessagesEntry.COLUMN_DATE + ", 'unixepoch' ) = strftime('%m-%d', 'now')",
                null,
                null,
                null,
                null
        );
        if(cursor.moveToNext()) {
            message = "\""+cursor.getString(0)+"\"";
        }
        return message;
    }


}
