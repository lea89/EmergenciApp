package com.vuelo247.emergenciapp.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;

public class Fragment_home extends Fragment {

	TextView txt_informacion_util;
	TextView txt_centro_mas_cercano;
	TextView txt_higa;
	TextView txt_hiemi;
	TextView txt_sub_hiemi;
	TextView txt_sub_higa;

	RelativeLayout relative_higa;
	RelativeLayout relative_hiemi;
	RelativeLayout relative_centro_mas_cercano;
	RelativeLayout relative_informacion_util;

	ImageButton img_higa;
	ImageButton img_hiemi;
	ImageButton img_centro_mas_cercano;
	ImageButton img_informacion_util;

	OnClickListener onClickHIGA;
	OnClickListener onClickHIEMI;
	OnClickListener onClickInformacionUtil;
	OnClickListener onClickCentroMasCercano;

	Fragment fragment;

	FragmentManager fragmentManager;

	Context context;

	Typeface tf;

	View rootView;
	
	ArrayList<Centro> list_centros;
	DAOCentros dao_centros;

	public Fragment_home(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		String fontPath = context.getResources().getString(R.string.font_path_medium);

		tf = Typeface.createFromAsset(context.getAssets(), fontPath);

		levantarXML();

		setTypeface();

		levantarEventos();

		setearEventos();

		return rootView;
	}

	private void levantarEventos() {
		onClickHIGA = new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragment = new Fragment_mapa(0);

				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		};

		onClickHIEMI = new OnClickListener() {

			@Override
			public void onClick(View v) {

				fragment = new Fragment_mapa(1);

				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

			}
		};
		
		onClickCentroMasCercano = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dao_centros = new DAOCentros(getActivity(),"CentrosDB", null, 1);

				list_centros = dao_centros.getCentros();
				
				if(list_centros.size() > 0)
				{
					fragment = new Fragment_mapa(2);

					fragmentManager = getActivity().getSupportFragmentManager();

					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
				}
				else
				{
					Toast.makeText(getActivity(),context.getResources().getString(R.string.actualizar_centros_reintentar),
							Toast.LENGTH_SHORT).show();
				}

			}
		};
		
		onClickInformacionUtil = new OnClickListener() {

			@Override
			public void onClick(View v) {

				fragment = new Fragment_centros(3);

				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}

		};
	}

	private void setTypeface() {
		txt_higa.setTypeface(tf);
		txt_sub_higa.setTypeface(tf);
		txt_hiemi.setTypeface(tf);
		txt_sub_hiemi.setTypeface(tf);
		txt_informacion_util.setTypeface(tf);
		txt_centro_mas_cercano.setTypeface(tf);
	}

	private void levantarXML() 
	{
		txt_higa = (TextView) rootView.findViewById(R.id.txt_higa);
		txt_sub_higa = (TextView) rootView.findViewById(R.id.txt_sub_higa);
		txt_informacion_util = (TextView) rootView.findViewById(R.id.txt_informacion_util);
		txt_centro_mas_cercano = (TextView) rootView.findViewById(R.id.txt_centro_mas_cercano);
		txt_hiemi = (TextView) rootView.findViewById(R.id.txt_hiemi);
		txt_sub_hiemi = (TextView) rootView.findViewById(R.id.txt_sub_hiemi);

		relative_higa = (RelativeLayout) rootView.findViewById(R.id.relative_higa);
		relative_hiemi = (RelativeLayout) rootView.findViewById(R.id.relative_hiemi);
		relative_centro_mas_cercano = (RelativeLayout) rootView.findViewById(R.id.relative_centro_mas_cercano);
		relative_informacion_util = (RelativeLayout) rootView.findViewById(R.id.relative_informacion_util);

		img_higa = (ImageButton) rootView.findViewById(R.id.img_higa);
		img_hiemi = (ImageButton) rootView.findViewById(R.id.img_hiemi);
		img_informacion_util = (ImageButton) rootView.findViewById(R.id.img_informacion_util);
		img_centro_mas_cercano = (ImageButton) rootView.findViewById(R.id.img_centro_mas_cercano);
	}

	private void setearEventos() 
	{
		relative_higa.setOnClickListener(onClickHIGA);
		relative_hiemi.setOnClickListener(onClickHIEMI);
		relative_centro_mas_cercano.setOnClickListener(onClickCentroMasCercano);
		relative_informacion_util.setOnClickListener(onClickInformacionUtil);
		
		img_higa.setOnClickListener(onClickHIGA);
		img_hiemi.setOnClickListener(onClickHIGA);
		img_centro_mas_cercano.setOnClickListener(onClickCentroMasCercano);
		img_informacion_util.setOnClickListener(onClickInformacionUtil);
	}
}
