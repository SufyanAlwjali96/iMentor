package com.tarmiz.imentor.Fragments.Tabs;



import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tarmiz.imentor.Adapters.TeachersAdapter;
import com.tarmiz.imentor.Models.Teacher;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TAB8 extends Fragment implements SearchView.OnQueryTextListener{

    List<Teacher> teachersList = new ArrayList<>();
    TeachersAdapter teachersAdapter;
    RecyclerView mRecyclerView;
    private Tracker mTracker;
    public MyApplication application;

    public TAB8() {}

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teachersAdapter = new TeachersAdapter(teachersList, getActivity());
        mRecyclerView.setAdapter(teachersAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("فئة جامعي")
                .setAction("البحث عن مدرس جامعي")
                .build());
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        teachersAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
    }

    private void requestData()
    {
        teachersList.clear();
        Realm realm = Realm.getDefaultInstance();
        if (!realm.isInTransaction()){
            realm.beginTransaction();

            RealmResults<Teacher> itemRealmResults = realm
                    .where(Teacher.class)
                    .equalTo("category.cat", "جامعي")
                    .findAll();

            for (int i=0; i<itemRealmResults.size();i++)
            {
                if (itemRealmResults.get(i) != null)
                {
                    Teacher teacher = new Teacher();
                    teacher.setId(itemRealmResults.get(i).getId());
                    teacher.setName(itemRealmResults.get(i).getName());
                    teacher.setMaterials(itemRealmResults.get(i).getMaterials());
                    teacher.setPhoto(itemRealmResults.get(i).getPhoto());
                    teacher.setDetails(itemRealmResults.get(i).getDetails());
                    teacher.setAddress(itemRealmResults.get(i).getAddress());

                    teacher.setCertified(itemRealmResults.get(i).getCertified());

                    teachersList.add(teacher);
                }
            }
            realm.commitTransaction();
            realm.close();
        }else {
            realm.commitTransaction();
            realm.beginTransaction();

            RealmResults<Teacher> itemRealmResults = realm
                    .where(Teacher.class)
                    .equalTo("category.cat", "جامعي")
                    .findAll();

            for (int i=0; i<itemRealmResults.size();i++)
            {
                if (itemRealmResults.get(i) != null)
                {
                    Teacher teacher = new Teacher();
                    teacher.setId(itemRealmResults.get(i).getId());
                    teacher.setName(itemRealmResults.get(i).getName());
                    teacher.setMaterials(itemRealmResults.get(i).getMaterials());
                    teacher.setPhoto(itemRealmResults.get(i).getPhoto());
                    teacher.setDetails(itemRealmResults.get(i).getDetails());
                    teacher.setAddress(itemRealmResults.get(i).getAddress());

                    teacher.setCertified(itemRealmResults.get(i).getCertified());

                    teachersList.add(teacher);
                }
            }
            realm.commitTransaction();
            realm.close(); }
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("معلمين جامعة");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
