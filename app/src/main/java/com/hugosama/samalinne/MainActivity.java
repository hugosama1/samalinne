package com.hugosama.samalinne;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;
import com.hugosama.samalinne.events.ErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private static int TOTAL_IMAGE_FILES = 21;
    private static int TOTAL_SONG_FILES = 5;
    private String currentMessage;
    private TextToSpeech textToSpeech;
    private int year;
    private int month;
    private int day;
    @BindView(R.id.txtMessageDate)  TextView txtMessageDate;
    @BindView(R.id.txt_message) Typewriter txtWriter;
    @BindView(R.id.imgBackgroud) ImageView imgView;
    private MediaPlayer mediaPlayer;
    private static int TEXT_TO_SPEECH_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setMessage(System.currentTimeMillis());
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        this.setDateText(year,month,day);
        update();
    }

    private void  update() {
        if (Utils.isWifiConnected(this)) {
            new UpdateManager(this).execute();
        }
    }

    private void setMessage(long timeInMillis) {
        currentMessage = getMessage(timeInMillis);
        //Add a character every 150ms
        txtWriter.setCharacterDelay(150);
        txtWriter.animateText(currentMessage);
        //set random background
        int imgId = getRandomResource("img_","drawable",TOTAL_IMAGE_FILES);
        imgView.setImageResource(imgId);
        //play random music
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, getRandomResource("song_","raw",TOTAL_SONG_FILES));
        playMusic();
        //say the message outloud
        if( Utils.isWifiConnected(this)) {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, TEXT_TO_SPEECH_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TEXT_TO_SPEECH_REQUEST_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=textToSpeech.setLanguage(new Locale("spa","MEX"));
                            if(result==TextToSpeech.LANG_MISSING_DATA ||
                                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                textToSpeech.speak(currentMessage, TextToSpeech.QUEUE_ADD, null);
                            }
                        }else {
                            Log.e("error", "Initialization Failed!");
                        }
                    }
                });
            } else {
                // missing data, install it
                Log.d(TAG, "onActivityResult: instalar");
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
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

    /**
     * Sets the date of the message on screen and updates current date properties (year,month,day)
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    private void setDateText( int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        this.txtMessageDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year );
    }

    @OnClick(R.id.btnMessageDate)
    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
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

    @OnClick(R.id.btnShareWhatsapp)
    public void shareScreenshot(View v) {
        /** * Show share dialog BOTH image and text */
        String imagePath = takeScreenshot();
        if(imagePath ==null) {
            return;
        }
        Uri imageUri = Uri.parse(imagePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_TEXT, txtWriter.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            sendToast("Whatsapp no se encontraba instalado...");
        }
    }

    private void openScreenshot(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void sendToast(String message) {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_LONG);
        toast.show();
    }

    private String takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        File imageFile = null;
        String path = null;
        try {
            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            Bitmap bitmap =screenShot(v1);
            path = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap , "Samalinne", this.txtMessageDate.getText().toString());
            openScreenshot(path);
        } catch (Throwable e) {
            Log.e("", "takeScreenshot: ",e );
            // Several error may come out with file handling or OOM
            sendToast("Error al tomar captura de pantalla, favor de contactar a hugo !");
            e.printStackTrace();
        }
        return path;
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent){
        sendToast(errorEvent.getMessage());
    }


}
