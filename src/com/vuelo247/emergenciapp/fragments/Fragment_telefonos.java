package com.vuelo247.emergenciapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.ListAdapterTelefono;
import com.vuelo247.emergenciapp.entidades.Telefono;

public class Fragment_telefonos extends Fragment
{
	 public static final String ARG_ARTICLES_NUMBER = "articles_number";

	    public Fragment_telefonos() {
	        // Constructor vacío obligatorio
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	    {
	        View rootView = inflater.inflate(R.layout.fragment_telefonos, container, false);
	    
	        int i = getArguments().getInt(ARG_ARTICLES_NUMBER);
	        
	        String article = getResources().getStringArray(R.array.Telefonos)[i];

	        getActivity().setTitle(article);
	        String[] tagTelefonos = getResources().getStringArray(R.array.Telefonos);
			
			ArrayList<Telefono> telefonos = new ArrayList<Telefono>();
			
			telefonos.add(new Telefono(tagTelefonos[0]));
			telefonos.add(new Telefono(tagTelefonos[1]));
			telefonos.add(new Telefono(tagTelefonos[2]));
			telefonos.add(new Telefono(tagTelefonos[3]));
			telefonos.add(new Telefono(tagTelefonos[4]));
			telefonos.add(new Telefono(tagTelefonos[5]));
			
	        ListAdapterTelefono adapter = new ListAdapterTelefono(getActivity(), telefonos);
	        ListView list_telefonos = (ListView) rootView.findViewById(R.id.list_telefonos);
	        list_telefonos.setAdapter(adapter);
//	        TextView headline = (TextView)rootView.findViewById(R.id.txt_nombre_item_telefono);
//	        
//	        headline.append(" "+article);

	        return rootView;
	    }
}
