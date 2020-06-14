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

import java.util.ArrayList;

public class SelectCafeAdapter extends RecyclerView.Adapter<SelectCafeAdapter.SelectCafeViewHolder> {
    private Context context;
    private ArrayList<CafeInfo> mDataset;
    private Activity activity;

    public class SelectCafeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;


        SelectCafeViewHolder(CardView v){
            super(v);
            cardView = v;
        }

    }

    public SelectCafeAdapter(Activity activity, ArrayList<CafeInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SelectCafeAdapter.SelectCafeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        final CardView cardView_review = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cafe_item_post, parent, false);
        final SelectCafeViewHolder selectCafeViewHolder = new SelectCafeViewHolder(cardView_review);

        cardView_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return selectCafeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectCafeViewHolder holder, int position) {

        ArrayList<String> univ_list = mDataset.get(position).getNear_Univ();
        ArrayList<String> subway_list = mDataset.get(position).getNear_Subway();

        CardView cardView = holder.cardView;

        TextView cafe_name = cardView.findViewById(R.id.cafe_name);
        TextView near_subway = cardView.findViewById(R.id.near_subway);
        TextView lead_time_s = cardView.findViewById(R.id.lead_time_s);
        TextView near_univ = cardView.findViewById(R.id.near_univ);
        TextView lead_time_u = cardView.findViewById(R.id.lead_time_u);

        cafe_name.setText( mDataset.get(position).getCafe_name() );

        near_subway.setText( subway_list.get(1) );
        lead_time_s.setText( subway_list.get(2) );

        near_univ.setText( univ_list.get(0) );
        lead_time_u.setText( univ_list.get(1) );
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
