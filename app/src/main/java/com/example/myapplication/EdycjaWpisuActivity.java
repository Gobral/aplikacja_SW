package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class EdycjaWpisuActivity extends AppCompatActivity {

    private WpisEntity wpisEntity;
    private EditText editText;
    private String kopiaTresci = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edycja_wpisu);
        editText = findViewById(R.id.plain_text_input);
        TextView textViewnNazwa = findViewById(R.id.edycja_nazwa_wpisu);
        TextView anuluj = findViewById(R.id.edycja_wpisu_anuluj);
        TextView zapisz = findViewById(R.id.edycja_wpisu_zapisz);

        Bundle b = getIntent().getExtras();
        String nazwaWpisu = b.getString("nazwaWpisu");
        String nazwaNotatki = b.getString("nazwaNotatki");
        try {
            wpisEntity = new EdycjaWpisuActivity.WczytajWpis(this, nazwaWpisu, nazwaNotatki).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (wpisEntity != null){
            kopiaTresci = wpisEntity.getTresc();
            editText.setText(wpisEntity.getTresc());
            textViewnNazwa.setText(wpisEntity.getNazwaWpisu());
        }

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ZapiszWpis(EdycjaWpisuActivity.this, wpisEntity, editText.getText().toString()).execute();
            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(kopiaTresci);
                new ZapiszWpis(EdycjaWpisuActivity.this, wpisEntity, kopiaTresci).execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if( !editText.getText().toString().equals(wpisEntity.getTresc())) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Powrót do dziennika")
                    .setMessage("Czy chcesz zapisać zmany?")
                    .setPositiveButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ZapiszWpis(EdycjaWpisuActivity.this, wpisEntity, editText.getText().toString()).execute();
                            finish();
                        }

                    })
                    .show();
        }
        else {
            finish();
        }
    }

    private class ZapiszWpis extends AsyncTask<Void, Void, WpisEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private WpisEntity wpisEntity;
        private String nowa_zawartosc;

        public ZapiszWpis(Activity activity, WpisEntity wpisEntity, String nowa_zawartosc) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.wpisEntity = wpisEntity;
            this.nowa_zawartosc = nowa_zawartosc;
        }

        @Override
        protected WpisEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            WpisEntity we = null;
            try {

                //notatkiDb.notatkiDAO().updateZawartosc(nowa_zawartosc, notatkaEntity.getNazwaNotatki());
                wpisEntity.setTresc(nowa_zawartosc);
                notatkiDb.wpisyDAO().updateWpis(wpisEntity);
            }
            catch (Exception e){

            }
            return we;
        }

    }

    private class WczytajWpis extends AsyncTask<Void, Void, WpisEntity> {

        private WeakReference<Activity> weakActivity;
        private String nazwaWpisu;
        private String nazwaNotatki;
        private Context context;

        public WczytajWpis(Activity activity, String nazwaWpisu, String nazwaNotatki) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaWpisu = nazwaWpisu;
            this.nazwaNotatki = nazwaNotatki;
        }

        @Override
        protected WpisEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            WpisEntity ne = null;
            try {
                ne = notatkiDb.wpisyDAO().loadWpisByNazwy(nazwaWpisu, nazwaNotatki);

            }
            catch (Exception e){

            }
            return ne;
        }

    }
}
