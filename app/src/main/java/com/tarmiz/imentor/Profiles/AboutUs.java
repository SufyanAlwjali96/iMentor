package com.tarmiz.imentor.Profiles;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;


public class AboutUs extends AppCompatActivity {
    Toolbar toolbar;
     Typeface typeface;
    FontTypeface fontTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.about_us);
        fontTypeface = new FontTypeface(this);
        typeface = fontTypeface.getTypefaceAndroid();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("تطبيق iMentor");
        TextView textView = (TextView) toolbar.getChildAt(0);
        textView.setTypeface(typeface);
        textView.setTextSize(16.0f);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
    }
}
