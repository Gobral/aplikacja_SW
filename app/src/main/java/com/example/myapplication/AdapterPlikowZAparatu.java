package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AdapterPlikowZAparatu extends RecyclerView.Adapter<AdapterPlikowZAparatu.MyViewHolderNotatka> {

    public String SCHEME_VIDEO="video";
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int wielkosc = displayMetrics.widthPixels / 2;
        v.setLayoutParams(new LinearLayout.LayoutParams(wielkosc, wielkosc));
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderNotatka holder, int position) {
        String nazwa = lista_plikow.get(position).getPathPliku();

        File plik = new File(nazwa);
        if(nazwa.charAt(nazwa.length() - 1) == '4'){

            VideoRequestHandler videoRequestHandler = new VideoRequestHandler();
            Picasso picassoInstance  = new Picasso.Builder(context.getApplicationContext()).addRequestHandler(videoRequestHandler).build();
            picassoInstance.load(SCHEME_VIDEO+":"+nazwa).resize(400, 400).into(holder.imageView_miniaturka);
            //holder.film_odtworz.bringToFront();
        }
        else{
            Picasso.get().load(plik).resize(400, 400).into(holder.imageView_miniaturka);
            holder.film_odtworz.setVisibility(ImageView.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return lista_plikow.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<AparatEntity> newData) {
        this.lista_plikow = newData;
        notifyDataSetChanged();
    }

    public static class MyViewHolderNotatka extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView_miniaturka;
        public ImageView film_odtworz;

        public MyViewHolderNotatka(View v) {
            super(v);
            imageView_miniaturka = v.findViewById(R.id.minaturka_image);
            film_odtworz = v.findViewById(R.id.film_odtworz_znak);

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
                int itemPosition = rv.getChildLayoutPosition(view);
                AparatEntity item = lista_plikow.get(itemPosition);
                openWyswietlanie(item);

            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openWyswietlanie(AparatEntity ne){
            Intent intent = new Intent(context, WyswietlaniePliku.class);
            intent.putExtra("path", ne.getPathPliku());
            context.startActivity(intent);
        }

    }
}
