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
import android.content.Intent;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location location;
    private Marker user;
    private Marker nowy;
    private AdapterPodgladuParkingu mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton blokada;
    private List<ParkingEntity> lista_parkingow = new ArrayList<>();
    private List<Marker> markery = new ArrayList<>();
    private List<Marker> dzienniki = new ArrayList<>();
    private List<NotatkaEntity> notatki = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        final Observer<List<ParkingEntity>> notatkaObserver = new Observer<List<ParkingEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ParkingEntity> updatedParkingi) {
                for(Marker m: markery){
                    m.remove();
                }
                lista_parkingow = updatedParkingi;
                mAdapter.setData(lista_parkingow);

                for(ParkingEntity parkingEntity: lista_parkingow){
                    markery.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(parkingEntity.getLattitude(), parkingEntity.getLongitude()))
                            .title(parkingEntity.getNazwaParkingu())));
                }

            }
        };

        LiveData notatkiLiveData = NotatkaDatabaseAccessor.getInstance(this.getApplicationContext()).parkingDAO().findAll();
        notatkiLiveData.observe(this, notatkaObserver);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View mapView = mapFragment.getView();
        View compassButton = mapView.findViewWithTag("GoogleMapCompass");
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_END);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START,0);
        rlp.topMargin = 150;
        rlp.rightMargin = 35;

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

        mAdapter = new AdapterPodgladuParkingu(lista_parkingow, recyclerView, this, mMap);
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
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0)) && nowy != null){
                            new DodanieAsyncTask(MapsActivity.this , wpisano, nowy.getPosition()).execute();
                            nowy.remove();
                            nowy = null;
                        }
                        else{
                            Toast.makeText(MapsActivity.this, "Podaj poprawną nazwę i zaznacz parking na mapie", Toast.LENGTH_LONG).show();
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

        final Observer<List<NotatkaEntity>> notatkaMapObserver = new Observer<List<NotatkaEntity>>() {
            @Override
            public void onChanged(@Nullable final List<NotatkaEntity> updatedNotatki) {
                notatki = updatedNotatki;
                for(Marker d: dzienniki){
                    d.remove();
                }
                for(NotatkaEntity n: updatedNotatki){
                    dzienniki.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(n.getLattitude(), n.getLongitude()))
                            .title(n.getNazwaNotatki())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                }
            }
        };

        LiveData notatkiMapsLiveData = NotatkaDatabaseAccessor.getInstance(this.getApplicationContext()).notatkiDAO().findAll();
        notatkiMapsLiveData.observe(this, notatkaMapObserver);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                System.out.println("Klik");
                if(dzienniki.contains(arg0)){
                    Intent intent = new Intent(MapsActivity.this, Notes2Activity.class);
                    intent.putExtra("nazwa", arg0.getTitle());
                    MapsActivity.this.startActivity(intent);
                }

            }
        });

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.11, 17.022222);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMinZoomPreference(5.0f);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mAdapter.setMapa(mMap);

        user = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Tu jesteś").icon(BitmapDescriptorFactory.fromResource(R.drawable.user25)));


        /*
        for(Marker d: dzienniki){
            d.remove();
        }
        for(NotatkaEntity n: notatki){
            System.out.println("DODAJE " + n.getLattitude() + n.getLongitude());
            dzienniki.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(n.getLattitude(), n.getLongitude()))
                    .title(n.getNazwaNotatki())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
        }
         */
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(nowy == null){
            nowy = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Kliknij + aby dodać nowy parking")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        else {
            nowy.setPosition(latLng);
        }

    }

    private class DodanieAsyncTask  extends AsyncTask<Void, Void, ParkingEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private String nazwaParkingu;
        private LatLng latLng;

        public DodanieAsyncTask(Activity activity, String nazwaParkingu, LatLng latLng) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaParkingu = nazwaParkingu;
            this.latLng = latLng;
        }

        @Override
        protected ParkingEntity doInBackground(Void... params) {

            ParkingEntity parkingEntity = null;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                parkingEntity = new ParkingEntity(nazwaParkingu, latLng.latitude, latLng.longitude);
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
