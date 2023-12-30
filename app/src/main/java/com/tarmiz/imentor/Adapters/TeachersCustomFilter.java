package com.tarmiz.imentor.Adapters;

import android.widget.Filter;

import com.tarmiz.imentor.Models.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeachersCustomFilter extends Filter {

    TeachersAdapter adapter;
    List<Teacher> filterList;

    public TeachersCustomFilter(List<Teacher> filterList, TeachersAdapter adapter)
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

            ArrayList<Teacher> filteredTeachers = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getName().toUpperCase().contains(constraint)||filterList.get(i).getMaterials().toUpperCase().contains(constraint))
                {
                    filteredTeachers.add(filterList.get(i));
                }
            }
            results.count = filteredTeachers.size();
            results.values = filteredTeachers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.teachersList= (List<Teacher>) filterResults.values;


        adapter.notifyDataSetChanged();
    }
}
