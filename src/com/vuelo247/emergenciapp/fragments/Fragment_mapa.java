package com.vuelo247.emergenciapp.fragments;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.MyInfoWindowAdapter;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.entidades.MyMarker;
import com.vuelo247.emergenciapp.tareas.DrawRoute;



public class Fragment_mapa extends Fragment
{
	private GoogleMap mapa;
	private SupportMapFragment mMap;
	private View rootView;
	
	private LocationManager locationManager;
	private LocationListener listenerGPS;
	private Resources recursos;
	public static Location myLocation;
	
	Centro oCentro;
	
	ArrayList<Centro> list_centros;
	
	int position;
	
	private HashMap<Marker, MyMarker> mMarkersHashMap;
	

    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();

	public Fragment_mapa()
	{

	}
	
	public Fragment_mapa(ArrayList<Centro> list_centros,int position)
	{
		this.list_centros = list_centros;
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		recursos = getActivity().getResources();
		
		mMarkersHashMap = new HashMap<Marker, MyMarker>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setFragment();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		setUpMapa();
		setupGPS();
		
		switch (position) 
		{
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			addMarkers(list_centros);
			break;
		case 4:
			addMarkers(list_centros);
			break;
		default:
			break;
		}
		
		
		super.onResume();
	}

		
	private void setFragment()
	{
		mMap = SupportMapFragment.newInstance();
	
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		
		fragmentTransaction.add(R.id.map_root, mMap);
		
		fragmentTransaction.commit();
	}

	private void setUpMapa()
	{
		mapa = mMap.getMap();

		mapa.setMyLocationEnabled(true);

		mapa.getUiSettings().setRotateGesturesEnabled(true);
		
		mapa.setOnMyLocationChangeListener(new OnMyLocationChangeListener()
		{
			@Override
			public void onMyLocationChange(Location location)
			{
				
			}
		});
		
		mapa.setOnMapLongClickListener(new OnMapLongClickListener()
		{
			@Override
			public void onMapLongClick(LatLng mapPoint)
			{
				
			}
		});
		mapa.setOnMapClickListener(new OnMapClickListener()
		{
			public void onMapClick(LatLng punto)
			{

			}
		});
		mapa.setOnMarkerClickListener(new OnMarkerClickListener()
		{
			@Override
			public boolean onMarkerClick(Marker marker)
			{
				marker.showInfoWindow();
				return true;
				
			}
		});
		mapa.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			@Override
			public void onInfoWindowClick(Marker arg0)
			{
				
			}
		});
	
		mapa.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			
			@Override
			public boolean onMyLocationButtonClick() {

				LatLng ubicacion = new LatLng(mapa.getMyLocation().getLatitude(),
						mapa.getMyLocation().getLongitude());
				
				switch(position)
				{
					case 0:
					{
						double latitud = Double.parseDouble("-37.9932584");
						double longitud = Double.parseDouble("-57.6012754");
						
						LatLng ubicacionHIGA = new LatLng(latitud,longitud);
						
						Marker m = mapa.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.cruz_roja)).position(ubicacionHIGA));
						
						new DrawRoute(ubicacion, ubicacionHIGA, mapa, getActivity()).execute();
						break;
					}
					case 1:
					{
						double latitud = Double.parseDouble("-38.0062782");
						double longitud = Double.parseDouble("-57.5620937");
						
						LatLng ubicacionHIGA = new LatLng(latitud,longitud);
						
						Marker m = mapa.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.cruz_roja)).position(ubicacionHIGA));
						
						new DrawRoute(ubicacion, ubicacionHIGA, mapa, getActivity()).execute();
						break;
					}
					case 2:
					{
						oCentro = obtenerCentroMasCercano(list_centros, ubicacion.latitude, ubicacion.longitude);
						
						LatLng destino = new LatLng(Double.parseDouble(oCentro.getLatitud()),Double.parseDouble(oCentro.getLongitud()));
						
						new DrawRoute(ubicacion, destino, mapa, getActivity()).execute();
						
						Marker m = mapa.addMarker(new MarkerOptions()
						.position(destino));
						break;
					}
				}
				
//				MyInfoWindowAdapter infoWindowAdapter = new MyInfoWindowAdapter();
				
