package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class AdapterPodgladuFilmu extends RecyclerView.Adapter<AdapterPodgladuFilmu.MyViewHolderNotatka> {

    public String SCHEME_VIDEO="video";
    public List<FilmEntity> lista_plikow;
    public Context context;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private final View.OnLongClickListener mOnLongClickListener = new MyOnLongClickListener();


    @NonNull
    @Override
    public MyViewHolderNotatka onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podglad_filmu    , parent, false);
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
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
        }
        holder.nazwaPliku.setText(lista_plikow.get(position).getNazwaFilmud());


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

    public void setData(List<FilmEntity> newData) {
        this.lista_plikow = newData;
        notifyDataSetChanged();
    }

    public static class MyViewHolderNotatka extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView_miniaturka;
        public TextView nazwaPliku;

        public MyViewHolderNotatka(View v) {
            super(v);
            imageView_miniaturka = v.findViewById(R.id.minaturka_image);
            nazwaPliku = v.findViewById(R.id.miniaturka_nazwa);

        }
    }

    public AdapterPodgladuFilmu(List lista_plikow, RecyclerView rv, Context context) {
        this.lista_plikow = lista_plikow;
        this.context = context;
        this.rv = rv;
    }



    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                FilmEntity item = lista_plikow.get(itemPosition);
                openWyswietlanie(item);

            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openWyswietlanie(FilmEntity ne){
            Intent intent = new Intent(context, WyswietlaniePliku.class);
            intent.putExtra("path", ne.getPathPliku());
            context.startActivity(intent);
        }

    }

    private class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {

            int itemPosition = rv.getChildLayoutPosition(v);
            FilmEntity item = lista_plikow.get(itemPosition);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Usuń plik");
            alert.setMessage("Czy na pewno chcesz usunąć " + item.getNazwaFilmud() +"?");
            alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    String path = item.getPathPliku();
                    new UsunAsyncTask((Activity) context, item).execute();
                    File file = new File(path);
                    file.delete();
                    notifyItemRemoved(itemPosition);
                }
            });
            alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                    dialog.cancel();
                }
            });
            AlertDialog ad = alert.create();
            ad.show();
            return true;
        }
    }

    private class UsunAsyncTask  extends AsyncTask<Void, Void, FilmEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private FilmEntity plik;

        public UsunAsyncTask(Activity activity, FilmEntity plik) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.plik = plik;
        }

        @Override
        protected FilmEntity doInBackground(Void... params) {

            FilmEntity aparatEntity = plik;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                notatkiDb.filmDAO().deleteAparat(plik);
            }
            catch (Exception e) {
                aparatEntity = null;
            }
            return aparatEntity;
        }

    }
}
