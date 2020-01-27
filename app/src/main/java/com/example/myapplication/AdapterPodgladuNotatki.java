package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterPodgladuNotatki  extends RecyclerView.Adapter< AdapterPodgladuNotatki.MyViewHolderNotatka> {

    public List<NotatkaEntity> lista_notatek;
    public Context context;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    private final View.OnLongClickListener mOnLongClickListener = new  MyOnLongClickListener();


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

    public AdapterPodgladuNotatki(List lista_notatek, RecyclerView rv, Context context) {
        this.lista_notatek = lista_notatek;
        this.context = context;
        this.rv = rv;
    }

    @Override
    public AdapterPodgladuNotatki.MyViewHolderNotatka onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notatka_podglad_view, parent, false);
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuNotatki.MyViewHolderNotatka holder, int position) {
        holder.textView_nazwa.setText(lista_notatek.get(position).getNazwaNotatki());
        Date d = lista_notatek.get(position).getDataDodania();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        holder.textView_data.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + (cal.get(Calendar.YEAR) - 1900) + "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
    }

    @Override
    public int getItemCount() {
        return lista_notatek.size();
    }

    public void setData(List<NotatkaEntity> newData) {
        this.lista_notatek = newData;
        notifyDataSetChanged();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                NotatkaEntity item = lista_notatek.get(itemPosition);
                openNotes(item);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        public void openNotes(NotatkaEntity ne){
            Intent intent = new Intent(context, Notes2Activity.class);
            intent.putExtra("nazwa", ne.getNazwaNotatki());
            context.startActivity(intent);
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {

            int itemPosition = rv.getChildLayoutPosition(v);
            NotatkaEntity item = lista_notatek.get(itemPosition);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Usuń " + item.getNazwaNotatki());
            alert.setMessage("Czy na pewno chcesz usunąć wybrany dziennik?");
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

    private class UsunAsyncTask  extends AsyncTask<Void, Void, NotatkaEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private NotatkaEntity dziennik;

        public UsunAsyncTask(Activity activity, NotatkaEntity dziennik) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.dziennik = dziennik;
        }

        @Override
        protected NotatkaEntity doInBackground(Void... params) {

            NotatkaEntity entity = dziennik;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                notatkiDb.notatkiDAO().deleteNotatka(dziennik);
            }
            catch (Exception e){
                entity = null;
            }
            return entity;
        }

    }
}
