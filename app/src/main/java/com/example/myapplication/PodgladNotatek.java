package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PodgladNotatek extends Fragment {

    private List<NotatkaEntity> lista_notatek = new ArrayList<>();
    private AdapterPodgladuNotatki mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_podglad_notatek, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.notatki_recycler_view);
        recyclerView.setHasFixedSize(true);

        CoordinatorLayout lwew = rootView.findViewById(R.id.notatki_podglad_layout);
        layoutManager = new LinearLayoutManager(lwew.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuNotatki(lista_notatek, recyclerView, getContext());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fb = rootView.findViewById(R.id.dodawanie_notatek_fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = PodgladNotatek.this.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Podaj nazwę notatki");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String wpisano = input.getText().toString();
                        if(wpisano.length() > 0 && !Character.isWhitespace(wpisano.charAt(0))){
                            new PodgladNotatek.DodanieAsyncTask(PodgladNotatek.this.getActivity() , wpisano).execute();
                        }
                        else{
                            Toast.makeText(PodgladNotatek.this.getActivity(), "Podaj poprawną nazwę", Toast.LENGTH_LONG).show();
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

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Observer<List<NotatkaEntity>> notatkaObserver = new Observer<List<NotatkaEntity>>() {
            @Override
            public void onChanged(@Nullable final List<NotatkaEntity> updatedNotatki) {
                lista_notatek = updatedNotatki;
                mAdapter.setData(lista_notatek);
                System.out.println(lista_notatek.size());
            }
        };

        LiveData notatkiLiveData = NotatkaDatabaseAccessor.getInstance(getActivity().getApplicationContext()).notatkiDAO().findAll();
        notatkiLiveData.observe(this, notatkaObserver);
    }

    private class DodanieAsyncTask  extends AsyncTask<Void, Void, NotatkaEntity> {

        private WeakReference<Activity> weakActivity;
        private Context context;
        private String nazwaNotatki;

        public DodanieAsyncTask(Activity activity, String nazwaNotatki) {
            weakActivity = new WeakReference<>(activity);
            this.context = activity.getApplicationContext();
            this.nazwaNotatki = nazwaNotatki;
        }

        @Override
        protected NotatkaEntity doInBackground(Void... params) {

            NotatkaEntity notatkaEntity = null;

            NotatkiDatabase notatkiDb = NotatkaDatabaseAccessor.getInstance(context);
            try {
                Calendar data = Calendar.getInstance();
                data.add(Calendar.YEAR, 1900);

                notatkaEntity = new NotatkaEntity(nazwaNotatki, data.getTime() , "");
                notatkiDb.notatkiDAO().insertNatatka(notatkaEntity);
            }
            catch (Exception e){
                notatkaEntity = null;
            }
            return notatkaEntity;
        }

        @Override
        protected void onPostExecute(NotatkaEntity notatkaEntity) {
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
