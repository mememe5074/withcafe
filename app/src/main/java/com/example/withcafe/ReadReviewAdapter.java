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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ReadReviewAdapter extends RecyclerView.Adapter<ReadReviewAdapter.ReplyViewHolder> {
    private ArrayList<ReplyInfo> mDataset;
    private Activity activity;
    private Context context;

    public class ReplyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;

        ReplyViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public ReadReviewAdapter(Activity activity, ArrayList<ReplyInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReadReviewAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        final CardView cardView_review = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);
        final ReplyViewHolder replyViewHolder = new ReplyViewHolder(cardView_review);

        cardView_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return replyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {

        final SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.KOREA);
        simpleDate.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String getTime = simpleDate.format(mDataset.get(position).getCreatedAt());

        CardView cardView = holder.cardView;
        TextView reply_contents = cardView.findViewById(R.id.reply_contents);
        TextView user = cardView.findViewById(R.id.user);
        TextView date = cardView.findViewById(R.id.date);

        user.setText(mDataset.get(position).getUser());
        reply_contents.setText(mDataset.get(position).getReply_contents());
        date.setText(getTime);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
