package com.hugosama.samalinne;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    private static int TOTAL_IMAGE_FILES = 21;
    private static int TOTAL_SONG_FILES = 5;
    @BindView(R.id.txtMessageDate)  TextView txtMessageDate;
    @BindView(R.id.txt_message) Typewriter txtWriter;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        setMessage(System.currentTimeMillis());
        ImageView imgView = (ImageView) findViewById(R.id.imgBackgroud);
        int imgId = getRandomResource("img_","drawable",TOTAL_IMAGE_FILES);
        imgView.setImageResource(imgId);
        mediaPlayer = MediaPlayer.create(this, getRandomResource("song_","raw",TOTAL_SONG_FILES));
        playMusic();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        this.setDateText(year,month,day);
    }

    private void setMessage(long timeInMillis) {
        //Add a character every 150ms
        txtWriter.setCharacterDelay(150);
        txtWriter.animateText(getMessage(timeInMillis));
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseMusic();
    }

    private void playMusic() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            mediaPlayer.start();
        }
    }

    private void pauseMusic() {
        mediaPlayer.pause();

    }
    @Override
    protected void onResume() {
        super.onResume();
        playMusic();
    }

    private int getRandomResource(String prefix, String res_folder, int total_files) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = 1 + (int)(Math.random() * total_files );
        int resId = getResources().getIdentifier(prefix+randomNum, res_folder, getPackageName());
        return resId;
    }

    private String getMessage(long timeInMillis) {
        String message = "Ups! no se pudo encontrar mensaje para hoy, pero hugo dejó el predeterminado: Te Amo";
        SQLiteDatabase db = new SamalinneDbHelper(this).getReadableDatabase();
        long currentTime = timeInMillis/ 1000;
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

    private void setDateText( int year, int monthOfYear, int dayOfMonth) {
        this.txtMessageDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year );
    }

    public void showDatePickerDialog(View v) {
        Log.d("","SHOW DATE PICKER");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDateText(year,monthOfYear,dayOfMonth);
                c.set( year,  monthOfYear,  dayOfMonth);
                setMessage(c.getTimeInMillis());
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        c.set(year,0,1);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }



}
