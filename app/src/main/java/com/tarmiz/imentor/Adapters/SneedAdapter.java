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
import com.tarmiz.imentor.Models.Sneeds;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.Profiles.SneedProfile;
import com.tarmiz.imentor.R;

import java.util.List;

public class SneedAdapter extends RecyclerView.Adapter<SneedAdapter.FeedViewHolder> implements Filterable {

    List<Sneeds> sneeds;
    Context context;
    SneedsCustomFilter filter;
    List<Sneeds> filteredSneeds;
    private Tracker mTracker;
    private MyApplication application;

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SneedsCustomFilter(filteredSneeds, this);
        }
        return filter;
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        SimpleDraweeView logo;
        ImageView certified;
        RelativeLayout sneeds_cv;

        FeedViewHolder(View itemView) {
            super(itemView);
            sneeds_cv = itemView.findViewById(R.id.cv);
            name = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.logo);
            address = itemView.findViewById(R.id.address);
            certified = itemView.findViewById(R.id.certified);
            application = (MyApplication) context.getApplicationContext();
            mTracker = application.getDefaultTracker();
        }
    }

    public SneedAdapter(List<Sneeds> sneeds, Context context) {
        this.sneeds = sneeds;
        this.filteredSneeds = sneeds;

        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public SneedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SneedAdapter.FeedViewHolder feedViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_view, parent, false);
        feedViewHolder = new SneedAdapter.FeedViewHolder(view);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SneedAdapter.FeedViewHolder holder, final int position) {

        holder.name.setText(sneeds.get(position).getName());
        holder.address.setText(sneeds.get(position).getAddress());

        Uri logo = Uri.parse(sneeds.get(position).getLogo());
        holder.logo.setImageURI(logo);

        holder.sneeds_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("صفحة مركز(ذوي الاحتياجات)")
                        .setAction(sneeds.get(position).getName())
                        .build());
                Intent intent = new Intent(context, SneedProfile.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("sneeds_id", sneeds.get(position).getId());
                context.startActivity(intent);
            }
        });

        if (sneeds.get(position).getCertified() == 1) {
            holder.certified.setVisibility(View.VISIBLE);
        } else {
            holder.certified.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return sneeds.size();
    }

}