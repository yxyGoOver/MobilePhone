package com.jumu.mobilephone.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Administrator
 */
public abstract class BaseSetupActivity extends Activity {
    GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX()-e2.getX()>100){
                     showNextPage();
                }
                if (e1.getX()-e2.getX()<-100){
                   showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showPrePage();

    protected abstract void showNextPage();

    // public abstract void nextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
          gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void nextPage(View view){
         showNextPage();
    }
    public void previousPage(View view){
        showPrePage();
    }
}
