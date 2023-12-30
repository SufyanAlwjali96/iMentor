package com.tarmiz.imentor.Fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tarmiz.imentor.Adapters.ViewPagerAdapter;
import com.tarmiz.imentor.Fragments.Tabs.TAB5;
import com.tarmiz.imentor.Fragments.Tabs.TAB6;
import com.tarmiz.imentor.Fragments.Tabs.TAB7;
import com.tarmiz.imentor.Fragments.Tabs.TAB8;
import com.tarmiz.imentor.Models.Category;
import com.tarmiz.imentor.Models.School;
import com.tarmiz.imentor.Models.Tcategory;
import com.tarmiz.imentor.Models.Teacher;
import com.tarmiz.imentor.Networking.Api;
import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Teachers extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    Gson gson;

    public Teachers() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()) {
            requestTeachersData();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teachers, container, false);
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        setTabs();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("المعلمين");

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toast.makeText(getActivity(), "الرجاء التحقق من الاتصال  بالانترنت", Toast.LENGTH_LONG).show();

        }
        return true;
    }

    private void setTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new TAB5());
        adapter.addFrag(new TAB6());
        adapter.addFrag(new TAB7());
        adapter.addFrag(new TAB8());
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FontTypeface fontTypeface = new FontTypeface(getActivity());
        Typeface typeface = fontTypeface.getTypefaceAndroid();
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                    ((TextView) tabViewChild).setTextSize(10);
                }
            }
        }
    }

    private void requestTeachersData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest request = new StringRequest
                (
                        Request.Method.GET,
                        Api.getAllTeachers(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<Teacher> schools = Arrays.asList(gson.fromJson(response, Teacher[].class));

                                Realm realm = Realm.getDefaultInstance();

                                RealmResults<Teacher> schoolRealmResults = realm.where(Teacher.class).findAll();

                                if (schoolRealmResults != null) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(Teacher.class); // this will delete table completely
                                        }
                                    });

                                    for (int i = 0; i < schools.size(); i++) {

                                        realm.beginTransaction();

                                        Teacher school = new Teacher();

                                        school.setId(schools.get(i).getId());
                                        school.setName(schools.get(i).getName());
                                        school.setMaterials(schools.get(i).getMaterials());
                                        school.setPhoto(schools.get(i).getPhoto());
                                        school.setDetails(schools.get(i).getDetails());
                                        school.setPhone(schools.get(i).getPhone());
                                        school.setPhone2(schools.get(i).getPhone2());
                                        school.setAddress(schools.get(i).getAddress());

                                        school.setCertified(schools.get(i).getCertified());
                                        school.setCreatedAt(schools.get(i).getCreatedAt());


                                        for (int j = 0; j < schools.get(i).getTcategory().size(); j++) {
                                            Tcategory category = new Tcategory(schools.get(i).getTcategory().get(j).getId(), schools.get(i).getTcategory().get(j).getTcategory());


                                            if (!realm.isInTransaction()) {
                                                realm.beginTransaction();
                                                realm.copyToRealm(category);
                                                realm.commitTransaction();
                                            }

                                            school.getTcategory().add(category);
                                        }

                                        realm.copyToRealmOrUpdate(school);
                                        realm.commitTransaction();
                                    }
                                } else {
                                    for (int i = 0; i < schools.size(); i++) {

                                        realm.beginTransaction();

                                        Teacher school = new Teacher();

                                        school.setId(schools.get(i).getId());
                                        school.setName(schools.get(i).getName());
                                        school.setMaterials(schools.get(i).getMaterials());
                                        school.setPhoto(schools.get(i).getPhoto());
                                        school.setDetails(schools.get(i).getDetails());
                                        school.setPhone(schools.get(i).getPhone());
                                        school.setPhone2(schools.get(i).getPhone2());
                                        school.setAddress(schools.get(i).getAddress());

                                        school.setCertified(schools.get(i).getCertified());
                                        school.setCreatedAt(schools.get(i).getCreatedAt());


                                        for (int j = 0; j < schools.get(i).getTcategory().size(); j++) {
                                            Tcategory category = new Tcategory(schools.get(i).getTcategory().get(j).getId(), schools.get(i).getTcategory().get(j).getTcategory());

                                            if (!realm.isInTransaction()) {
                                                realm.beginTransaction();
                                                realm.copyToRealm(category);
                                                realm.commitTransaction();
                                            }

                                            school.getTcategory().add(category);
                                        }

                                        realm.copyToRealmOrUpdate(school);
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
