package com.example.myapplication;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class EdycjaWpisuFragment extends Fragment {
    private NotatkaEntity notatkaEntity;
    private WpisEntity wpisEntity;

    public EdycjaWpisuFragment(NotatkaEntity notatkaEntity, WpisEntity wpisEntity) {
        this.notatkaEntity = notatkaEntity;
        this.wpisEntity = wpisEntity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_edycja_wpisu, container, false);

       return rootView;
    }

    private class ZapiszNotatke extends AsyncTask<Void, Void, WpisEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private NotatkaEntity notatkaEntity;
        private String nowa_zawartosc;

        public ZapiszNotatke(Activity activity, NotatkaEntity notatkaEntity, String nowa_zawartosc) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.notatkaEntity = notatkaEntity;
            this.nowa_zawartosc = nowa_zawartosc;
        }

        @Override
        protected WpisEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            WpisEntity we = null;
            try {
                //notatkiDb.notatkiDAO().updateZawartosc(nowa_zawartosc, notatkaEntity.getNazwaNotatki());
            }
            catch (Exception e){

            }
            return we;
        }

    }
}
