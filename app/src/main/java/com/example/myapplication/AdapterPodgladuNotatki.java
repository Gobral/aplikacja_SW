package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPodgladuNotatki  extends RecyclerView.Adapter< AdapterPodgladuNotatki.MyViewHolderNotatka>  {

    public List<NotatkaEntity> lista_notatek;
    public Context context;

    public static class MyViewHolderNotatka extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_nazwa;
        public TextView textView_data;
        public MyViewHolderNotatka(View v) {
            super(v);
            textView_nazwa = v.findViewById(R.id.podglad_nazwa);
            textView_data = v.findViewById(R.id.podglad_data);
        }
    }

    public AdapterPodgladuNotatki(List lista_notatek, Context context){
        this.lista_notatek = lista_notatek;
        this.context = context;
    }

    @Override
    public AdapterPodgladuNotatki.MyViewHolderNotatka onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notatka_podglad_view, parent, false);
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuNotatki.MyViewHolderNotatka holder, int position) {
        holder.textView_nazwa.setText(lista_notatek.get(position).getNazwaNotatki());
        holder.textView_data.setText(lista_notatek.get(position).getDataDodania().toString());
    }

    @Override
    public int getItemCount() {
        return lista_notatek.size();
    }

    public void setData(List<NotatkaEntity> newData) {
        this.lista_notatek = newData;
        notifyDataSetChanged();
    }
}
