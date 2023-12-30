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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.tarmiz.imentor.Adapters.ViewPagerAdapter;
import com.tarmiz.imentor.Fragments.Tabs.TAB1;
import com.tarmiz.imentor.Fragments.Tabs.TAB2;
import com.tarmiz.imentor.Fragments.Tabs.TAB3;
import com.tarmiz.imentor.Fragments.Tabs.TAB4;
import com.tarmiz.imentor.Models.Category;
import com.tarmiz.imentor.Models.School;
import com.tarmiz.imentor.Models.Slider;
import com.tarmiz.imentor.Networking.Api;
import com.tarmiz.imentor.R;
import com.tarmiz.imentor.Utils.FontTypeface;

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

public class Schools extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    String[] images;
    String[] labels;
    SliderLayout sliderLayout;
    Gson gson;

    public Schools() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()) {
            getImages();
            requestSchoolsData();
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schools, container, false);
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        setTabs();

        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SWAP); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        setSliderViews();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new TAB1());
        adapter.addFrag(new TAB2());
        adapter.addFrag(new TAB3());
        adapter.addFrag(new TAB4());
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toast.makeText(getActivity(), "الرجاء التحقق من الاتصال  بالانترنت", Toast.LENGTH_LONG).show();

        }
        return true;
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

    private void getImages() {

        Realm realm = Realm.getDefaultInstance();
        if (!realm.isInTransaction()) {
            realm.beginTransaction();

            RealmResults<Slider> itemRealmResults = realm
                    .where(Slider.class)
                    .equalTo("catId", 1)
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
                    .equalTo("catId", 1)
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

    private void requestSchoolsData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest request = new StringRequest
                (
                        Request.Method.GET,
                        Api.getAllSchools(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<School> schools = Arrays.asList(gson.fromJson(response, School[].class));

                                Realm realm = Realm.getDefaultInstance();

                                RealmResults<School> schoolRealmResults = realm.where(School.class).findAll();

                                if (schoolRealmResults != null)
                                {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(School.class); // this will delete table completely
                                        }
                                    });

                                    for (int i = 0; i < schools.size(); i++) {

                                        realm.beginTransaction();

                                        School school = new School();

                                        school.setId(schools.get(i).getId());
                                        school.setName(schools.get(i).getName());
                                        school.setAddress(schools.get(i).getAddress());
                                        school.setLogo(schools.get(i).getLogo());
                                        school.setDetails(schools.get(i).getDetails());
                                        school.setPhone(schools.get(i).getPhone());
                                        school.setPhone2(schools.get(i).getPhone2());
                                        school.setLongitude(schools.get(i).getLongitude());
                                        school.setLatitude(schools.get(i).getLatitude());
                                        school.setStatus(schools.get(i).getStatus());
                                        school.setCertified(schools.get(i).getCertified());
                                        school.setCreatedAt(schools.get(i).getCreatedAt());

                                        for (int j = 0; j < schools.get(i).getCategory().size(); j++) {
                                            Category category = new Category(schools.get(i).getCategory().get(j).getId(), schools.get(i).getCategory().get(j).getCategory());

                                            if (!realm.isInTransaction()) {
                                                realm.beginTransaction();
                                                realm.copyToRealm(category);
                                                realm.commitTransaction();
                                            }

                                            school.getCategory().add(category);
                                        }

                                        realm.copyToRealmOrUpdate(school);
                                        realm.commitTransaction();
                                    }
                                } else {
                                    for (int i = 0; i < schools.size(); i++) {

                                        realm.beginTransaction();

                                        School school = new School();

                                        school.setId(schools.get(i).getId());
                                        school.setName(schools.get(i).getName());
                                        school.setAddress(schools.get(i).getAddress());
                                        school.setLogo(schools.get(i).getLogo());
                                        school.setDetails(schools.get(i).getDetails());
                                        school.setPhone(schools.get(i).getPhone());
                                        school.setPhone2(schools.get(i).getPhone2());
                                        school.setLongitude(schools.get(i).getLongitude());
                                        school.setLatitude(schools.get(i).getLatitude());
                                        school.setStatus(schools.get(i).getStatus());
                                        school.setCertified(schools.get(i).getCertified());
                                        school.setCreatedAt(schools.get(i).getCreatedAt());

                                        for (int j = 0; j < schools.get(i).getCategory().size(); j++) {
                                            Category category = new Category(schools.get(i).getCategory().get(j).getId(), schools.get(i).getCategory().get(j).getCategory());

                                            if (!realm.isInTransaction()) {
                                                realm.beginTransaction();
                                                realm.copyToRealm(category);
                                                realm.commitTransaction();
                                            }

                                            school.getCategory().add(category);
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
                        }){
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

