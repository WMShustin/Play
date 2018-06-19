package de.justfamouzin.play.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.justfamouzin.play.Play;
import de.justfamouzin.play.R;

/**
 * @author Justin@Famouz
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Play(SplashScreenActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, SignInActivity.class);
                Log.d("XXX", "sign in");
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
