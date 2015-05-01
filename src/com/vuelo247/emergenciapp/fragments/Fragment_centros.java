package com.vuelo247.emergenciapp.fragments;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.Adaptador_ListCentros;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;

public class Fragment_centros extends Fragment
{
	private View rootView;
	
	public static Location myLocation;
	
	Centro oCentro;
	
	ArrayList<Centro> list_centros;
	
	int position;
	
	double[] coordinatesMyPosition;

	LayoutInflater inflater;
	
	Resources recursos;
	
	ListView listView_centros;
	
	TextView txt_centros_informacion;
	TextView txt_centros_higa;
	TextView txt_centros_hiemi;
	
	RelativeLayout relative_centros_higa;
	RelativeLayout relative_centros_hiemi;
	
	OnClickListener onClickHIGA;
	OnClickListener onClickHIEMI;
	
	Typeface tf;
	
	Bundle args;
	
	Fragment fragment;
	FragmentManager fragmentManager;
	
	public Fragment_centros()
	{

	}
	
	public Fragment_centros(int position)
	{
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_centros, container, false);
		
		DAOCentros dao_centros = new DAOCentros(getActivity(),"CentrosDB", null, 1);
		
		String fontPath = getActivity().getResources().getString(R.string.font_path_medium);
		 
        tf = Typeface.createFromAsset(getActivity().getResources().getAssets(), fontPath);


        
		ArrayList<Centro> list_centros = dao_centros.getCentros();
		
		Adaptador_ListCentros adapter = new Adaptador_ListCentros(getActivity(), list_centros,getActivity().getSupportFragmentManager());
		
		levantarXML();
		levantarEventos();
		setearEventos();
        
		txt_centros_informacion.setTypeface(tf);
		
		listView_centros.setAdapter(adapter);
		this.inflater = inflater;

		return rootView;
	}
	

	
	private void setearEventos() 
	{
		relative_centros_hiemi.setOnClickListener(onClickHIEMI);
		relative_centros_higa.setOnClickListener(onClickHIGA);
		
		txt_centros_hiemi.setOnClickListener(onClickHIEMI);
		txt_centros_higa.setOnClickListener(onClickHIGA);
	}

	private void levantarEventos() 
	{
		onClickHIGA = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				args = new Bundle();
				
				args.putString("nombre",recursos.getString(R.string.higa));
				args.putString("latitud",recursos.getString(R.string.latitudHIGA));
				args.putString("longitud",recursos.getString(R.string.longitudHIGA));
				args.putString("direccion",recursos.getString(R.string.direccion_higa));
				args.putString("telefonos", recursos.getString(R.string.telefonos_higa));
				args.putString("colectivos", recursos.getString(R.string.colectivos_higa));
				
				fragment = Fragment_item_centros.newInstance(args,0);
				
				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		};
		
		onClickHIEMI = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				args = new Bundle();
				
				args.putString("nombre",recursos.getString(R.string.hiemi));
				args.putString("latitud",recursos.getString(R.string.latitudMaterno));
				args.putString("longitud",recursos.getString(R.string.longitudMaterno));
				args.putString("direccion",recursos.getString(R.string.direccion_hiemi));
				args.putString("telefonos", recursos.getString(R.string.telefonos_hiemi));
				args.putString("colectivos", recursos.getString(R.string.colectivos_hiemi));
				
				fragment = Fragment_item_centros.newInstance(args,1);
				
				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		};
	}

	private void levantarXML() 
	{
		listView_centros = (ListView) rootView.findViewById(R.id.list_centros);
		
		txt_centros_informacion = (TextView) rootView.findViewById(R.id.txt_centros_informacion);
		txt_centros_hiemi = (TextView) rootView.findViewById(R.id.txt_centros_hiemi);
		txt_centros_higa = (TextView) rootView.findViewById(R.id.txt_centros_higa);
		
		relative_centros_higa = (RelativeLayout) rootView.findViewById(R.id.relative_centros_higa);
		relative_centros_hiemi = (RelativeLayout) rootView.findViewById(R.id.relative_centros_hiemi);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		recursos = getActivity().getResources();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
}
