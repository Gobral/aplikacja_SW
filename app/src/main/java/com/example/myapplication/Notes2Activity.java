package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class Notes2Activity extends AppCompatActivity {
    private NotatkaEntity notatkaEntity;
    private String nazwaNotatki;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        nazwaNotatki = b.getString("nazwa");
        try {
            notatkaEntity = new WczytajNotatke(this, nazwaNotatki).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_notes2);

        TextView tv = findViewById(R.id.title);
        tv.setText(nazwaNotatki);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), notatkaEntity);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton wpis_fab = findViewById(R.id.dodaj_wpis_fb);
        FloatingActionButton nagranie_fab = findViewById(R.id.dodaj_glosowe_fb);
        FloatingActionButton fimy_fab = findViewById(R.id.dodaj_video_fb);
        FloatingActionButton loklizacja_fab = findViewById(R.id.lokalizacja_update_fb);
        wpis_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Notes2Activity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę wpisu");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                Calendar currentTime = Calendar.getInstance();
                input.setText("Wpis " + currentTime.get(currentTime.YEAR) + "-" + (currentTime.get(currentTime.MONTH) + 1) + "-" + currentTime.get(currentTime.DAY_OF_MONTH) + " " + currentTime.get(currentTime.HOUR_OF_DAY)
                        + ":" +  currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND));
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){
                            new Notes2Activity.DodanieAsyncTask(Notes2Activity.this, wpisano).execute();
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

                Context context = Notes2Activity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę zdjęcia");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                Calendar currentTime = Calendar.getInstance();
                input.setText("Zdjęcie " + currentTime.get(currentTime.YEAR) + "-" + (currentTime.get(currentTime.MONTH) + 1) + "-" + currentTime.get(currentTime.DAY_OF_MONTH) + " " + currentTime.get(currentTime.HOUR_OF_DAY)
                        + ":" +  currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND));
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){

                            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Intent intent = new Intent(Notes2Activity.this, AparatActivity.class);
                            intent.putExtra("nazwa", notatkaEntity.getNazwaNotatki());
                            intent.putExtra("zdjecie", wpisano);
                            startActivity(intent);
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

        fimy_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = Notes2Activity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę filmu");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                Calendar currentTime = Calendar.getInstance();
                input.setText("Film " + currentTime.get(currentTime.YEAR) + "-" + (currentTime.get(currentTime.MONTH) + 1) + "-" + currentTime.get(currentTime.DAY_OF_MONTH) + " " + currentTime.get(currentTime.HOUR_OF_DAY)
                        + ":" +  currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND));
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){

                            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Intent intent = new Intent(Notes2Activity.this, FilmActivity.class);
                            intent.putExtra("nazwa", notatkaEntity.getNazwaNotatki());
                            intent.putExtra("film", wpisano);
                            startActivity(intent);
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

        nagranie_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = Notes2Activity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater li = LayoutInflater.from(Notes2Activity.this);
                RelativeLayout relativeLayout = (RelativeLayout) li.inflate(R.layout.nagrywanie_dialog, null);

                EditText input = relativeLayout.findViewById(R.id.dialog_relative_nazwa);

                Calendar currentTime = Calendar.getInstance();
                input.setText("Nagranie " + currentTime.get(currentTime.YEAR) + "-" + (currentTime.get(currentTime.MONTH) + 1) + "-" + currentTime.get(currentTime.DAY_OF_MONTH) + " " + currentTime.get(currentTime.HOUR_OF_DAY)
                        + ":" +  currentTime.get(currentTime.MINUTE) + ":" + currentTime.get(currentTime.SECOND));


                TextView start = relativeLayout.findViewById(R.id.dialog_nagranie_start);
                TextView stop = relativeLayout.findViewById(R.id.dialog_nagranie_stop);
                TextView zapisz = relativeLayout.findViewById(R.id.dialog_nagranie_zapisz);
                TextView czas = relativeLayout.findViewById(R.id.dialog_nagranie_aktualnie_nagrano);
                final int[] sekundy = {0};
                Timer timer = new Timer();


                File path = getExternalCacheDir();
                String nazwa_nagrania = "Nagranie" + "_" + currentTime.get(currentTime.YEAR) + currentTime.get(currentTime.MONTH) + currentTime.get(currentTime.DAY_OF_MONTH) + "_" + currentTime.get(currentTime.HOUR_OF_DAY)
                        + currentTime.get(currentTime.MINUTE) + currentTime.get(currentTime.SECOND) + currentTime.get(currentTime.MILLISECOND) + ".3gp";
                File nowe_nagranie = new File(path, nazwa_nagrania);

                builder.setView(relativeLayout);
                AlertDialog alertDialog = builder.create();
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop.setClickable(true);
                        stop.setTextColor(getResources().getColor(R.color.colorAccent));

                        try {
                            nowe_nagranie.createNewFile();
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.setOutputFile(nowe_nagranie.getPath());
                            mediaRecorder.prepare();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable()  {
                                        @Override
                                        public void run() {
                                            sekundy[0]++;
                                            int minuty = sekundy[0] / 60;
                                            int sekunda_minuty = sekundy[0] - minuty * 60;
                                            czas.setText(minuty + ":" + (sekunda_minuty < 10 ? "0" + sekunda_minuty : sekunda_minuty));
                                        }
                                    });
                                }
                            }, 1000, 1000);
                            mediaRecorder.start();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        start.setClickable(false);
                        start.setTextColor(getResources().getColor(R.color.colorCzekania));
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop.setClickable(false);
                        stop.setTextColor(getResources().getColor(R.color.colorCzekania));

                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();

                        timer.cancel();

                        zapisz.setClickable(true);
                        zapisz.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                });

                zapisz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aktualne = input.getText().toString();
                        if(aktualne.length() > 0 && !Character.isWhitespace(aktualne.charAt(0))) {
                            new DodanieNagraniaAsyncTask(Notes2Activity.this, input.getText().toString(), nowe_nagranie).execute();
                            alertDialog.dismiss();
                        }
                        else {
                            Toast.makeText(Notes2Activity.this, "Podaj poprawną nazwę", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.show();
            }
        });

        loklizacja_fab.setTag(false);

        loklizacja_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(!(Boolean) loklizacja_fab.getTag());
                if (!(Boolean) loklizacja_fab.getTag()) {
                    Context context = Notes2Activity.this;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Czy na pewno chcesz zaktualizować lokalizację dziennika?");

                    builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loklizacja_fab.setTag(true);
                            loklizacja_fab.setImageResource(R.drawable.ic_gps_fixed_black_24dp);
                            LocationCallback mLocationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if ((Boolean) loklizacja_fab.getTag()) {
                                        Location location = new Location(locationResult.getLastLocation());
                                        notatkaEntity.setLattitude(location.getLatitude());
                                        notatkaEntity.setLongitude(location.getLongitude());
                                        loklizacja_fab.setTag(false);
                                        Toast.makeText(context, "Zaktualizowano lokalizację.", Toast.LENGTH_LONG).show();
                                        loklizacja_fab.setImageResource(R.drawable.ic_gps_not_fixed_black_24dp);
                                        new AktualizujNotatke(Notes2Activity.this, notatkaEntity).execute();
                                    }
                                }
                            };

                            FusedLocationProviderClient fusedLocationClient = null;
                            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                            LocationRequest request = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(2000); // Update every 5 seconds.
                            fusedLocationClient.requestLocationUpdates(request, mLocationCallback, null);
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } //if ((Boolean) loklizacja_fab.getTag()) {
                else {
                    loklizacja_fab.setTag(false);
                    loklizacja_fab.setImageResource(R.drawable.ic_gps_not_fixed_black_24dp);
                }
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

    private class AktualizujNotatke extends AsyncTask<Void, Void, NotatkaEntity> {

        private WeakReference<Activity> weakActivity;
        private NotatkaEntity notatka;
        private Context context;

        public AktualizujNotatke(Activity activity, NotatkaEntity notatka) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.notatka = notatka;
        }

        @Override
        protected NotatkaEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            notatkiDb.notatkiDAO().updateNotatka(notatka);
            return notatka;
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

    private class DodanieNagraniaAsyncTask  extends AsyncTask<Void, Void, GlosoweEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private String nazwaNagrania;
        private String file;

        public DodanieNagraniaAsyncTask(Activity activity, String nazwaNagrania, File file) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaNagrania = nazwaNagrania;
            this.file = file.getPath();
        }

        @Override
        protected GlosoweEntity doInBackground(Void... params) {

            GlosoweEntity glosoweEntity = null;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                glosoweEntity = new GlosoweEntity(nazwaNagrania, nazwaNotatki, file);
                notatkiDb.glosoweDAO().insertNagranie(glosoweEntity);
            }
            catch (Exception e){
                glosoweEntity = null;
            }
            return glosoweEntity;
        }

        @Override
        protected void onPostExecute(GlosoweEntity notatkaEntity) {
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