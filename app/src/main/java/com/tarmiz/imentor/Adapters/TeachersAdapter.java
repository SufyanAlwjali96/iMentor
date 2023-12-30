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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tarmiz.imentor.Models.Teacher;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.Profiles.TeachersProfile;
import com.tarmiz.imentor.R;

import java.util.List;


public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.FeedViewHolder> implements Filterable {

    List<Teacher> teachersList;
    Context context;
    TeachersCustomFilter filter;
    List<Teacher> filteredTeachers;
    private Tracker mTracker;
    private MyApplication application;

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TeachersCustomFilter(filteredTeachers, this);
        }
        return filter;
    }


    class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView name, materials;
        SimpleDraweeView logo;
        LinearLayout teachers_cv;
        ImageView certified;


        FeedViewHolder(View itemView) {
            super(itemView);
            teachers_cv = itemView.findViewById(R.id.teachers_cv);
            name = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.image);
            materials = itemView.findViewById(R.id.materials);
            certified = itemView.findViewById(R.id.certified);

            application = (MyApplication) context.getApplicationContext();
            mTracker = application.getDefaultTracker();
        }
    }

    public TeachersAdapter(List<Teacher> TeachersList, Context context) {
        this.teachersList = TeachersList;
        this.filteredTeachers = teachersList;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TeachersAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TeachersAdapter.FeedViewHolder feedViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_view, parent, false);
        feedViewHolder = new TeachersAdapter.FeedViewHolder(view);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TeachersAdapter.FeedViewHolder holder, final int position) {

        holder.name.setText(teachersList.get(position).getName());
        holder.materials.setText(teachersList.get(position).getMaterials());
        Uri logo = Uri.parse(teachersList.get(position).getPhoto());
        holder.logo.setImageURI(logo);

        holder.teachers_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("صفحة المعلم")
                        .setAction(teachersList.get(position).getName())
                        .build());
                Intent intent = new Intent(context, TeachersProfile.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("teacher_id", teachersList.get(position).getId());

                context.startActivity(intent);
            }
        });

        if (teachersList.get(position).getCertified() == 1) {
            holder.certified.setVisibility(View.VISIBLE);
        } else {
            holder.certified.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return teachersList.size();
    }
}