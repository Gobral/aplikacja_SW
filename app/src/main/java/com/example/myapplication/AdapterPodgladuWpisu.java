package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPodgladuWpisu  extends RecyclerView.Adapter< AdapterPodgladuWpisu.MyViewHolderWpis> {

    public List<WpisEntity> lista_wpiso;
    public Context context;
    private NotatkaEntity notatkaEntity;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public static class MyViewHolderWpis extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_nazwa;
        public TextView textView_tresc;

        public MyViewHolderWpis(View v) {
            super(v);
            textView_nazwa = v.findViewById(R.id.podglad_wpisu_nazwa);
            textView_tresc = v.findViewById(R.id.podglad_wpisu_tresc);
        }
    }

    public AdapterPodgladuWpisu(List lista_wpisow, RecyclerView rv, Context context, NotatkaEntity notatkaEntity) {
        this.lista_wpiso = lista_wpisow;
        this.context = context;
        this.rv = rv;
        this.notatkaEntity = notatkaEntity;
    }

    public void setData(List<WpisEntity> newData) {
        this.lista_wpiso= newData;
        notifyDataSetChanged();
    }

    @Override
    public AdapterPodgladuWpisu.MyViewHolderWpis onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wpis_podglad, parent, false);
        v.setOnClickListener(mOnClickListener);
        MyViewHolderWpis vh = new MyViewHolderWpis(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuWpisu.MyViewHolderWpis holder, int position) {
        holder.textView_nazwa.setText(lista_wpiso.get(position).getNazwaWpisu());
        holder.textView_tresc.setText(lista_wpiso.get(position).getTresc());
    }

    @Override
    public int getItemCount() {
        return lista_wpiso.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                WpisEntity item = lista_wpiso.get(itemPosition);
                openWpis(item);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openWpis(WpisEntity we){
            //System.out.println("Zamienianie");
            //Fragment fragment = new EdycjaWpisuFragment(notatkaEntity, we);
            //AppCompatActivity aca = (AppCompatActivity) context;
            //FragmentTransaction transaction = aca.getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.edycja_wpisu_layout, fragment).commit();
            Intent intent = new Intent(context, EdycjaWpisuActivity.class);
            intent.putExtra("nazwaWpisu", we.getNazwaWpisu());
            intent.putExtra("nazwaNotatki", we.getNotatkaId());
            context.startActivity(intent);
        }
    }
}
