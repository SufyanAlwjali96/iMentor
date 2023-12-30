package com.tarmiz.imentor.Adapters;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tarmiz.imentor.Models.Advert;
import com.tarmiz.imentor.MyApplication;
import com.tarmiz.imentor.R;


import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;

import static android.view.View.GONE;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.FeedViewHolder> implements Filterable {

    List<Advert> adverts;
    Context context;
    AdvertsCustomFilter filter;
    List<Advert> filteredAdvertsList;
    private Tracker mTracker;
    private MyApplication application;

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new AdvertsCustomFilter(filteredAdvertsList, this);
        }
        return filter;
    }


    class FeedViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        TextView title, createdAt, register_link, details;
        SimpleDraweeView avatar, photo;
        ImageView share;
        ImageButton call_button, facebook_button, place_button;

        FeedViewHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.studentItem);
            title = itemView.findViewById(R.id.title);
            avatar = itemView.findViewById(R.id.avatar);
            photo = itemView.findViewById(R.id.photo);
            createdAt = itemView.findViewById(R.id.createdAt);
            details = itemView.findViewById(R.id.details);
            call_button = itemView.findViewById(R.id.call_button);
            facebook_button = itemView.findViewById(R.id.facebook_button);
            place_button = itemView.findViewById(R.id.place_button);
            register_link = itemView.findViewById(R.id.register_link);
            share = itemView.findViewById(R.id.share);
            //////////////////////
            application = (MyApplication) context.getApplicationContext();
            mTracker = application.getDefaultTracker();
        }
    }

    public AdvertAdapter(List<Advert> adverts, Context context) {
        this.adverts = adverts;
        this.context = context;
        this.filteredAdvertsList = adverts;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FeedViewHolder feedViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advert_item, parent, false);
        feedViewHolder = new FeedViewHolder(view);

        return feedViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder holder, final int position) {
        mTracker.setScreenName(adverts.get(position).getTitle());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        holder.title.setText(adverts.get(position).getTitle());
        holder.details.setText(adverts.get(position).getDetails());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String sAux = "";

                sAux = sAux + "https://play.google.com/store/apps/details?id=com.tarmiz.imentor";

                sendIntent.putExtra(Intent.EXTRA_TEXT, adverts.get(position).getDetails() + '\n'  + '\n'  + '\n' + "  رقم الهاتف : " + adverts.get(position).getPhone() +'\n'+" العنوان : "+ adverts.get(position).getAddress() +'\n'+'\n' +"مزيد من التفاصيل داخل التطبيق :  "

                        +Html.fromHtml(sAux));
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);

            }
        });
        holder.createdAt.setText(adverts.get(position).getCreatedAt());


        // Checking for null feed url
        if (adverts.get(position).getReglink() != null) {
            if (adverts.get(position).getReglink().length() > 5) {
                holder.register_link.setText(Html.fromHtml("<a href=\"" + adverts.get(position).getReglink() + "\" >"
                        + "اضغط هنا</a> "));
            }

            // Making url clickable
            holder.register_link.setMovementMethod(LinkMovementMethod.getInstance());
            holder.register_link.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.register_link.setVisibility(GONE);
        }

        if (adverts.get(position).getPhoto() != null) {
            Uri photo = Uri.parse(adverts.get(position).getPhoto());
            holder.photo.setImageURI(photo);
        } else {
            holder.photo.setVisibility(View.GONE);
        }

        Uri avatar = Uri.parse(adverts.get(position).getAvatar());
        holder.avatar.setImageURI(avatar);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.avatar_info);
                ImageView name = dialog.findViewById(R.id.logoinfo);
                name.setImageURI(Uri.parse(adverts.get(position).getAvatar()));
                dialog.show();

            }

        });
///////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
        holder.call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.phone_info);
                TextView name = dialog.findViewById(R.id.name);
                name.setText(adverts.get(position).getTitle());

                TextView phone = dialog.findViewById(R.id.phone);
                TextView phone2 = dialog.findViewById(R.id.phone2);

                phone.setText(adverts.get(position).getPhone());
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNo = adverts.get(position).getPhone();
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);
                    }
                });
                dialog.show();

                phone2.setText(adverts.get(position).getPhone2());
                phone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNo = adverts.get(position).getPhone2();
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        holder.facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add facebook pages ID in database
                try {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adverts.get(position).getFacebook())); //if fb app exist
                    context.startActivity(intent);

                } catch (PackageManager.NameNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adverts.get(position).getFaceUrl()));// if fb app does not exist
                    context.startActivity(intent);
                }
            }
        });

        holder.place_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PromptDialog(context)
                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)

                        .setTitleText(adverts.get(position).getTitle())
                        .setContentText(adverts.get(position).getAddress())
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    public Context getContext() {
        return context;
    }
}