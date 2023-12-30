package com.tarmiz.imentor.Adapters;

import android.widget.Filter;

import com.tarmiz.imentor.Models.University;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murad on 03/01/2017.
 */
public class UniversityCustomFilter extends Filter {

    UniversityAdapter adapter;
    List<University> filterList;

    public UniversityCustomFilter(List<University> filterList, UniversityAdapter adapter)
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

            ArrayList<University> filteredUniversities = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getName().toUpperCase().contains(constraint)|| filterList.get(i).getAddress().toUpperCase().contains(constraint)||filterList.get(i).getDetails().toUpperCase().contains(constraint));
                {
                    filteredUniversities.add(filterList.get(i));
                }
            }
            results.count = filteredUniversities.size();
            results.values = filteredUniversities;
        }
        else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.universities= (List<University>) filterResults.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
