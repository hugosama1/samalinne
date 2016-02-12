package com.hugosama.samalinne;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.os.Handler;
/**
 * Created by hgomez on 11/02/2016.
 */
public class Typewriter extends TextView {

    private int mIndex;
    private long mDelay = 100; //Default 500ms delay
    private String [] mWords;

    public Typewriter(Context context) {
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            append(mWords[mIndex++] + " ");
            if (mIndex < mWords.length) {
                mHandler.postDelayed(characterAdder,mDelay);
            }
        }
    };

    public void animateText(String text) {
        mWords = text.split(" ");
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}
