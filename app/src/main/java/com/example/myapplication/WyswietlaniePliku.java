package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class WyswietlaniePliku extends AppCompatActivity {
    private AparatEntity plik;
    private String path;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        path = b.getString("path");

        file = new File(path);
        /*
        try {
            plik = new WczytajPlik(this, path).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wyswietlanie_pliku);
        getSupportActionBar().hide();

        RelativeLayout constraintLayout = findViewById(R.id.wyswietlanie_layout);
        if(path.charAt(path.length() - 1) == 'g') {
            TouchImageView touchImageView = new TouchImageView(this);
            touchImageView.setLayoutParams((new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)));
            touchImageView.setImageURI(Uri.fromFile(file));
            constraintLayout.addView(touchImageView);
        }
        else if(path.charAt(path.length() - 1) == '4') {
            System.out.println(path);
            VideoView videoView = new VideoView(this);
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            videoView.setLayoutParams(layoutParams);
            videoView.setVideoURI(Uri.fromFile(file));

            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            constraintLayout.addView(videoView);
            videoView.requestFocus();
            videoView.start();
        }

    }

    private class WczytajPlik extends AsyncTask<Void, Void, AparatEntity> {

        private WeakReference<Activity> weakActivity;
        private String nazwa;
        private Context context;

        public WczytajPlik(Activity activity, String nazwa) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwa = nazwa;
        }

        @Override
        protected AparatEntity doInBackground(Void... params) {

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            AparatEntity ae = null;
            try {
                ae = notatkiDb.aparatDAO().loadAparatByPath(nazwa);
            }
            catch (Exception e){

            }
            return ae;
        }

    }
}
