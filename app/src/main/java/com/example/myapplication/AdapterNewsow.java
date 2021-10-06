package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNewsow extends RecyclerView.Adapter<AdapterNewsow.MyViewHolder> {
    public ArrayList<NewsClass> mDataset;
    public String[] tablica;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public CardView cardView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.wpis_naglowek);
            imageView = v.findViewById(R.id.wpis_image_view);
            cardView = v.findViewById(R.id.wpis_cardview);
        }
    }

    public AdapterNewsow(ArrayList<NewsClass> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AdapterNewsow.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_sr_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    public void aktualizuj(){
        notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position).tresc);
        Picasso.get().load(mDataset.get(position).miniaturka).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WyswietlanieNewsaActivity.class);
                intent.putExtra("url", mDataset.get(position).link);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}