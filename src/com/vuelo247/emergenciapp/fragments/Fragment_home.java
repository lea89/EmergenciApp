package com.vuelo247.emergenciapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;

public class Fragment_home extends Fragment{
	public static final String ARG_ARTICLES_NUMBER = "articles_number";
	
	TextView text_2;
	TextView txt_centros;
    public Fragment_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    
        int i = getArguments().getInt(ARG_ARTICLES_NUMBER);
        
        String article = getResources().getStringArray(R.array.Telefonos)[i];

        getActivity().setTitle(article);

        txt_centros = (TextView)rootView.findViewById(R.id.txt_centros);
        text_2 = (TextView)rootView.findViewById(R.id.text_2);

        txt_centros.setText("Mostrar centros");
        text_2.setText("Centro mas cercano");

        return rootView;
    }
}
