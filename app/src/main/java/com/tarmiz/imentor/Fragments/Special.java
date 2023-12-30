package com.tarmiz.imentor.Fragments;


import android.app.SearchManager;
import android.content.Context;
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
import android.widget.Toast;

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
import com.tarmiz.imentor.Adapters.CentersAdapter;
import com.tarmiz.imentor.Adapters.SneedAdapter;
import com.tarmiz.imentor.Adapters.UniversityAdapter;
import com.tarmiz.imentor.Models.Center;
import com.tarmiz.imentor.Models.Sneeds;
import com.tarmiz.imentor.Models.University;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.Networking.Api;
import com.tarmiz.imentor.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Special extends Fragment implements SearchView.OnQueryTextListener {

    List<Sneeds> spneedsList = new ArrayList<>();
    SneedAdapter sneedsAdapter;
    RecyclerView mRecyclerView;
    private Tracker mTracker;
    public MyApplication application;
    Gson gson;

    public Special() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
     }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //requestData();


        View view = inflater.inflate(R.layout.fragment_special, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("مراكز ذوي الاحتياجان الخاصة");
        requestSNeedsData();

    }
    private void requestSNeedsData() {

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
                            Api.getAllSneeds(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    spneedsList = Arrays.asList(gson.fromJson(response, Sneeds[].class));
                                    sneedsAdapter = new SneedAdapter(spneedsList, getActivity());
                                    mRecyclerView.setAdapter(sneedsAdapter);

                                    Realm realm = Realm.getDefaultInstance();

                                    for (int i = 0; i < spneedsList.size(); i++) {

                                        realm.beginTransaction();

                                        Sneeds sneeds = new Sneeds();

                                        sneeds.setId(spneedsList.get(i).getId());
                                        sneeds.setName(spneedsList.get(i).getName());
                                        sneeds.setAddress(spneedsList.get(i).getAddress());
                                        sneeds.setLogo(spneedsList.get(i).getLogo());
                                        sneeds.setDetails(spneedsList.get(i).getDetails());
                                        sneeds.setPhone(spneedsList.get(i).getPhone());
                                        sneeds.setPhone2(spneedsList.get(i).getPhone2());
                                        sneeds.setLongitude(spneedsList.get(i).getLongitude());
                                        sneeds.setLatitude(spneedsList.get(i).getLatitude());
                                        sneeds.setStatus(spneedsList.get(i).getStatus());
                                        sneeds.setCertified(spneedsList.get(i).getCertified());

                                        realm.copyToRealmOrUpdate(sneeds);
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
            spneedsList.clear();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<Sneeds> itemRealmResults = realm
                    .where(Sneeds.class)
                    .findAll();

            for (int i = 0; i < itemRealmResults.size(); i++) {
                if (itemRealmResults.get(i) != null) {
                    Sneeds sneeds = new Sneeds();

                    sneeds.setId(itemRealmResults.get(i).getId());
                    sneeds.setName(itemRealmResults.get(i).getName());
                    sneeds.setAddress(itemRealmResults.get(i).getAddress());
                    sneeds.setLogo(itemRealmResults.get(i).getLogo());
                    sneeds.setDetails(itemRealmResults.get(i).getDetails());
                    sneeds.setPhone(itemRealmResults.get(i).getPhone());
                    sneeds.setPhone2(itemRealmResults.get(i).getPhone2());
                    sneeds.setLongitude(itemRealmResults.get(i).getLongitude());
                    sneeds.setLatitude(itemRealmResults.get(i).getLatitude());
                    sneeds.setStatus(itemRealmResults.get(i).getStatus());
                    sneeds.setCertified(itemRealmResults.get(i).getCertified());

                    spneedsList.add(sneeds);

                }
            }
            realm.close();

            sneedsAdapter = new SneedAdapter(spneedsList, getActivity());
            mRecyclerView.setAdapter(sneedsAdapter);

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
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        sneedsAdapter.getFilter().filter(query);
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("واجهة مراكز ذوي الاحتياجات");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

        }

