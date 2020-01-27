package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class AdapterPodgladuWpisu  extends RecyclerView.Adapter< AdapterPodgladuWpisu.MyViewHolderWpis> {

    public List<WpisEntity> lista_wpiso;
    public Context context;
    private NotatkaEntity notatkaEntity;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private final View.OnLongClickListener mOnLongClickListener = new MyOnLongClickListener();

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
        v.setOnLongClickListener(mOnLongClickListener);
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

    private class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {

            int itemPosition = rv.getChildLayoutPosition(v);
            WpisEntity item = lista_wpiso.get(itemPosition);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Usuń " + item.getNazwaWpisu());
            alert.setMessage("Czy na pewno chcesz usunąć wybrany wpis?");
            alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    new UsunAsyncTask((Activity) context, item).execute();

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

    private class UsunAsyncTask  extends AsyncTask<Void, Void, WpisEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private WpisEntity wpis;

        public UsunAsyncTask(Activity activity, WpisEntity wpis) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.wpis = wpis;
        }

        @Override
        protected WpisEntity doInBackground(Void... params) {

            WpisEntity entity = wpis;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                notatkiDb.wpisyDAO().deleteWpis(wpis);
            }
            catch (Exception e){
                entity = null;
            }
            return entity;
        }

    }
}
