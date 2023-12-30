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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.tarmiz.imentor.Models.Gallary;
import com.tarmiz.imentor.Models.Sneeds;
import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;

import cn.refactor.lib.colordialog.PromptDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class SneedProfile extends AppCompatActivity {
    TextView details,name;
    LinearLayout address, phone, map;
    Toolbar toolbar;
    FontTypeface fontTypeface;
    Typeface typeface;
    int sneeds_id;
    static String[] images;
    String[] labels;
     SliderLayout sliderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_sneed_profile);
        sneeds_id = getIntent().getIntExtra("sneeds_id", 0);

        fontTypeface = new FontTypeface(this);
        typeface = fontTypeface.getTypefaceAndroid();

        toolbar = findViewById(R.id.stoolbar);
        name = findViewById(R.id.name);
        details = findViewById(R.id.details);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        map = findViewById(R.id.map);
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SWAP); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        getImages();
        setSliderViews();


        initializeData();

    }

    private void initializeData() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        final Sneeds saved_Sneeds = realm
                .where(Sneeds.class)
                .equalTo("id", sneeds_id)
                .findFirst();
        realm.commitTransaction();

        toolbar.setTitle(saved_Sneeds.getName());
        TextView textView = (TextView) toolbar.getChildAt(0);
        textView.setTypeface(typeface);
        textView.setTextSize(16.0f);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        name.setText(saved_Sneeds.getName());

        details.setText(saved_Sneeds.getDetails());


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PromptDialog(SneedProfile.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)
                        .setTitleText(saved_Sneeds.getName())
                        .setContentText(saved_Sneeds.getAddress())
                        .show();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*
                String mobileNo = saved_university.getPhone();
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(UniversityProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                */
                Dialog dialog = new Dialog(SneedProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.phone_info);
                TextView name = dialog.findViewById(R.id.name);
                name.setText(saved_Sneeds.getName());

                TextView phone = dialog.findViewById(R.id.phone);
                TextView phone2 = dialog.findViewById(R.id.phone2);

                phone.setText(saved_Sneeds.getPhone());
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNo = saved_Sneeds.getPhone();
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(SneedProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                phone2.setText(saved_Sneeds.getPhone2());
                phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNo = saved_Sneeds.getPhone2();
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(SneedProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Double lat = saved_Sneeds.getLatitude();
                final Double lng = saved_Sneeds.getLongitude();
                Dialog dialog = new Dialog(SneedProfile.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setContentView(R.layout.map_dialog_layout);////your custom content

                MapView mMapView = dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(SneedProfile.this);
                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        LatLng posisiabsen = new LatLng(lat, lng); ////your lat lng
                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);// change type of google map
                        googleMap.addMarker(new MarkerOptions().position(posisiabsen).title(saved_Sneeds.getName())).showInfoWindow();

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    }
                });

                dialog.show();
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
    private void getImages() {
        Realm realm = Realm.getDefaultInstance();
        if (!realm.isInTransaction()) {
            realm.beginTransaction();

            RealmResults<Gallary> itemRealmResults = realm
                    .where(Gallary.class)
                    .equalTo("ownerId", sneeds_id)
                    .equalTo("type", "sp")
                    .findAll();

            images = new String[itemRealmResults.size()];
            labels = new String[itemRealmResults.size()];

            for (int i = 0; i < itemRealmResults.size(); i++) {
                if (itemRealmResults.get(i) != null) {
                    images[i] = itemRealmResults.get(i).getImage();
                    labels[i] = itemRealmResults.get(i).getLabel();

                }
            }
            realm.commitTransaction();
            realm.close();
        } else {
            realm.commitTransaction();
            realm.beginTransaction();

            RealmResults<Gallary> itemRealmResults = realm
                    .where(Gallary.class)
                    .equalTo("ownerId", sneeds_id)
                    .equalTo("type", "sp")
                    .findAll();

            images = new String[itemRealmResults.size()];
            labels = new String[itemRealmResults.size()];

            for (int i = 0; i < itemRealmResults.size(); i++) {
                if (itemRealmResults.get(i) != null) {
                    images[i] = itemRealmResults.get(i).getImage();
                    labels[i] = itemRealmResults.get(i).getLabel();

                }
            }
            realm.commitTransaction();
            realm.close();
        }
    }

    private void setSliderViews() {
        if (images.length != 0) {
            for (int i = 0; i < images.length; i++) {
                SliderView sliderView = new SliderView(this);

                switch (i) {
                    case 0:
                        sliderView.setImageUrl(images[0]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[0]);

                        break;
                    case 1:
                        sliderView.setImageUrl(images[1]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[1]);

                        break;
                    case 2:
                        sliderView.setImageUrl(images[2]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[2]);

                        break;
                    case 3:
                        sliderView.setImageUrl(images[3]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[3]);

                        break;
                    case 4:
                        sliderView.setImageUrl(images[4]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[4]);
                        break;
                    case 5:
                        sliderView.setImageUrl(images[5]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[5]);
                        break;
                    case 6:
                        sliderView.setImageUrl(images[6]);
                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        sliderView.setDescription(labels[6]);
                        break;
                }
                //at last add this view in your layout :
                sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(SliderView sliderView) {
                        Dialog dialog = new Dialog(SneedProfile.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                        dialog.setContentView(R.layout.slider_bigimage);
                        ImageView name = dialog.findViewById(R.id.logoinfo);
                        name.setImageURI(Uri.parse(sliderView.getImageUrl()));
                        dialog.show();
                    }
                });


                sliderLayout.addSliderView(sliderView);
            }
        }

    }
}
