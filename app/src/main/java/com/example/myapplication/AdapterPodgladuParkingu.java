package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPodgladuParkingu extends RecyclerView.Adapter<AdapterPodgladuParkingu.MyViewHolderNotatka> {

    public List<ParkingEntity> lista_parkingow;
    public Context context;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();


    public static class MyViewHolderNotatka extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_nazwa;
        public TextView textView_lokacja;

        public MyViewHolderNotatka(View v) {
            super(v);
            textView_nazwa = v.findViewById(R.id.podglad_parkingu_nazwa);
            textView_lokacja = v.findViewById(R.id.podglad_parkingu_lokacja);
        }
    }

    public AdapterPodgladuParkingu(List lista_parkingow, RecyclerView rv, Context context) {
        this.lista_parkingow = lista_parkingow;
        this.context = context;
        this.rv = rv;
    }

    @Override
    public AdapterPodgladuParkingu.MyViewHolderNotatka onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podglad_parkingu, parent, false);
        v.setOnClickListener(mOnClickListener);
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuParkingu.MyViewHolderNotatka holder, int position) {
        holder.textView_nazwa.setText(lista_parkingow.get(position).getNazwaParkingu());
        holder.textView_lokacja.setText("Chwila");
    }

    @Override
    public int getItemCount() {
        return lista_parkingow.size();
    }

    public void setData(List<ParkingEntity> newData) {
        this.lista_parkingow = newData;
        notifyDataSetChanged();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                ParkingEntity item = lista_parkingow.get(itemPosition);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
