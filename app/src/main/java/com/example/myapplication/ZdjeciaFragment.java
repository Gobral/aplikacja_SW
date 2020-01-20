package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZdjeciaFragment extends Fragment {

    private List<AparatEntity> lista_plikow = new ArrayList<>();
    public RecyclerView.LayoutManager layoutManager;
    public AdapterPlikowZAparatu mAdapter;
    private NotatkaEntity notatka;

    public ZdjeciaFragment(NotatkaEntity notatkaEntity){
        notatka = notatkaEntity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_zdjecia, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.pliki_z_aparatu_grid);

        LinearLayout lwew = rootView.findViewById(R.id.zdjecia_layout);
        layoutManager = new LinearLayoutManager(lwew.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPlikowZAparatu(lista_plikow, recyclerView, getContext());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Observer<List<AparatEntity>> plikiObserver = new Observer<List<AparatEntity>>() {
            @Override
            public void onChanged(@Nullable final List<AparatEntity> updatedPliki) {
                lista_plikow = updatedPliki;
                mAdapter.setData(lista_plikow);
            }
        };

        LiveData notatkiLiveData = NotatkaDatabaseAccessor.getInstance(getActivity().getApplicationContext()).aparatDAO().findPowiazane(notatka.getNazwaNotatki());
        notatkiLiveData.observe(this, plikiObserver);
    }

}
