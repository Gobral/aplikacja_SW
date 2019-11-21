package com.example.myapplication;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Wpis> naglowki = new ArrayList<>();;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayout lwew = rootView.findViewById(R.id.wew_news_layout);
        //final FragmentActivity c = getActivity();
        layoutManager = new LinearLayoutManager(lwew.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(naglowki, getContext());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Content content = new Content();
        content.execute();
    }



    private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://slaskwroclaw.pl/strona/articles/index/1";
            try {
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("div.news-box");
                int size = data.size();
                Log.d("size", " " + size);
                for (int i = 0; i < size; i++){
                    String imgUrl = data.select("div.news-box").select("img").eq(i).attr("src");
                    String title = data.select("h3.font-color-red").select("a").eq(i).text();
                    String wpisUrl = data.select("h3.font-color-red").select("a").eq(i).attr("href");
                    Log.d("zapytanie", "artyk: " + title);
                    naglowki.add(new Wpis(wpisUrl, imgUrl, title));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
