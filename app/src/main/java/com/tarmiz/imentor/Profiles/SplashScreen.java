package com.tarmiz.imentor.Profiles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tarmiz.imentor.MainActivity;
import com.tarmiz.imentor.R;

public class SplashScreen extends AppCompatActivity {
    ImageView img;
    Animation anim1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim_down);
        img = findViewById(R.id.Logo);
        //img.setAnimation(anim1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();

            }
        }, 3000);
    }
}
