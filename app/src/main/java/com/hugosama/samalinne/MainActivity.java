package com.hugosama.samalinne;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hugosama.samalinne.api.update.ServiceGenerator;
import com.hugosama.samalinne.api.update.UpdateService;
import com.hugosama.samalinne.data.SamalinneContract;
import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;
import com.hugosama.samalinne.data.SamalinneDbHelper;
import com.hugosama.samalinne.data.entities.DaoMaster;
import com.hugosama.samalinne.data.entities.DaoSession;
import com.hugosama.samalinne.data.entities.Message;
import com.hugosama.samalinne.data.entities.MessageDao;
import com.hugosama.samalinne.events.ErrorEvent;
import com.hugosama.samalinne.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private static final int TOTAL_IMAGE_FILES = 21;
    private static final int TOTAL_SONG_FILES = 5;
    private String currentMessage;
    private TextToSpeech textToSpeech;
    private int year;
    private int month;
    private int day;
    @BindView(R.id.txtMessageDate)
    TextView txtMessageDate;
    @BindView(R.id.txt_message)
    Typewriter txtWriter;
    @BindView(R.id.imgBackgroud)
    ImageView imgView;
    private MediaPlayer mediaPlayer;
    private static int TEXT_TO_SPEECH_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        this.setDateText(year, month, day);
        update();
        setMessage(System.currentTimeMillis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_messages:
                Intent intent = new Intent(this, MessagesActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * updates messages
     */
    private void update() {
        if (Utils.isWifiConnected(this)) {
            new UpdateManager(this).execute();
        }

    }

    /**
     * sets the current message on screen, the song and image
     *
     * @param timeInMillis
     */
    private void setMessage(long timeInMillis) {
        currentMessage = getMessage(timeInMillis);
        //Add a character every 150ms
        txtWriter.setCharacterDelay(150);
        txtWriter.animateText(currentMessage);
        //set random background
        int imgId = getRandomResource("img_", "drawable", TOTAL_IMAGE_FILES);
        imgView.setImageResource(imgId);
        resetPlayer();
        playMusic();
        //say the message outloud
        if (Utils.isWifiConnected(this) && !Utils.isOnSilence(this)) {
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
                        if (status == TextToSpeech.SUCCESS) {
                            int result = textToSpeech.setLanguage(new Locale("spa", "MEX"));
                            if (result == TextToSpeech.LANG_MISSING_DATA ||
                                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("error", "This Language is not supported");
                            } else {
                                textToSpeech.speak(currentMessage, TextToSpeech.QUEUE_ADD, null);
                            }
                        } else {
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

    public void resetPlayer() {
        //play random music
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, getRandomResource("song_", "raw", TOTAL_SONG_FILES));
    }

    private void playMusic() {
        if (!Utils.isOnSilence(this) && !mediaPlayer.isPlaying()) {
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

    /**
     * calculates a random number and returns a random resource file (song or image)
     *
     * @param prefix      currently only img_ and song_ are supported
     * @param res_folder  the resource folder where the resources are located
     * @param total_files number of selectable resources in folder
     * @return
     */
    private int getRandomResource(String prefix, String res_folder, int total_files) {
        int randomNum = 1 + (int) (Math.random() * total_files);
        int resId = getResources().getIdentifier(prefix + randomNum, res_folder, getPackageName());
        return resId;
    }

    /**
     * Gets today's message
     *
     * @param timeInMillis the current day to deliver the message
     * @return
     */
    private String getMessage(long timeInMillis) {
        String message = getString(R.string.default_daily_message);
        long currentTime = SamalinneContract.normalizeDate(timeInMillis);
        MessageDao messageDao = ((Samalinne) this.getApplication()).getDaoSession().getMessageDao();
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.Date.eq(currentTime));
        Message dailyMessage = qb.count() >= 1 ? qb.list().get(0) : null;
        if (dailyMessage != null)
            message = "\"" + dailyMessage.getMessage() + "\"";
        return message;
    }

    /**
     * Sets the date of the message on screen and updates current date properties (year,month,day)
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    private void setDateText(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        this.txtMessageDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    /**
     * Creates a calendar datepicker to change the message of the day
     *
     * @param v
     */
    @OnClick(R.id.btnMessageDate)
    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDateText(year, monthOfYear, dayOfMonth);
                c.set(year, monthOfYear, dayOfMonth);
                setMessage(c.getTimeInMillis());
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime() + (1000 * 60 * 60 * 24));

        datePickerDialog.show();
    }

    /**
     * shares screenshot to whatsapp
     *
     * @param v
     */
    @OnClick(R.id.btnShareWhatsapp)
    public void shareScreenshot(View v) {
        /** * Show share dialog BOTH image and text */
        String imagePath = takeScreenshot();
        if (imagePath == null) {
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
            String errorMessage = getString(R.string.whatsapp_not_installed);
            ErrorEvent errorEvent = new ErrorEvent(errorMessage,
                    ex.getMessage());
            EventBus.getDefault().post(errorEvent);
        }
    }

    @OnClick(R.id.btnRandomMessage)
    public void randomMessage() {
        MessageDao messageDao = ((Samalinne) this.getApplication()).getDaoSession().getMessageDao();
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.orderRaw(" Random() ").limit(1);
        Message randomMessage = qb.count() >= 1 ? qb.list().get(0) : null;
        if (randomMessage != null) setMessage(randomMessage.getDate());
    }

    private void openScreenshot(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void sendToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private String takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String path = null;
        try {
            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            Bitmap bitmap = screenShot(v1);
            path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Samalinne", this.txtMessageDate.getText().toString());
            openScreenshot(path);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            String errorMessage = getString(R.string.error_image_not_captured);
            ErrorEvent errorEvent = new ErrorEvent(errorMessage, e.getMessage());
            EventBus.getDefault().post(errorEvent);
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
    public void onErrorEvent(ErrorEvent errorEvent) {
        sendToast(errorEvent.getMessage());
        if (BuildConfig.DEBUG) Log.e(TAG, errorEvent.getDevMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageEvent: llegaron mensajes");
        this.setMessage(System.currentTimeMillis());
    }


}
