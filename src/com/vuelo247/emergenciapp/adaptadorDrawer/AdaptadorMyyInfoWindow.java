package com.vuelo247.emergenciapp.adaptadorDrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.vuelo247.emergenciapp.R;

public class AdaptadorMyyInfoWindow implements InfoWindowAdapter
{
	LayoutInflater inflater = null;

	int position;
	
	public AdaptadorMyyInfoWindow(LayoutInflater inflater,int position)
	{
		this.inflater = inflater;
		this.position = position;
	}

	@Override
	public View getInfoWindow(Marker marker)
	{
		View popup = inflater.inflate(R.layout.info_window, null);
		
		TextView markerLabel = (TextView) popup.findViewById(R.id.descripcion);

        TextView anotherLabel = (TextView) popup.findViewById(R.id.title);

       	String snippet = marker.getSnippet();
       	
       	markerLabel.setText(snippet.split("#")[1]);
       	
       	anotherLabel.setText(snippet.split("#")[0]);
		
		LinearLayout linear_info = (LinearLayout) popup.findViewById(R.id.linear_info);
		
		switch(position)
		{
			case 0:
				linear_info.setBackgroundColor(popup.getResources().getColor(R.color.verde_nombre));
				break;
			case 1:
				linear_info.setBackgroundColor(popup.getResources().getColor(R.color.verde_nombre));
				break;
			case 2:
				linear_info.setBackgroundColor(popup.getResources().getColor(R.color.rojo));
				break;
			case 3:
				linear_info.setBackgroundColor(popup.getResources().getColor(R.color.rojo));
				break;
		}
		
		
		return (popup);
	}

	@Override
	public View getInfoContents(Marker marker)
	{
		return (null);
	}

}
