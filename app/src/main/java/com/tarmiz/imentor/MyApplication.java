package com.tarmiz.imentor;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tarmiz.imentor.Models.Gallary;
import com.tarmiz.imentor.Models.Slider;
import com.tarmiz.imentor.Networking.Api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyApplication extends Application {
    Gson gson;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);

        Fresco.initialize(this);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("imentor.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        AndroidNetworking.initialize(this);

        //requestSchoolsData();
        // requestTeachersData();
        requestSliderImagesData();
        requestGallaryImages();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }

        return sTracker;
    }

    private void requestGallaryImages() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest
                (
                        Request.Method.GET,
                        Api.getAllImages(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                List<Gallary> gallaries = Arrays.asList(gson.fromJson(response, Gallary[].class));

                                Realm realm = Realm.getDefaultInstance();
                                RealmResults<Gallary> schoolRealmResults = realm.where(Gallary.class).findAll();
                                if (schoolRealmResults != null) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(Gallary.class); // this will delete table completely
                                        }
                                    });


                                    for (int i = 0; i < gallaries.size(); i++) {

                                        realm.beginTransaction();

                                        Gallary gallary = new Gallary();

                                        gallary.setId(gallaries.get(i).getId());
                                        gallary.setOwnerId(gallaries.get(i).getOwnerId());
                                        gallary.setLabel(gallaries.get(i).getLabel());
                                        gallary.setType(gallaries.get(i).getType());
                                        gallary.setImage(gallaries.get(i).getImage());

                                        realm.copyToRealmOrUpdate(gallary);
                                        realm.commitTransaction();
                                    }
                                } else {
                                    for (int i = 0; i < gallaries.size(); i++) {

                                        realm.beginTransaction();

                                        Gallary gallary = new Gallary();

                                        gallary.setId(gallaries.get(i).getId());
                                        gallary.setOwnerId(gallaries.get(i).getOwnerId());
                                        gallary.setLabel(gallaries.get(i).getLabel());
                                        gallary.setType(gallaries.get(i).getType());
                                        gallary.setImage(gallaries.get(i).getImage());

                                        realm.copyToRealmOrUpdate(gallary);
                                        realm.commitTransaction();
                                    }

                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error:", error.toString());
                            }
                        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json");
                headers.put("mentor", "$2y$10$4cItIiRowmDFhpDHTXwatukZxL9WqWAyl5I1jeQD4TxyWlx0jYx6G");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


    private void requestSliderImagesData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Api.getSliderImages(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Slider> sliderList = Arrays.asList(gson.fromJson(response, Slider[].class));

                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<Slider> schoolRealmResults = realm.where(Slider.class).findAll();
                        if (schoolRealmResults != null) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Slider.class); // this will delete table completely
                                }
                            });
                            for (int i = 0; i < sliderList.size(); i++) {
                                realm.beginTransaction();

                                Slider slider = new Slider();
                                slider.setId(sliderList.get(i).getId());
                                slider.setCatId(sliderList.get(i).getCatId());
                                slider.setImage(sliderList.get(i).getImage());
                                slider.setLabel(sliderList.get(i).getLabel());

                                realm.copyToRealmOrUpdate(slider);
                                realm.commitTransaction();
                            }
                        } else {
                            for (int i = 0; i < sliderList.size(); i++) {
                                realm.beginTransaction();

                                Slider slider = new Slider();
                                slider.setId(sliderList.get(i).getId());
                                slider.setCatId(sliderList.get(i).getCatId());
                                slider.setImage(sliderList.get(i).getImage());
                                slider.setLabel(sliderList.get(i).getLabel());

                                realm.copyToRealmOrUpdate(slider);
                                realm.commitTransaction();
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error:", error.toString());
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json");
                headers.put("mentor", "$2y$10$4cItIiRowmDFhpDHTXwatukZxL9WqWAyl5I1jeQD4TxyWlx0jYx6G");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


}