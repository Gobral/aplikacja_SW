package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FilmActivity extends AppCompatActivity {

    private CustomOrientationEventListener customOrientationEventListener;

    final int ROTATION_O    = 1;
    final int ROTATION_90   = 2;
    final int ROTATION_180  = 3;
    final int ROTATION_270  = 4;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private NotatkaEntity notatkaEntity;
    private String nazwaNotatki;
    private File ostatni_plik;
    private List<File> nowePliki;
    private String nazwaFilmu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        nazwaNotatki = b.getString("nazwa");
        nazwaFilmu = b.getString("film");
        try {
            notatkaEntity = new WczytajNotatke(this, nazwaNotatki).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_film);
        getSupportActionBar().hide();

        CameraView camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);

        FloatingActionButton fbfilm = findViewById(R.id.fbvideo);
        File path = getExternalCacheDir();
        nowePliki = new ArrayList<>();

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                // A Picture was taken!
                fbfilm.setVisibility(FloatingActionButton.VISIBLE);
            }

            @Override
            public void onVideoTaken(VideoResult result) {
                fbfilm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCzekania)));

                File kopiaPliku = new File(ostatni_plik.getPath());
                new DodajPlikZAparatu(FilmActivity.this, kopiaPliku, nazwaNotatki).execute();
                nowePliki.add(kopiaPliku);
                FilmActivity.this.finish();
            }

            // And much more
        });

        fbfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!camera.isTakingVideo() && !camera.isTakingPicture()){
                    camera.setMode(Mode.VIDEO);
                    fbfilm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    String nazwa = generujNazwe("mp4", "film");

                    ostatni_plik = new File(path, nazwa);
                    try {
                        ostatni_plik.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.takeVideo(ostatni_plik);
                }
                else if(camera.isTakingVideo()) {
                    camera.stopVideo();
                }
            }
        });
        customOrientationEventListener = new
                CustomOrientationEventListener(getBaseContext()) {
                    @Override
                    public void onSimpleOrientationChanged(int orientation) {
                        switch(orientation){
                            case ROTATION_O:
                                //rotate as on portrait
                                fbfilm.animate().rotation(0).setDuration(500).start();
                                break;
                            case ROTATION_90:
                                //rotate as left on top
                                fbfilm.animate().rotation(-90).setDuration(500).start();
                                break;
                            case ROTATION_270:
                                //rotate as right on top
                                fbfilm.animate().rotation(90).setDuration(500).start();
                                break;
                            case ROTATION_180:
                                //rotate as upside down
                                fbfilm.animate().rotation(180).setDuration(500).start();
                                break;

                        }
                    }
                };

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

    private class DodajPlikZAparatu extends AsyncTask<Void, Void, String> {

        private WeakReference<Activity> weakActivity;
        private String nazwa;
        private String path;
        private Context context;

        public DodajPlikZAparatu(Activity activity, File plik, String nazwaNotatki) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.path= plik.getPath();
            this.nazwa = nazwaNotatki;
        }

        @Override
        protected String doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                notatkiDb.filmDAO().insertFilm(new FilmEntity(path, nazwa, nazwaFilmu));
            }
            catch (Exception e){

            }
            return path;
        }

    }

    public String generujNazwe(String koncowka, String przed){
        String ret = "";
        Calendar currentTime = Calendar.getInstance();
        ret += przed + "_" + currentTime.get(currentTime.YEAR) + currentTime.get(currentTime.MONTH) + currentTime.get(currentTime.DAY_OF_MONTH) + "_" + currentTime.get(currentTime.HOUR_OF_DAY)
                + currentTime.get(currentTime.MINUTE) + currentTime.get(currentTime.SECOND) + currentTime.get(currentTime.MILLISECOND) + "." + koncowka;
        return  ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        customOrientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        customOrientationEventListener.disable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customOrientationEventListener.disable();
    }
}
