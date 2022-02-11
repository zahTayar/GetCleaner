package com.example.getcleaner.activities;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import com.example.getcleaner.R;
import com.example.getcleaner.objects.RateAndComment;
import com.example.getcleaner.objects.userCleaner;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<RateAndComment> relevantComments = new ArrayList<>();

    public AdapterComments(Activity activity, ArrayList<RateAndComment> relevantComments) {
        this.activity = activity;
        this.relevantComments = relevantComments;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_list, viewGroup, false);
        return new CommentViewHolder(view);
    }
    //this is the finction that takes the data to each card
    //need to be fixed
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder commentViewHolder= (CommentViewHolder) holder;
        RateAndComment current = getItem(position);
        String comment=current.getComment();
        if(comment.length()==0){
            comment = "No comment";
        }
        commentViewHolder.cleaner_LBL_comment.setText(comment);
        commentViewHolder.cleaner_LBL_byhow.setText(current.getByHow());
        commentViewHolder.cleaner_RTNG_stars.setRating(current.getRate());
    }

    @Override
    public int getItemCount() {
        return relevantComments.size();
    }

    public RateAndComment getItem(int position){return relevantComments.get(position);}

    public interface CleanerItemClickListner {
        void cleanerItemClicked(userCleaner cleaner, int position);

        void favouriteClicked(userCleaner cleaner, int position);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView cleaner_LBL_comment;
        public MaterialTextView cleaner_LBL_byhow;
        public AppCompatRatingBar cleaner_RTNG_stars;

        public CommentViewHolder(final View itemView) {
            super(itemView);
            this.cleaner_LBL_comment = itemView.findViewById(R.id.cleaner_LBL_comment);
            this.cleaner_LBL_byhow = itemView.findViewById(R.id.cleaner_LBL_byhow);
            this.cleaner_RTNG_stars = itemView.findViewById(R.id.cleaner_RTNG_stars);

        }
    }
}
