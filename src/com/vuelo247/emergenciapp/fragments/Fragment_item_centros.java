package com.vuelo247.emergenciapp.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.Adaptador_Telefonos;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;

public class Fragment_item_centros extends Fragment
{
private View rootView;
	
	public static Location myLocation;
	
	Centro oCentro;
	
	ArrayList<Centro> list_centros;
	
	int codigo;
	
	double[] coordinatesMyPosition;

	LayoutInflater inflater;
	
	Resources recursos;
	
	ListView listView_telefonos;
	
	TextView txt_item_centro_titulo;
	TextView txt_item_centro_direccion;
	TextView txt_item_centro_colectivos;
	TextView txt_item_centro_email;
	TextView txt_presione_para_llamar;
	
	RelativeLayout relative_item_centro_email;
	
	Button btn_item_ir_centro;
	
	Typeface tf;
	
	OnClickListener onClickEnviarMail;
	OnClickListener onClickIrCentro;
	
	Fragment fragment;
	FragmentManager fragmentManager;
	
	Bundle args;
	
	int position;
	
	public Fragment_item_centros()
	{

	}
	
	public static Fragment_item_centros newInstance(Bundle args,int position)
	{
		Fragment_item_centros f = new Fragment_item_centros();
		
		if(args != null)
		{
			f.setArguments(args);
			f.position = position;
		}
		
		return f;
	}
	
	public Fragment_item_centros(int codigo)
	{
		this.codigo = codigo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_item_centros, container, false);
		
		recursos = getActivity().getResources();
		
		DAOCentros dao_centros = new DAOCentros(getActivity(),"CentrosDB", null, 1);
		
		String fontPath = getActivity().getResources().getString(R.string.font_path_medium);
		 
        tf = Typeface.createFromAsset(getActivity().getResources().getAssets(), fontPath);

        args = getArguments();
        
        if(args != null)
        {
       		oCentro = new Centro();
        	
        	oCentro.setLatitud(args.getString("latitud"));
        	oCentro.setLongitud(args.getString("longitud"));
        	oCentro.setDescripcion(args.getString("nombre"));
        	oCentro.setDireccion(args.getString("direccion"));
        	oCentro.setTelefono(args.getString("telefonos"));
        	oCentro.setColectivos(args.getString("colectivos"));
        	
        	oCentro.setEmail("S/D");

        }
        else
        {
        	oCentro = dao_centros.getCentro(codigo);
        	position = 4;
        }
		
		levantarXML();
		
		setTypeface();
		
		levantarEventos();
		
		setearEventos();
		
		if(!oCentro.getColectivos().equals(""))
		{
			txt_item_centro_colectivos.setText(recursos.getString(R.string.colectivos)+" "+oCentro.getColectivos().toString());
		}
		else
		{
			txt_item_centro_colectivos.setText(recursos.getString(R.string.colectivos));
		}
		
		if(!oCentro.getDireccion().equals(""))
		{
			txt_item_centro_direccion.setText(recursos.getString(R.string.direccion)+" "+oCentro.getDireccion().toString());
		}
		if(!oCentro.getEmail().equals(""))
		{
			txt_item_centro_email.setText(recursos.getString(R.string.email)+" "+oCentro.getEmail().toString());
		}
		if(!oCentro.getDescripcion().equals(""))
		{
			txt_item_centro_titulo.setText(oCentro.getDescripcion().toString());
		}
		
		listView_telefonos = (ListView) rootView.findViewById(R.id.list_item_centro_telefonos);
		
		String[] telefonos = oCentro.getTelefono().split("/");
		
		if(!telefonos[0].equals("S"))
		{
		
			ArrayList<String> list_telefonos = new ArrayList<String>();
		
			for(int i = 0; i < telefonos.length; i++)
			{
				list_telefonos.add(telefonos[i]);
			}
		
			Adaptador_Telefonos adapter = new Adaptador_Telefonos(getActivity(), list_telefonos, getActivity().getSupportFragmentManager());
		
			listView_telefonos.setAdapter(adapter);
		}
		
		txt_item_centro_titulo.setTypeface(tf);
		
		this.inflater = inflater;

		return rootView;
	}
	
	private void setearEventos() 
	{
		btn_item_ir_centro.setOnClickListener(onClickIrCentro);
	}

	public void levantarXML()
	{
		txt_item_centro_titulo = (TextView) rootView.findViewById(R.id.txt_item_centro_titulo);
		txt_item_centro_direccion = (TextView) rootView.findViewById(R.id.txt_item_centro_direccion);
		txt_item_centro_colectivos = (TextView) rootView.findViewById(R.id.txt_item_centro_colectivos);
		txt_item_centro_email = (TextView) rootView.findViewById(R.id.txt_item_centro_email);
		txt_presione_para_llamar = (TextView) rootView.findViewById(R.id.txt_presione_para_llamar);
		
		relative_item_centro_email = (RelativeLayout) rootView.findViewById(R.id.relative_item_centro_email);
		
		btn_item_ir_centro = (Button) rootView.findViewById(R.id.btn_item_ir_centro);
	}
	
	public void setTypeface()
	{
		txt_item_centro_titulo.setTypeface(tf);
		txt_item_centro_direccion.setTypeface(tf);
		txt_item_centro_colectivos.setTypeface(tf);
		txt_item_centro_email.setTypeface(tf);
		txt_presione_para_llamar.setTypeface(tf);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		recursos = getActivity().getResources();
		
		txt_item_centro_email.setOnClickListener(onClickEnviarMail);
		
		relative_item_centro_email.setOnClickListener(onClickEnviarMail);
		
		args = getArguments();
		
	}
	
	 protected void enviarMail() {

	      String[] TO = {txt_item_centro_email.getText().toString().replace("Email: ","")};
	      String[] CC = {};
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setType("message/rfc822");


	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_CC, CC);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
	      emailIntent.putExtra(Intent.EXTRA_TEXT, "");

	      try {
	         startActivity(Intent.createChooser(emailIntent, "Enviar e-mail"));
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(getActivity(), 
	         "No tiene aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show();
	      }
	   }
	 
	 private void levantarEventos()
	 {
		 onClickEnviarMail = new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					enviarMail();
				}
			};
			
		onClickIrCentro = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				args = new Bundle();
				args.putString("centro", "otroCentro");
				args.putString("direccion", oCentro.getDireccion());
				args.putString("ubicacion", oCentro.getUbicacion());
				args.putString("descripcion", oCentro.getDescripcion());
				args.putString("latitudDestino", oCentro.getLatitud());
				args.putString("longitudDestino", oCentro.getLongitud());
				
				fragment = Fragment_mapa.newInstance(args,position);
				
				fragmentManager = getActivity().getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
		};
	 }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
}
