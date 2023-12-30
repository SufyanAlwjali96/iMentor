package com.tarmiz.imentor.Adapters;

import android.widget.Filter;

import com.tarmiz.imentor.Models.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolsCustomFilter extends Filter {

    SchoolsAdapter adapter;
    List<School> filterList;

    public SchoolsCustomFilter(List<School> filterList, SchoolsAdapter adapter)
    {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();

            ArrayList<School> filteredSchools = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getName().toUpperCase().contains(constraint)|| filterList.get(i).getAddress().toUpperCase().contains(constraint))
                {
                    filteredSchools.add(filterList.get(i));
                }
            }
            results.count = filteredSchools.size();
            results.values = filteredSchools;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.schoolsList= (List<School>) filterResults.values;


        adapter.notifyDataSetChanged();
    }
}
