package com.vuelo247.emergenciapp.adaptadorDrawer;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.entidades.MyMarker;

public class MyInfoWindowAdapter implements InfoWindowAdapter {

	Activity act;
	
	String titulo;
	String descripcion;
	
	private HashMap<Marker, MyMarker> mMarkersHashMap;

	Marker markerHIGA;
	
	public MyInfoWindowAdapter(Activity act,String title, String descripcion)
    {
		this.act = act;
		this.titulo = title;
		this.descripcion = descripcion;
    }
	
	public MyInfoWindowAdapter(Activity act,HashMap<Marker, MyMarker> mMarkersHashMap)
    {
		this.act = act;
		this.mMarkersHashMap = mMarkersHashMap;
    }
	
	public MyInfoWindowAdapter(Activity act,HashMap<Marker, MyMarker> mMarkersHashMap,Marker markerHIGA)
    {
		this.act = act;
		this.mMarkersHashMap = mMarkersHashMap;
		this.markerHIGA = markerHIGA;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        View v  = act.getLayoutInflater().inflate(R.layout.info_window, null);

        MyMarker myMarker = mMarkersHashMap.get(marker);


        TextView markerLabel = (TextView)v.findViewById(R.id.descripcion);

        TextView anotherLabel = (TextView)v.findViewById(R.id.title);

        if(myMarker != null)
        {
        	markerLabel.setText(myMarker.getDescripcion());
        	anotherLabel.setText(myMarker.getUbicacion());
        }
        

        return v;
    }
}