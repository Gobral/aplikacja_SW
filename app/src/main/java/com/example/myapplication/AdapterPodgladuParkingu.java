package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class AdapterPodgladuParkingu extends RecyclerView.Adapter<AdapterPodgladuParkingu.MyViewHolderNotatka> {

    public List<ParkingEntity> lista_parkingow;
    public Context context;
    public RecyclerView rv;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    private final View.OnLongClickListener mOnLongClickListener = new MyOnLongClickListener();
    GoogleMap googleMap;


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

    public AdapterPodgladuParkingu(List lista_parkingow, RecyclerView rv, Context context, GoogleMap googleMap) {
        this.lista_parkingow = lista_parkingow;
        this.context = context;
        this.rv = rv;
        this.googleMap = googleMap;
    }

    @Override
    public AdapterPodgladuParkingu.MyViewHolderNotatka onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podglad_parkingu, parent, false);
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
        MyViewHolderNotatka vh = new MyViewHolderNotatka(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPodgladuParkingu.MyViewHolderNotatka holder, int position) {
        holder.textView_nazwa.setText(lista_parkingow.get(position).getNazwaParkingu());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lista_parkingow.get(position).getLattitude(), lista_parkingow.get(position).getLongitude(), 1);
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            String postalCode = addresses.get(0).getPostalCode();
            holder.textView_lokacja.setText(country + (city != null ? ", " + city + " " + postalCode : ""));

        } catch (IOException e) {
            e.printStackTrace();
            holder.textView_lokacja.setText("Błąd wczytywania");
        }

    }

    @Override
    public int getItemCount() {
        return lista_parkingow.size();
    }

    public void setData(List<ParkingEntity> newData) {
        this.lista_parkingow = newData;
        notifyDataSetChanged();
    }

    public void  setMapa(GoogleMap mapa){
        this.googleMap = mapa;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                int itemPosition = rv.getChildLayoutPosition(view);
                ParkingEntity item = lista_parkingow.get(itemPosition);
                LatLng sydney = new LatLng(item.getLattitude(), item.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
    private class MyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {

            int itemPosition = rv.getChildLayoutPosition(v);
            ParkingEntity item = lista_parkingow.get(itemPosition);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Usuń " + item.getNazwaParkingu());
            alert.setMessage("Czy na pewno chcesz usunąć wybrany parking?");
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

    private class UsunAsyncTask  extends AsyncTask<Void, Void, ParkingEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private ParkingEntity nazwaParkingu;

        public UsunAsyncTask(Activity activity, ParkingEntity nazwaParkingu) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaParkingu = nazwaParkingu;
        }

        @Override
        protected ParkingEntity doInBackground(Void... params) {

            ParkingEntity parkingEntity = nazwaParkingu;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                notatkiDb.parkingDAO().deleteParking(nazwaParkingu);
            }
            catch (Exception e){
                parkingEntity = null;
            }
            return parkingEntity;
        }

    }
}
