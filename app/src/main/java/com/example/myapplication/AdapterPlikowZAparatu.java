package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AdapterPlikowZAparatu extends RecyclerView.Adapter<AdapterPlikowZAparatu.MyViewHolderNotatka> {

    public List<AparatEntity> lista_plikow;
    public Context context;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    @NonNull
    @Override
    public MyViewHolderNotatka onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plik_z_aparatu_podglad    , parent, false);
        v.setOnClickListener(mOnClickListener);
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderNotatka holder, int position) {
        File plik = new File(lista_plikow.get(position).getPathPliku());
        Picasso.get().load(plik).into(holder.imageView_miniaturka);
    }

    @Override
    public int getItemCount() {
        return lista_plikow.size();
    }

    public void setData(List<AparatEntity> newData) {
        this.lista_plikow = newData;
        notifyDataSetChanged();
    }

    public static class MyViewHolderNotatka extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView_miniaturka;

        public MyViewHolderNotatka(View v) {
            super(v);
            imageView_miniaturka = v.findViewById(R.id.minaturka_image);
        }
    }

    public AdapterPlikowZAparatu(List lista_plikow, RecyclerView rv, Context context) {
        this.lista_plikow = lista_plikow;
        this.context = context;
        this.rv = rv;
    }



    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                System.out.println(rv);
                int itemPosition = rv.getChildLayoutPosition(view);
                AparatEntity item = lista_plikow.get(itemPosition);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }

    }
}
