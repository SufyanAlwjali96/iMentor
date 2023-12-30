package com.tarmiz.imentor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tarmiz.imentor.Models.Center;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.Profiles.CenterProfile;
import com.tarmiz.imentor.R;

import java.util.List;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.FeedViewHolder> implements Filterable {

    List<Center> centers;
    Context context;
    CentersCustomFilter filter;
    List<Center> filteredCenters;
    private Tracker mTracker;
    private MyApplication application;

    @Override
    public Filter getFilter()
    {
        if(filter==null)
        {
            filter = new CentersCustomFilter(filteredCenters,this);
        }
        return filter;
    }

    class FeedViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, address;
        SimpleDraweeView logo;
        ImageView certified;
        RelativeLayout center_cv;

        FeedViewHolder(View itemView) {
            super(itemView);
            center_cv = itemView.findViewById(R.id.cv);
            name = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.logo);
            address = itemView.findViewById(R.id.address);
            certified = itemView.findViewById(R.id.certified);
            application = (MyApplication) context.getApplicationContext();
            mTracker = application.getDefaultTracker();
        }
    }

    public CentersAdapter(List<Center> centers, Context context) {
        this.centers = centers;
        this.context = context;

        this.filteredCenters = centers;

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FeedViewHolder feedViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_view, parent, false);
        feedViewHolder = new FeedViewHolder(view);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder holder, final int position) {

        holder.name.setText(centers.get(position).getName());
        holder.address.setText(centers.get(position).getAddress());

        Uri logo = Uri.parse(centers.get(position).getLogo());
        holder.logo.setImageURI(logo);

        holder.center_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("صفحة مركز")
                        .setAction(centers.get(position).getName())
                        .build());
                Intent intent = new Intent(context, CenterProfile.class);
                intent.setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("center_id", centers.get(position).getId());
                context.startActivity(intent);
            }
        });

        if (centers.get(position).getCertified()==1)
        {
            holder.certified.setVisibility(View.VISIBLE);
        }
        else {
            holder.certified.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

}