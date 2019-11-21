package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);


        final FragmentActivity c = getActivity();
        layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);


        // specify an adapter (see also next example)
        String myDataset[] = new String[100];
        for (int i = 0; i < 100; i++){
            myDataset[i] = "test " + i;
        }
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
