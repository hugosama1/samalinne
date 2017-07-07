package com.hugosama.samalinne;

import android.app.Application;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.hugosama.samalinne.data.entities.MessageDao;
import com.hugosama.samalinne.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.WhereCondition;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";

    @BindView(R.id.lstMessages)
    ListView lstMessages;

    SimpleCursorAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        MessageDao messageDao = ((Samalinne) this.getApplication())
                .getDaoSession()
                .getMessageDao();
        cursor = messageDao.queryBuilder()
                .where(MessageDao.Properties.Message.isNotNull())
                .buildCursor()
        .forCurrentThread().query();
        String [] from = {MessageDao.Properties.Message.columnName};
        int [] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
                cursor,from,to,1);
        lstMessages.setAdapter(adapter);
        // Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        Log.d(TAG, "onMessageEvent: llegaron mensajes");
        adapter.notifyDataSetChanged();
    }



}
