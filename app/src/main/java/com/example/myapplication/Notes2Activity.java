package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class Notes2Activity extends AppCompatActivity {
    private NotatkaEntity notatkaEntity;
    private String nazwaNotatki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        nazwaNotatki = b.getString("nazwa");
        try {
            notatkaEntity = new WczytajNotatke(this, nazwaNotatki).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_notes2);

        TextView tv = findViewById(R.id.title);
        if(nazwaNotatki.length() > 12){
            tv.setText(nazwaNotatki.substring(0,10) + "... - Edycja");
        }
        else{
            tv.setText(nazwaNotatki + " - Edycja");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), notatkaEntity);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton wpis_fab = findViewById(R.id.dodaj_wpis_fb);
        wpis_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Notes2Activity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę wpisu");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){
                            new Notes2Activity.DodanieAsyncTask(Notes2Activity.this, wpisano).execute();
                            //new PodgladNotatek.DodanieAsyncTask(PodgladNotatek.this.getActivity() , wpisano).execute();
                        }
                        else{
                            Toast.makeText(Notes2Activity.this, "Podaj poprawną nazwę", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(Notes2Activity.this, AparatActivity.class);
                intent.putExtra("nazwa", notatkaEntity.getNazwaNotatki());
                startActivity(intent);
            }
        });
    }

    private class WczytajNotatke extends AsyncTask<Void, Void, NotatkaEntity> {

        private WeakReference<Activity> weakActivity;
        private String nazwa;
        private Context context;

        public WczytajNotatke(Activity activity, String nazwa) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwa = nazwa;
        }

        @Override
        protected NotatkaEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            NotatkaEntity ne = null;
            try {
                ne = notatkiDb.notatkiDAO().loadNotatkaByName(nazwa);

            }
            catch (Exception e){

            }
            return ne;
        }

    }

    private class DodanieAsyncTask  extends AsyncTask<Void, Void, WpisEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private String nazwaWpisu;

        public DodanieAsyncTask(Activity activity, String nazwaWpisu) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaWpisu = nazwaWpisu;
        }

        @Override
        protected WpisEntity doInBackground(Void... params) {

            WpisEntity wpisEntity = null;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                wpisEntity = new WpisEntity(nazwaWpisu, nazwaNotatki, "");
                notatkiDb.wpisyDAO().insertWpis(wpisEntity);
            }
            catch (Exception e){
                wpisEntity = null;
            }
            return wpisEntity;
        }

        @Override
        protected void onPostExecute(WpisEntity notatkaEntity) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }
            if(notatkaEntity == null){
                Toast.makeText(activity, "Nazwa już istnieje", Toast.LENGTH_LONG).show();
            }

        }
    }


}