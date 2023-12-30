package com.tarmiz.imentor.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
 import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.tarmiz.imentor.Adapters.CentersAdapter;
import com.tarmiz.imentor.Adapters.UniversityAdapter;
import com.tarmiz.imentor.Models.Center;
import com.tarmiz.imentor.Models.Slider;
import com.tarmiz.imentor.Models.University;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.Networking.Api;
import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class Universities extends Fragment implements SearchView.OnQueryTextListener {

    List<University> universityList = new ArrayList<>();
    UniversityAdapter universityAdapter;
    RecyclerView mRecyclerView;
    String[] images;
    String[] labels;
    SliderLayout sliderLayout;
    private Tracker mTracker;
    Toolbar toolbar;
    FontTypeface fontTypeface;
    Typeface typeface;
    public MyApplication application;
    Gson gson;

    public Universities() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getImages();
        application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();


        fontTypeface = new FontTypeface(getContext());
        typeface = fontTypeface.getTypefaceAndroid();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("الكليات و الجامعات");


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // requestData();


        View view = inflater.inflate(R.layout.fragment_universities, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SWAP); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        setSliderViews();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestUniversitiesData();

    }
    private void requestUniversitiesData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        if (isNetworkAvailable()) {

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest
                    (
                            Request.Method.GET,
                            Api.getAllUniversity(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    universityList = Arrays.asList(gson.fromJson(response, University[].class));
                                    universityAdapter = new UniversityAdapter(universityList, getActivity());
                                    mRecyclerView.setAdapter(universityAdapter);

                                    Realm realm = Realm.getDefaultInstance();

                                    for (int i = 0; i < universityList.size(); i++) {

                                        realm.beginTransaction();

                                        University university = new University();

                                        university.setId(universityList.get(i).getId());
                                        university.setName(universityList.get(i).getName());
                                        university.setAddress(universityList.get(i).getAddress());
                                        university.setLogo(universityList.get(i).getLogo());
                                        university.setDetails(universityList.get(i).getDetails());
                                        university.setPhone(universityList.get(i).getPhone());
                                        university.setPhone2(universityList.get(i).getPhone2());
                                        university.setLongitude(universityList.get(i).getLongitude());
                                        university.setLatitude(universityList.get(i).getLatitude());
                                        university.setStatus(universityList.get(i).getStatus());
                                        university.setCertified(universityList.get(i).getCertified());
                                        university.setSpecialties(universityList.get(i).getSpecialties());

                                        realm.copyToRealmOrUpdate(university);
                                        realm.commitTransaction();
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

        } else {
            universityList.clear();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<University> itemRealmResults = realm
                    .where(University.class)
                    .findAll();

            for (int i = 0; i < itemRealmResults.size(); i++) {
                if (itemRealmResults.get(i) != null) {
                    University center = new University();

                    center.setId(itemRealmResults.get(i).getId());
                    center.setName(itemRealmResults.get(i).getName());
                    center.setAddress(itemRealmResults.get(i).getAddress());
                    center.setLogo(itemRealmResults.get(i).getLogo());
                    center.setDetails(itemRealmResults.get(i).getDetails());
                    center.setPhone(itemRealmResults.get(i).getPhone());
                    center.setPhone2(itemRealmResults.get(i).getPhone2());
                    center.setLongitude(itemRealmResults.get(i).getLongitude());
                    center.setLatitude(itemRealmResults.get(i).getLatitude());
                    center.setStatus(itemRealmResults.get(i).getStatus());
                    center.setCertified(itemRealmResults.get(i).getCertified());

                    universityList.add(center);

                }
            }
            realm.close();

            universityAdapter = new UniversityAdapter(universityList, getActivity());
            mRecyclerView.setAdapter(universityAdapter);

        }


    }
    private void getImages() {
        Realm realm = Realm.getDefaultInstance();
        if (!realm.isInTransaction()) {
            realm.beginTransaction();

            RealmResults<Slider> itemRealmResults = realm
                    .where(Slider.class)
                    .equalTo("catId", 2)
                    .sort("id", Sort.DESCENDING)
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

            RealmResults<Slider> itemRealmResults = realm
                    .where(Slider.class)
                    .equalTo("catId", 2)
                    .sort("id", Sort.DESCENDING)
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        universityAdapter.getFilter().filter(query);
        return false;
    }

    private void setSliderViews() {
        if (images.length != 0) {
            for (int i = 0; i < images.length; i++) {
                SliderView sliderView = new SliderView(getActivity());

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
                sliderLayout.addSliderView(sliderView);
            }
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("واجهة الجامعات");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
