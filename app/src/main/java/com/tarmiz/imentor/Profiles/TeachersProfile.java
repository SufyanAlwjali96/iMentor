package com.tarmiz.imentor.Profiles;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarmiz.imentor.Models.Teacher;
import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;

import cn.refactor.lib.colordialog.PromptDialog;
import io.realm.Realm;

public class TeachersProfile extends AppCompatActivity {
    TextView name, about, address, detals, materials,name2;
    ImageView avatar;
    TextView phone;
    Toolbar toolbar;
    FontTypeface fontTypeface;
    Typeface typeface;
    int teacher_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.teachers_profile);

        fontTypeface = new FontTypeface(this);
        typeface = fontTypeface.getTypefaceAndroid();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("معلومات عامة");
        TextView textView = (TextView) toolbar.getChildAt(0);
        textView.setTypeface(typeface);
        textView.setTextSize(16.0f);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);


        teacher_id = getIntent().getIntExtra("teacher_id", 0);

        fontTypeface = new FontTypeface(this);
        typeface = fontTypeface.getTypefaceAndroid();


        //image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        avatar = findViewById(R.id.avatar);
        about = findViewById(R.id.about);
        name2 = findViewById(R.id.name2);
        address = findViewById(R.id.address);
        detals = findViewById(R.id.details);
        materials = findViewById(R.id.materials);
        initializeData();


    }






    private void initializeData() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        final Teacher saved_teachers = realm
                .where(Teacher.class)
                .equalTo("id", teacher_id)
                .findFirst();
        realm.commitTransaction();

        name.setText(saved_teachers.getName());
        about.setText(saved_teachers.getName());
        name2.setText(saved_teachers.getName());
        detals.setText(saved_teachers.getDetails());
        materials.setText(saved_teachers.getMaterials());
        avatar.setImageURI(Uri.parse(saved_teachers.getPhoto()));

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(TeachersProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.avatar_info);
                ImageView name = dialog.findViewById(R.id.logoinfo);
                name.setImageURI(Uri.parse(saved_teachers.getPhoto()));
                dialog.show();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Dialog dialog = new Dialog(SchoolProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.phone_info);
                TextView textView = dialog.findViewById(R.id.address);
                TextView textView2 = dialog.findViewById(R.id.name);
                textView2.setText(saved_school.getName());
                textView.setText(saved_school.getAddress());
                dialog.show();
                */
                new PromptDialog(TeachersProfile.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)
                        .setTitleText(saved_teachers.getName())
                        .setContentText(saved_teachers.getAddress())
                        .show();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking for null feed url

                if (saved_teachers.getPhone() == null) {
                    Toast.makeText(TeachersProfile.this, "No Phone", Toast.LENGTH_SHORT).show();
                } else {

                    Dialog dialog = new Dialog(TeachersProfile.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    dialog.setContentView(R.layout.phone_info);
                    TextView name = dialog.findViewById(R.id.name);
                    name.setText(saved_teachers.getName());

                    TextView phone = dialog.findViewById(R.id.phone);
                    TextView phone2 = dialog.findViewById(R.id.phone2);

                    phone.setText(saved_teachers.getPhone());
                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mobileNo = saved_teachers.getPhone();
                            String uri = "tel:" + mobileNo.trim();
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (ActivityCompat.checkSelfPermission(TeachersProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(intent);
                        }
                    });
                    dialog.show();


                    phone2.setText(saved_teachers.getPhone2());
                    phone2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mobileNo = saved_teachers.getPhone2();
                            String uri = "tel:" + mobileNo.trim();
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (ActivityCompat.checkSelfPermission(TeachersProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(intent);
                        }
                    });
                    dialog.show();

                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);

    }


}
