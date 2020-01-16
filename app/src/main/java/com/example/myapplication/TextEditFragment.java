package com.example.myapplication;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextEditFragment extends Fragment {

    EditText editText;
    NotatkaEntity notatkaEntity;
    String lokalny_tekst;
    public  TextEditFragment(NotatkaEntity notatkaEntity){
        super();
        this.notatkaEntity = notatkaEntity;
        this.lokalny_tekst = notatkaEntity.getZawartosc();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_edit, container, false);
        editText = view.findViewById(R.id.plain_text_input);
        editText.setText(lokalny_tekst);

        return view;
    }

    @Override
    public void onDetach () {
        new ZapiszNotatke(getActivity(), notatkaEntity, editText.getText().toString()).execute();
        super.onDetach();
    }

    private class ZapiszNotatke extends AsyncTask<Void, Void, NotatkaEntity> {

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
        protected NotatkaEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            NotatkaEntity ne = null;
            try {
                notatkiDb.notatkiDAO().updateZawartosc(nowa_zawartosc, notatkaEntity.getNazwaNotatki());
            }
            catch (Exception e){

            }
            return ne;
        }

    }


}
