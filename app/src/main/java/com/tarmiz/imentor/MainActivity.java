package com.tarmiz.imentor;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tarmiz.imentor.Fragments.Centers;
import com.tarmiz.imentor.Fragments.Dashboard;
import com.tarmiz.imentor.Fragments.Schools;
import com.tarmiz.imentor.Fragments.Special;
import com.tarmiz.imentor.Fragments.Teachers;
import com.tarmiz.imentor.Fragments.Universities;
import com.tarmiz.imentor.Utils.CustomTypefaceSpan;
import com.tarmiz.imentor.Utils.FontTypeface;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FontTypeface fontTypeface;
    Typeface typeface;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private Tracker mTracker;
    public MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        fontTypeface = new FontTypeface(this);
        typeface = fontTypeface.getTypefaceAndroid();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("الرئيسية");
        TextView textView = (TextView) toolbar.getChildAt(0);
        textView.setTypeface(typeface);
        textView.setTextSize(16.0f);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeTypeface(navigationView);

        Fragment fragment = new Dashboard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
/*
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    private void applyExit() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this,"Press Again to exit", Toast.LENGTH_LONG).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
       FragmentManager fm = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (fm.getFragments().size() <= 1) {
            applyExit();
        } else {
            for (Fragment frag : fm.getFragments()) {
                if (frag == null) {
                    applyExit();
                    return;
                }
                if (frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getFragments() == null) {
                        super.onBackPressed();
                        return;
                    }
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        return;
                    } else {
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        return;
                    }
                }
            }
        }
    }

*/
/*
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
*/


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.dashboard:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("الصفحة الرئيسية")
                        .setAction("صفحة الاعلانات")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Dashboard())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("الرئيسية");
                break;

            case R.id.schools:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("المدارس")
                        .setAction("صفحة المدارس")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Schools())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("المدارس");
                break;

            case R.id.universities:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("الجامعات")
                        .setAction("صفحة الجامعات")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Universities())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("الجامعات");
                break;

            case R.id.centers:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("المراكز التعليمية")
                        .setAction("صفحة المراكز")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Centers())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("مراكز التدريب");
                break;


            case R.id.teachers:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("المعلمين")
                        .setAction("صفحة المدرسين")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Teachers())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("مدرسين خصوصي");
                break;

            case R.id.special:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("مراكز ذوي الاحتياجات")
                        .setAction("صفحة مراكز ذوي الاحتياجات")
                        .build());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Special())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                toolbar.setTitle("مراكز تأهيل");
                break;

            case R.id.share:
                try {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("القائمة")
                            .setAction("زر مشاركة التطبيق")
                            .build());

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "تطبيق iMentor دليلك نحو التعليم");
                    String sAux = "";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.tarmiz.imentor \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.about:
                Intent i = new Intent(MainActivity.this, About.class);
                startActivity(i);
                break;



        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeTypeface(NavigationView navigationView) {
        MenuItem item;
        item = navigationView.getMenu().findItem(R.id.dashboard);
        item.setTitle(R.string.nav_menu_dashboard);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.schools);
        item.setTitle(R.string.nav_menu_schools);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.universities);
        item.setTitle(R.string.nav_menu_universities);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.centers);
        item.setTitle(R.string.nav_menu_centers);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.teachers);
        item.setTitle(R.string.nav_menu_teachers);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.special);
        item.setTitle(R.string.nav_menu_special);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.share);
        item.setTitle(R.string.nav_menu_share);
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.about);
        item.setTitle(R.string.nav_menu_aboutus);
        applyFontToItem(item, typeface);


    }

    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 14), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }


}