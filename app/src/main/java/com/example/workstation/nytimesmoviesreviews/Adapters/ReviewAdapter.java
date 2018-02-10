package com.example.workstation.nytimesmoviesreviews.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.workstation.nytimesmoviesreviews.Model.Review;
import com.example.workstation.nytimesmoviesreviews.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Review> reviews = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReviewHolder) holder).bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void add(List<Review> reviews) {
        for (Review review : reviews) {
            if (review!=null && !this.reviews.contains(review)){
                this.reviews.add(review);
                notifyItemInserted(getItemCount());
            }
        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_picture)
        ImageView ivPicture;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.btn_more)
        Button btnMore;

        private RequestOptions options;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder);
        }

        public void bind(Review review) {
            Glide.with(itemView.getContext()).load(review.getMultimedia().getSrc()).apply(options).into(ivPicture);
            btnMore.setOnClickListener((view) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(review.getLink().getUrl()));
                Intent chooser = Intent.createChooser(intent, itemView.getContext().getResources().getText(R.string.complete_action).toString());
                itemView.getContext().startActivity(chooser);
            });
            tvTitle.setText(review.getDisplayTitle());
            tvDescription.setText(review.getSummaryShort());
            tvDate.setText(review.getDateUpdated());
        }
    }
}
