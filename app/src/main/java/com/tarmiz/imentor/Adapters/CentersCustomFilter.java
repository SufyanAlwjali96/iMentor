package com.tarmiz.imentor.Adapters;

import android.widget.Filter;

import com.tarmiz.imentor.Models.Center;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murad on 03/01/2017.
 */
public class CentersCustomFilter extends Filter {

    CentersAdapter adapter;
    List<Center> filterList;

    public CentersCustomFilter(List<Center> filterList, CentersAdapter adapter)
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

            ArrayList<Center> filteredCenters = new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getName().toUpperCase().contains(constraint)|| filterList.get(i).getAddress().toUpperCase().contains(constraint)||filterList.get(i).getDetails().toUpperCase().contains(constraint))
                {
                    filteredCenters.add(filterList.get(i));
                }
            }
            results.count = filteredCenters.size();
            results.values = filteredCenters;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.centers= (List<Center>) filterResults.values;

        //REFRESH

        adapter.notifyDataSetChanged();
    }
}
