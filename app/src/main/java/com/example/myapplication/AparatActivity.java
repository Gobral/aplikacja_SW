package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.File;
import java.io.IOException;

public class AparatActivity extends AppCompatActivity {

    File file;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aparat);
        getSupportActionBar().hide();

        CameraView camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);

        FloatingActionButton fbzdjecie = findViewById(R.id.fbzdjecie);
        FloatingActionButton fbfilm = findViewById(R.id.fbvideo);
        File path = getExternalCacheDir();

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
            }

            @Override
            public void onVideoTaken(VideoResult result) {
                // A Video was taken!
            }

            // And much more
        });

        fbzdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(camera.isTakingVideo());
                if(camera.isTakingVideo() == false) {
                    camera.setMode(Mode.PICTURE);
                    camera.takePicture();
                }
            }
        });
        fbfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera.isTakingVideo() == false){
                    camera.setMode(Mode.VIDEO);
                    file = new File(path, "DemoPicture.mp4");
                    try {
                        file.createNewFile();
                        System.out.println(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.takeVideo(file);
                }
                else {
                    camera.stopVideo();
                }
            }
        });
    }
}
