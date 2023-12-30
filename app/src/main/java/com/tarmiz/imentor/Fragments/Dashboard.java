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
import com.tarmiz.imentor.Adapters.AdvertAdapter;
import com.tarmiz.imentor.Adapters.CentersAdapter;
import com.tarmiz.imentor.Models.Advert;
import com.tarmiz.imentor.Models.Center;
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

public class Dashboard extends Fragment implements SearchView.OnQueryTextListener {
    List<Advert> advertList = new ArrayList<>();
    AdvertAdapter advertAdapter;
    RecyclerView mRecyclerView;
    Gson gson;
    private Tracker mTracker;
    private MyApplication application;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestAds();
    }

    private void requestAds() {

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
                            Api.getAllAdverts(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    advertList = Arrays.asList(gson.fromJson(response, Advert[].class));
                                    advertAdapter = new AdvertAdapter(advertList, getActivity());
                                    mRecyclerView.setAdapter(advertAdapter);

                                    Realm realm = Realm.getDefaultInstance();

                                    for (int i = 0; i < advertList.size(); i++) {

                                        realm.beginTransaction();

                                        Advert advert = new Advert();

                                        advert.setId(advertList.get(i).getId());
                                        advert.setTitle(advertList.get(i).getTitle());
                                        advert.setAvatar(advertList.get(i).getAddress());
                                        advert.setPhoto(advertList.get(i).getPhoto());
                                        advert.setDetails(advertList.get(i).getDetails());
                                        advert.setPhone(advertList.get(i).getPhone());
                                        advert.setDetails(advertList.get(i).getDetails());
                                        advert.setPhone2(advertList.get(i).getPhone2());
                                        advert.setAddress(advertList.get(i).getAddress());
                                        advert.setFacebook(advertList.get(i).getFacebook());
                                        advert.setReglink(advertList.get(i).getReglink());
                                        advert.setCreatedAt(advertList.get(i).getCreatedAt());

                                        realm.copyToRealmOrUpdate(advert);
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
            advertList.clear();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<Advert> itemRealmResults = realm
                    .where(Advert.class)
                    .findAll();

            for (int i = 0; i < itemRealmResults.size(); i++) {
                if (itemRealmResults.get(i) != null) {
                    Advert advert = new Advert();

                    advert.setId(itemRealmResults.get(i).getId());
                    advert.setTitle(itemRealmResults.get(i).getTitle());
                    advert.setAvatar(itemRealmResults.get(i).getAddress());
                    advert.setPhoto(itemRealmResults.get(i).getPhoto());
                    advert.setDetails(itemRealmResults.get(i).getDetails());
                    advert.setPhone(itemRealmResults.get(i).getPhone());
                    advert.setDetails(itemRealmResults.get(i).getDetails());
                    advert.setPhone2(itemRealmResults.get(i).getPhone2());
                    advert.setAddress(itemRealmResults.get(i).getAddress());
                    advert.setFacebook(itemRealmResults.get(i).getFacebook());
                    advert.setReglink(itemRealmResults.get(i).getReglink());
                    advert.setCreatedAt(itemRealmResults.get(i).getCreatedAt());

                    advertList.add(advert);

                }
            }
            realm.close();

            advertAdapter = new AdvertAdapter(advertList, getActivity());
            mRecyclerView.setAdapter(advertAdapter);

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
        advertAdapter.getFilter().filter(query);
        return false;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /*
    @Override
    public void onBackPressed() {
        advertAdapter.moveTaskToBack(true);
    }
    */
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("واجهة الاعلانات");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
