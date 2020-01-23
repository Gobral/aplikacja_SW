package com.example.myapplication;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextEditFragment extends Fragment {

    private List<WpisEntity> lista_wpisow = new ArrayList<>();
    public RecyclerView.LayoutManager layoutManager;
    private AdapterPodgladuWpisu mAdapter;
    private NotatkaEntity notatkaEntity;

    public  TextEditFragment(NotatkaEntity notatkaEntity){
        super();
        this.notatkaEntity = notatkaEntity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_edit, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wpisy_recycler_view);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new AdapterPodgladuWpisu(lista_wpisow, recyclerView, getContext(), notatkaEntity);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Observer<List<WpisEntity>> plikiObserver = new Observer<List<WpisEntity>>() {
            @Override
            public void onChanged(@Nullable final List<WpisEntity> updatedPliki) {
                lista_wpisow = updatedPliki;
                mAdapter.setData(lista_wpisow);
            }
        };

        LiveData wpisyiLiveData = NotatkaDatabaseAccessor.getInstance(getActivity().getApplicationContext()).wpisyDAO().findPowiazane(notatkaEntity.getNazwaNotatki());
        wpisyiLiveData.observe(this, plikiObserver);
    }



}
