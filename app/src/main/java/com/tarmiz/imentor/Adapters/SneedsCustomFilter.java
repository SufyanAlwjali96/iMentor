package com.tarmiz.imentor.Adapters;

import android.widget.Filter;

import com.tarmiz.imentor.Models.Sneeds;

import java.util.ArrayList;
import java.util.List;

public class SneedsCustomFilter  extends Filter {

    SneedAdapter adapter;
    List<Sneeds> filterList;

    public SneedsCustomFilter(List<Sneeds> filterList, SneedAdapter adapter)
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

            ArrayList<Sneeds> filteredsncenter = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getName().toUpperCase().contains(constraint)|| filterList.get(i).getAddress().toUpperCase().contains(constraint))
                {
                    filteredsncenter.add(filterList.get(i));
                }
            }
            results.count = filteredsncenter.size();
            results.values = filteredsncenter;
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
        adapter.sneeds= (List<Sneeds>) filterResults.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