//				mapa.setInfoWindowAdapter(infoWindowAdapter);
				
				mapa.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
				
				
				
				CameraPosition camPos = new CameraPosition.Builder()
						.target(ubicacion).zoom(14) // Establecemos el zoom en 14
						.build();
				
				CameraUpdate camUpd3 = CameraUpdateFactory
						.newCameraPosition(camPos);
				
				mapa.animateCamera(camUpd3);
				return false;
			}
		});

	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}
	
	private Centro obtenerCentroMasCercano(ArrayList<Centro> list_centros,double latitud, double longitud)
	{
		Centro oCentro = new Centro();
		
		Location locationA = new Location("point A");
		
		locationA.setLatitude(latitud);
		locationA.setLongitude(longitud);
		
		
		double distancia;
		double distanciaMenor = 100000;
		int posCentro = 0;
		
    	for(int i = 0; i < list_centros.size(); i++)
    	{
    		for(int j = 0; j < list_centros.size() ; j++)
	    	{
    			Location locationB = new Location("point B");

        		locationB.setLatitude(Double.parseDouble(list_centros.get(j).getLatitud()));
        		locationB.setLongitude(Double.parseDouble(list_centros.get(j).getLongitud()));

        		distancia = locationA.distanceTo(locationB);
        		
        		if(distancia < distanciaMenor)
        		{
        			distanciaMenor = distancia;
        			posCentro = j;
        		}
	    	}
    	}
    	
    	oCentro = list_centros.get(posCentro);
		
    	return oCentro;
	}
	
	private void setupGPS() {
		String serviceString = Context.LOCATION_SERVICE;
		
		locationManager = (LocationManager) getActivity().getSystemService(serviceString);
		
		listenerGPS = new LocationListener();
		
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 100, listenerGPS);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 100, listenerGPS);
		} else {
			DialogoConfirmacion dialog = new DialogoConfirmacion();
			dialog.show(getActivity().getSupportFragmentManager(),
					recursos.getString(R.string.confirmacion));

		}
	}

	public void addMarkers(ArrayList<Centro> list_centros) 
	{
		
		double latitud;
		double longitud;
		
		LatLng ubicacionHIGA = new LatLng(Double.parseDouble("-37.993360"),Double.parseDouble("-57.601275"));
		
		for(int i = 0; i < list_centros.size(); i++)
		{
			latitud = Double.parseDouble(list_centros.get(i).getLatitud());
			longitud = Double.parseDouble(list_centros.get(i).getLongitud());
			
	        mMyMarkersArray.add(new MyMarker(list_centros.get(i).getDescripcion(), list_centros.get(i).getUbicacion(), latitud, longitud));
			
		}
		
		plotMarkers(mMyMarkersArray);
		
//		Marker markerHIGA = mapa.addMarker(new MarkerOptions()
//		.position(ubicacionHIGA));
//		markerHIGA.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cruz_roja));
		
	}
	
	private void plotMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));
//                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = mapa.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker,myMarker);

                mapa.setInfoWindowAdapter(new MyInfoWindowAdapter(getActivity(),mMarkersHashMap));
            }
        }
    }
	
	

	public class LocationListener implements android.location.LocationListener,
			Listener {
		public void onLocationChanged(Location location) {
			
			LatLng ubicacion = new LatLng(location.getLatitude(),
					location.getLongitude());
//			myLocation = location;
			
			CameraPosition camPos = new CameraPosition.Builder()
					.target(ubicacion).zoom(14) // Establecemos el zoom en 14
					.build();
			
			CameraUpdate camUpd3 = CameraUpdateFactory
					.newCameraPosition(camPos);
			
			mapa.animateCamera(camUpd3);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onGpsStatusChanged(int event) {
		}
	}
	
	public class DialogoConfirmacion extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage(recursos.getString(R.string.confirmacion_gps))
					.setTitle(recursos.getString(R.string.confirmacion))
					.setPositiveButton(recursos.getString(R.string.si),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent callGPSSettingIntent = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(callGPSSettingIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton(recursos.getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									getActivity().finish();
								}
							});

			return builder.create();
		}
	}

}
