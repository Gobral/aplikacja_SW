package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WideoFragment extends Fragment {


    private List<FilmEntity> lista_plikow = new ArrayList<>();
    public RecyclerView.LayoutManager layoutManager;
    public AdapterPodgladuFilmu mAdapter;
    private NotatkaEntity notatka;

    public WideoFragment(NotatkaEntity notatkaEntity){
        notatka = notatkaEntity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Observer<List<FilmEntity>> plikiObserver = new Observer<List<FilmEntity>>() {
            @Override
            public void onChanged(@Nullable final List<FilmEntity> updatedPliki) {
                lista_plikow = updatedPliki;
                mAdapter.setData(lista_plikow);
            }
        };

        LiveData notatkiLiveData = NotatkaDatabaseAccessor.getInstance(getActivity().getApplicationContext()).filmDAO().findPowiazane(notatka.getNazwaNotatki());
        notatkiLiveData.observe(this, plikiObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wideo, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.pliki_z_aparatu_grid);

        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuFilmu(lista_plikow, recyclerView, getContext());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
