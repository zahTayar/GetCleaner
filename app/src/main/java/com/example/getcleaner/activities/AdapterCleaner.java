package com.example.getcleaner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.RateAndComment;
import com.example.getcleaner.objects.userCleaner;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterCleaner extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<userCleaner> relevantCleaners = new ArrayList<>();
    private CleanerItemClickListner cleanerItemClickListner;
    private float sum;

    public AdapterCleaner(Activity activity, ArrayList<userCleaner> relevantCleaners) {
        this.activity = activity;
        this.relevantCleaners = relevantCleaners;
    }

    public AdapterCleaner setCleanerItemClickListner(CleanerItemClickListner cleanerItemClickListner) {
        this.cleanerItemClickListner=cleanerItemClickListner;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cleaners_list, viewGroup, false);
        return new CleanerViewHolder(view);
    }
//this is the finction that takes the data to each card
    //need to be fixed
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CleanerViewHolder cleanerViewHolder= (CleanerViewHolder) holder;
        userCleaner current=getItem(position);
        cleanerViewHolder.cleaner_LBL_name_and_last.setText(current.getFirstNameStr()+" "+current.getLastNameStr());
        cleanerViewHolder.cleaner_LBL_town.setText(current.getAddressStr());
        cleanerViewHolder.cleaner_LBL_actors.setText(current.getRelaventData());

        Helper.getMe().getAllComments(new Helper.callbackCommentsAndRates() {
            @Override
            public void dataReady(ArrayList<RateAndComment> arrayList) {
                for (RateAndComment comment: arrayList)
                {
                    sum+=(float) comment.getRate();
                }
                cleanerViewHolder.cleaner_RTNG_stars.setRating((float)sum/arrayList.size());
                sum=(float) 0.0;
            }
        },current.getEmailStr());
        if(current.isFavorite()){
            cleanerViewHolder.cleaner_IMG_favorite.setImageResource(R.drawable.ic_heart_filled);
        }else
            cleanerViewHolder.cleaner_IMG_favorite.setImageResource(R.drawable.ic_heart_empty);
        float rating = (float) 50.0511;
        cleanerViewHolder.cleaner_RTNG_stars.setRating(rating);
    }

    @Override
    public int getItemCount() {
        return relevantCleaners.size();
    }

    public userCleaner getItem(int position){return relevantCleaners.get(position);}

    public interface CleanerItemClickListner {
        void cleanerItemClicked(userCleaner cleaner, int position);
        void goToCommentsScreen(userCleaner cleaner);
        void favouriteClicked(userCleaner cleaner, int position);
    }

    public class CleanerViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView cleaner_IMG_image;
        public AppCompatImageView cleaner_IMG_favorite;
        public MaterialTextView cleaner_LBL_name_and_last;
        public MaterialTextView cleaner_LBL_actors;
        public MaterialTextView cleaner_LBL_town;
        public AppCompatRatingBar cleaner_RTNG_stars;
        public ImageView comments;

        public CleanerViewHolder(final View itemView) {
            super(itemView);
            this.cleaner_IMG_image = itemView.findViewById(R.id.cleaner_IMG_image);
            this.cleaner_IMG_favorite = itemView.findViewById(R.id.cleaner_IMG_favorite);
            this.cleaner_LBL_name_and_last = itemView.findViewById(R.id.cleaner_LBL_title);
            this.cleaner_LBL_actors = itemView.findViewById(R.id.cleaner_LBL_actors);
            this.cleaner_LBL_town = itemView.findViewById(R.id.cleaner_LBL_town);
            this.cleaner_RTNG_stars = itemView.findViewById(R.id.cleaner_RTNG_stars);
            this.comments=itemView.findViewById(R.id.cleaner_IMG_comments);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cleanerItemClickListner.cleanerItemClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

            cleaner_IMG_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cleanerItemClickListner.favouriteClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cleanerItemClickListner.goToCommentsScreen(getItem(getAdapterPosition()));
                }
            });

        }
    }
}