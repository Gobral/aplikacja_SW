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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NagraniaFragment extends Fragment {

    private List<GlosoweEntity> lista_nagran = new ArrayList<>();
    public RecyclerView.LayoutManager layoutManager;
    private AdapterPodgladuNagrania mAdapter;
    private NotatkaEntity notatkaEntity;

    public NagraniaFragment(NotatkaEntity notatkaEntity){
        super();
        this.notatkaEntity = notatkaEntity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.nagrania_recycler_view);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuNagrania(lista_nagran, recyclerView, getContext(), notatkaEntity);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Observer<List<GlosoweEntity>> plikiObserver = new Observer<List<GlosoweEntity>>() {
            @Override
            public void onChanged(@Nullable final List<GlosoweEntity> updatedPliki) {
                lista_nagran = updatedPliki;
                mAdapter.setData(lista_nagran);
            }
        };

        LiveData wpisyiLiveData = NotatkaDatabaseAccessor.getInstance(getActivity().getApplicationContext()).glosoweDAO().findPowiazane(notatkaEntity.getNazwaNotatki());
        wpisyiLiveData.observe(this, plikiObserver);
    }

}
