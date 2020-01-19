package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

public class AparatActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private NotatkaEntity notatkaEntity;
    private String nazwaNotatki;
    private File ostatni_plik;
    private List<File> nowePliki;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aparat);
        getSupportActionBar().hide();

        CameraView camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);

        FloatingActionButton fbzdjecie = findViewById(R.id.fbzdjecie);
        FloatingActionButton fbfilm = findViewById(R.id.fbvideo);
        File path = getExternalCacheDir();
        nowePliki = new ArrayList<>();

        System.out.println(notatkaEntity.getNazwaNotatki());

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
                Toast.makeText(AparatActivity.this, "Zrobiono zdjecie", Toast.LENGTH_SHORT).show();
                String nazwa = generujNazwe("jpg", "zdjecie");
                ostatni_plik = new File(path, nazwa);
                try {
                    ostatni_plik.createNewFile();
                    System.out.println(ostatni_plik.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result.toFile(ostatni_plik, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {

                    }
                });
                fbfilm.setVisibility(FloatingActionButton.VISIBLE);
                fbzdjecie.setVisibility(FloatingActionButton.VISIBLE);

                File kopiaPliku = new File(ostatni_plik.getPath());
                new DodajPlikZAparatu(AparatActivity.this, kopiaPliku, nazwaNotatki).execute();
                nowePliki.add(kopiaPliku);

            }

            @Override
            public void onVideoTaken(VideoResult result) {
                fbfilm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCzekania)));
                fbzdjecie.setVisibility(FloatingActionButton.VISIBLE);

                File kopiaPliku = new File(ostatni_plik.getPath());
                new DodajPlikZAparatu(AparatActivity.this, kopiaPliku, nazwaNotatki).execute();
                nowePliki.add(kopiaPliku);
            }

            // And much more
        });

        fbzdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!camera.isTakingVideo() && !camera.isTakingPicture()){
                    camera.setMode(Mode.PICTURE);
                    fbfilm.setVisibility(FloatingActionButton.INVISIBLE);
                    fbzdjecie.setVisibility(FloatingActionButton.INVISIBLE);
                    camera.takePicture();
                }
            }
        });
        fbfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!camera.isTakingVideo() && !camera.isTakingPicture()){
                    camera.setMode(Mode.VIDEO);
                    fbfilm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    fbzdjecie.setVisibility(FloatingActionButton.INVISIBLE);
                    String nazwa = generujNazwe("mp4", "film");

                    ostatni_plik = new File(path, nazwa);
                    try {
                        ostatni_plik.createNewFile();
                        System.out.println(ostatni_plik.getAbsolutePath());
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
                notatkiDb.aparatDAO().insertZdjecie(new AparatEntity(path, nazwa));
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
}
