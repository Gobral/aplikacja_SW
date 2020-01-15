package com.example.myapplication;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextEditFragment extends Fragment {

    EditText editText;
    NotatkaEntity notatkaEntity;
    String lokalny_tekst;
    public  TextEditFragment(NotatkaEntity notatkaEntity){
        super();
        this.notatkaEntity = notatkaEntity;
        this.lokalny_tekst = notatkaEntity.getZawartosc();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_edit, container, false);
        editText = view.findViewById(R.id.plain_text_input);
        editText.setText(lokalny_tekst);

        return view;
    }



}
