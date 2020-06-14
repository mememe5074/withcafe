package com.example.withcafe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class ShowReviewAdapter extends RecyclerView.Adapter<ShowReviewAdapter.ReViewHolder> {
    private ArrayList<ReviewInfo> mDataset;
    private Activity activity;
    private Context context;

    public class ReViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        ReViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

    }

    public ShowReviewAdapter(Activity activity, ArrayList<ReviewInfo> myDataset, String cafe_name, String district) {
        mDataset = myDataset;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ShowReviewAdapter.ReViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        final CardView cardView_review = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.reivew_item, parent, false);
        final ReViewHolder reViewHolder = new ReViewHolder(cardView_review);

        cardView_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return reViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReViewHolder holder, int position) {
        final SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.KOREA);
        simpleDate.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String getTime = simpleDate.format(mDataset.get(position).getCreatedAt());

        CardView cardView = holder.cardView;
        TextView title = cardView.findViewById(R.id.title);
        TextView star = cardView.findViewById(R.id.star);
        TextView user = cardView.findViewById(R.id.user);
        TextView date = cardView.findViewById(R.id.date);

        title.setText(mDataset.get(position).getTitle());
        star.setText(mDataset.get(position).getNumOfStar());
        user.setText(mDataset.get(position).getUser());
        date.setText(getTime);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
