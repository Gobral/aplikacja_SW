package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class PodgladNotatek extends Fragment {

    private RecyclerView recyclerView;
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

        LinearLayout lwew = rootView.findViewById(R.id.notatki_podglad_layout);
        layoutManager = new LinearLayoutManager(lwew.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuNotatki(lista_notatek, recyclerView, getContext());
        recyclerView.setAdapter(mAdapter);


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



}
