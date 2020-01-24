package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPodgladuNagrania extends RecyclerView.Adapter< AdapterPodgladuNagrania.MyViewHolderNagranie>{

    public List<GlosoweEntity> lista_nagran;
    public Context context;
    private NotatkaEntity notatkaEntity;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new AdapterPodgladuNagrania.MyOnClickListener();

    public static class MyViewHolderNagranie extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_nazwa;
        public TextView textView_dlugosc;

        public MyViewHolderNagranie(View v) {
            super(v);
            textView_nazwa = v.findViewById(R.id.podglad_nagrania_nazwa);
            textView_dlugosc = v.findViewById(R.id.podglad_nagrania_dlugosc);
        }
    }

    public AdapterPodgladuNagrania(List lista_nagran, RecyclerView rv, Context context, NotatkaEntity notatkaEntity) {
        this.lista_nagran = lista_nagran;
        this.context = context;
        this.rv = rv;
        this.notatkaEntity = notatkaEntity;
    }

    public void setData(List<GlosoweEntity> newData) {
        this.lista_nagran= newData;
        notifyDataSetChanged();
    }

    @Override
    public AdapterPodgladuNagrania.MyViewHolderNagranie onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nagranie_glosowe_podglad, parent, false);
        v.setOnClickListener(mOnClickListener);
        AdapterPodgladuNagrania.MyViewHolderNagranie vh = new AdapterPodgladuNagrania.MyViewHolderNagranie(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuNagrania.MyViewHolderNagranie holder, int position) {
        holder.textView_nazwa.setText(lista_nagran.get(position).getNazwaNagrania());
        //holder.textView_Dl.setText(lista_nagran.get(position).getTresc());
    }

    @Override
    public int getItemCount() {
        return lista_nagran.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                GlosoweEntity item = lista_nagran.get(itemPosition);
                openWpis(item);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openWpis(GlosoweEntity we){
            //Intent intent = new Intent(context, EdycjaWpisuActivity.class);
            //intent.putExtra("nazwaWpisu", we.getNazwaWpisu());
            //intent.putExtra("nazwaNotatki", we.getNotatkaId());
            //context.startActivity(intent);
        }
    }
}
