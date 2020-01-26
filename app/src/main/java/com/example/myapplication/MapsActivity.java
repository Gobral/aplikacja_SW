package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location location;
    private Marker user;
    private AdapterPodgladuParkingu mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton blokada;
    private List<ParkingEntity> lista_parkingow = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        final Observer<List<ParkingEntity>> notatkaObserver = new Observer<List<ParkingEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ParkingEntity> updatedParkingi) {
                lista_parkingow = updatedParkingi;
                mAdapter.setData(lista_parkingow);
            }
        };

        LiveData notatkiLiveData = NotatkaDatabaseAccessor.getInstance(this.getApplicationContext()).parkingDAO().findAll();
        notatkiLiveData.observe(this, notatkaObserver);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton imageButton = findViewById(R.id.parkingi_menu_button);
        RelativeLayout menu = findViewById(R.id.parkingi_menu);
        FloatingActionButton fb = findViewById(R.id.dodawanie_parkingow_fb);
        blokada = findViewById(R.id.parkingi_center_button);
        blokada.setTag("fixed");
        menu.setVisibility(menu.GONE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.parkingi_recucler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(menu.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuParkingu(lista_parkingow, recyclerView, this);
        recyclerView.setAdapter(mAdapter);

        blokada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("KLIK "+ blokada.getDrawable().getConstantState() + " " + getResources().getDrawable(R.drawable.ic_gps_fixed_black_24dp).getConstantState() );
                if(blokada.getTag().toString().equals("fixed")){
                    blokada.setImageDrawable(getResources().getDrawable(R.drawable.ic_gps_not_fixed_black_24dp));
                    blokada.setTag("notfixed");
                }
                else{
                    blokada.setImageDrawable(getResources().getDrawable(R.drawable.ic_gps_fixed_black_24dp));
                    blokada.setTag("fixed");
                }
            }
        });

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(600);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(600);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu.getVisibility() == menu.VISIBLE){
                    menu.startAnimation(fadeOut);
                    //menu.setVisibility(menu.GONE);
                }
                else {
                    menu.startAnimation(fadeIn);
                    //menu.setVisibility(menu.VISIBLE);
                }
            }
        });

        GoogleApiAvailability availability
                = GoogleApiAvailability.getInstance();
        int result = availability.isGooglePlayServicesAvailable(this);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                5);
        if (result != ConnectionResult.SUCCESS) {
            if (!availability.isUserResolvableError(result)) {
                Toast.makeText(this, "Przyznaj uprawnienia GPS!", Toast.LENGTH_LONG).show();
            }
        }
        location = new Location("domyslne");
        location.setLongitude(17.022222);
        location.setLatitude(51.11);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = new Location(locationResult.getLastLocation());
                if (location != null) {
                    if (mMap != null) {
                        LatLng latLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

                        user.setPosition(latLng);
                        if(blokada.getTag().toString().equals("fixed")){
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }
                }
            }
        };

        FusedLocationProviderClient fusedLocationClient = null;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(500); // Update every 5 seconds.
        fusedLocationClient.requestLocationUpdates(request, mLocationCallback, null);


        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = MapsActivity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę parkingu");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){
                            new MapsActivity.DodanieAsyncTask(MapsActivity.this , wpisano).execute();
                        }
                        else{
                            Toast.makeText(MapsActivity.this, "Podaj poprawną nazwę", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.11, 17.022222);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMinZoomPreference(5.0f);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        user = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Tu jesteś").icon(BitmapDescriptorFactory.fromResource(R.drawable.user25)));
    }

    private class DodanieAsyncTask  extends AsyncTask<Void, Void, ParkingEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private String nazwaParkingu;

        public DodanieAsyncTask(Activity activity, String nazwaParkingu) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaParkingu = nazwaParkingu;
        }

        @Override
        protected ParkingEntity doInBackground(Void... params) {

            ParkingEntity parkingEntity = null;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                Calendar data = Calendar.getInstance();
                data.add(Calendar.YEAR, 1900);

                parkingEntity = new ParkingEntity(nazwaParkingu, location.getLatitude(), location.getLongitude());
                notatkiDb.parkingDAO().insertParking(parkingEntity);
            }
            catch (Exception e){
                parkingEntity = null;
            }
            return parkingEntity;
        }

        @Override
        protected void onPostExecute(ParkingEntity notatkaEntity) {
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
