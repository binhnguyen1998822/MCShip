package com.example.nguyen.tokentesst.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.example.nguyen.tokentesst.MainActivity;
import com.example.nguyen.tokentesst.MainLoginActivity;
import com.example.nguyen.tokentesst.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ScreenActivity extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000;
    ImageView img;

    /**
     * Called when the activity is first created.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        img = (ImageView) findViewById(R.id.screen);
        // thread for displaying the SplashScreen


        checkConnection();



    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            Thread splashTread = new Thread() {

                @Override
                public void run() {

                    try {
                        int waited = 0;
                        while (_active && (waited < _splashTime)) {
                            sleep(100);
                            if (_active) {
                                waited += 100;
                            }
                        }
                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                        hide(img);
                        finish();
                        Intent mainIntent = new Intent(ScreenActivity.this, MainLoginActivity.class);
                        ScreenActivity.this.startActivity(mainIntent);
                        ScreenActivity.this.finish();
                    }
                }
            };
            splashTread.start();
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ScreenActivity.this);

            alertbox.setTitle("Cảnh Báo");
            alertbox.setMessage("Ứng dụng cần kết nối internet !!!");

            alertbox.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });


            alertbox.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            alertbox.show();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hide(final View view) {

        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        int initialRadius = view.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                initialRadius, 0);
        anim.setDuration(2000);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void show(final View view) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                0, finalRadius);
        anim.setDuration(1000);

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }
}