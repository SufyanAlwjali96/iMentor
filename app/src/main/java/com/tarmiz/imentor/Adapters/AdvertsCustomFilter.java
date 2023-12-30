package com.tarmiz.imentor.Adapters;

import android.widget.Filter;


import com.tarmiz.imentor.Models.Advert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murad on 03/01/2017.
 */
public class AdvertsCustomFilter extends Filter {

    public AdvertAdapter adapter;
    public List<Advert> filterList;

    public AdvertsCustomFilter(List<Advert> filterList, AdvertAdapter adapter)
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

            ArrayList<Advert> filteredAdverts = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getDetails().toUpperCase().contains(constraint)|| filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    filteredAdverts.add(filterList.get(i));
                }
            }
            results.count = filteredAdverts.size();
            results.values = filteredAdverts;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.adverts= (List<Advert>) filterResults.values;

        //REFRESH

        adapter.notifyDataSetChanged();
    }
}
