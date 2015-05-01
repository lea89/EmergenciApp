package com.vuelo247.emergenciapp.fragments;
import java.util.ArrayList;

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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.AdaptadorMyyInfoWindow;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.miscellaneous.CheckConnection;
import com.vuelo247.emergenciapp.miscellaneous.Util;
import com.vuelo247.emergenciapp.tareas.Tarea_Dibujar_Ruta;



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
	
	double[] coordinatesMyPosition;

	LayoutInflater inflater;
	
	Bundle args;
	
	public Fragment_mapa()
	{

	}
	
	public static Fragment_mapa newInstance(Bundle arguments,int position){
		Fragment_mapa f = new Fragment_mapa();
		
        if(arguments != null){
            f.setArguments(arguments);
            f.position = position;
        }
        return f;
    }
	
	public Fragment_mapa(int position)
	{
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
		this.inflater = inflater;

		return rootView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		recursos = getActivity().getResources();
		args = getArguments();
		
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
		
		DAOCentros dao_centros = new DAOCentros(getActivity(), "CentrosDB", null, 1);
		
		list_centros = dao_centros.getCentros();
		switch (position) 
		{
		case 0:
			coordinatesMyPosition = Util.getGPS(getActivity());
			
			double latitud = Double.parseDouble(recursos.getString(R.string.latitudHIGA));
			double longitud = Double.parseDouble(recursos.getString(R.string.longitudHIGA));
			
			LatLng ubicacionHIGA = new LatLng(latitud,longitud);
			
			LatLng miPosicion = new LatLng(coordinatesMyPosition[0],coordinatesMyPosition[1]);
			
			if(CheckConnection.ConnectTo3G(getActivity()) || CheckConnection.ConnectToWifi(getActivity()))
			{
				new Tarea_Dibujar_Ruta(miPosicion, ubicacionHIGA , mapa, getActivity(),0).execute();
			}
			else
			{
				Toast.makeText(getActivity(), recursos.getString(R.string.sin_conexion), Toast.LENGTH_SHORT).show();
			}
			
			
			mapa.addMarker(new MarkerOptions()
     		.position(new LatLng(ubicacionHIGA.latitude, ubicacionHIGA.longitude))
     		.title(recursos.getString(R.string.higa))
     		.snippet(recursos.getString(R.string.higa)+"#"+recursos.getString(R.string.sub_higa))
     		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_higa)));
	
			 mapa.setInfoWindowAdapter(new AdaptadorMyyInfoWindow(inflater,position));
			break;
		case 1:
			coordinatesMyPosition = Util.getGPS(getActivity());
			
			latitud = Double.parseDouble(recursos.getString(R.string.latitudMaterno));
			longitud = Double.parseDouble(recursos.getString(R.string.longitudMaterno));
			
			LatLng ubicacionMaterno = new LatLng(latitud,longitud);
			
			miPosicion = new LatLng(coordinatesMyPosition[0],coordinatesMyPosition[1]);
			
			mapa.addMarker(new MarkerOptions()
     		.position(new LatLng(ubicacionMaterno.latitude, ubicacionMaterno.longitude))
     		.title(recursos.getString(R.string.hiemi))
     		.snippet(recursos.getString(R.string.hiemi)+"#"+recursos.getString(R.string.sub_hiemi))
     		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hiemi)));
	
			 mapa.setInfoWindowAdapter(new AdaptadorMyyInfoWindow(inflater,position));
			
			if(CheckConnection.ConnectTo3G(getActivity()) || CheckConnection.ConnectToWifi(getActivity()))
			{
			 new Tarea_Dibujar_Ruta(miPosicion, ubicacionMaterno, mapa, getActivity(),1).execute();
			}
			else
			{
				Toast.makeText(getActivity(), recursos.getString(R.string.sin_conexion), Toast.LENGTH_SHORT).show();
			}
			
			
			break;
		case 2:
			
			coordinatesMyPosition = Util.getGPS(getActivity());

			miPosicion = new LatLng(coordinatesMyPosition[0],coordinatesMyPosition[1]);
			
			oCentro = obtenerCentroMasCercano(list_centros, coordinatesMyPosition[0], coordinatesMyPosition[1]);

			LatLng destino = new LatLng(Double.parseDouble(oCentro.getLatitud()),Double.parseDouble(oCentro.getLongitud()));
			
			if(CheckConnection.ConnectTo3G(getActivity()) || CheckConnection.ConnectToWifi(getActivity()))
			{
				new Tarea_Dibujar_Ruta(miPosicion, destino, mapa, getActivity(),2).execute();
			}
			else
			{
				Toast.makeText(getActivity(), recursos.getString(R.string.sin_conexion), Toast.LENGTH_SHORT).show();
			}
			
			mapa.addMarker(new MarkerOptions()
     		.position(new LatLng(destino.latitude, destino.longitude))
     		.title(oCentro.getDescripcion())
     		.snippet(oCentro.getDescripcion()+"#"+oCentro.getDireccion())
     		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_centro)));
	
			 mapa.setInfoWindowAdapter(new AdaptadorMyyInfoWindow(inflater,position));
			break;
		case 3:
			//addMarkers(list_centros);
			break;
		case 4:
			coordinatesMyPosition = Util.getGPS(getActivity());

			miPosicion = new LatLng(coordinatesMyPosition[0],coordinatesMyPosition[1]);
			
			destino = new LatLng(Double.parseDouble(args.getString("latitudDestino")),Double.parseDouble(args.getString("longitudDestino")));
			
			if(CheckConnection.ConnectTo3G(getActivity()) || CheckConnection.ConnectToWifi(getActivity()))
			{
				new Tarea_Dibujar_Ruta(miPosicion, destino, mapa, getActivity(),2).execute();
			}
			else
			{
				Toast.makeText(getActivity(), recursos.getString(R.string.sin_conexion), Toast.LENGTH_SHORT).show();
			}
			
			 mapa.addMarker(new MarkerOptions()
     		.position(new LatLng(destino.latitude, destino.longitude))
     		.snippet(args.getString("descripcion")+"#"+args.getString("direccion"))
     		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_centro)));
	
			 mapa.setInfoWindowAdapter(new AdaptadorMyyInfoWindow(inflater,2));
			break;
		default:
			break;
		}
		
		
		super.onResume();
	}

		
	private void setFragment()
	{
		GoogleMapOptions op = new GoogleMapOptions();
		op.zOrderOnTop(true);
//		SupportMapFragment.newInstance(op);
		
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

				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 1000, 100, listenerGPS);
					LatLng ubicacion = new LatLng(mapa.getMyLocation().getLatitude(),
							mapa.getMyLocation().getLongitude());
					
					mapa.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
					
					
					
					CameraPosition camPos = new CameraPosition.Builder()
							.target(ubicacion).zoom(14) // Establecemos el zoom en 14
							.build();
					
					CameraUpdate camUpd3 = CameraUpdateFactory
							.newCameraPosition(camPos);
					
					mapa.animateCamera(camUpd3);
				} else {
					DialogoConfirmacion dialog = new DialogoConfirmacion();
					dialog.show(getActivity().getSupportFragmentManager(),
							recursos.getString(R.string.dialog_confirmacion));
				}
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
		Centro oCentro;
		
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
					recursos.getString(R.string.dialog_confirmacion));
		}
	}

	public void addMarkers(ArrayList<Centro> list_centros) 
	{
		
		double latitud;
		double longitud;
		
		
		for(int i = 0; i < list_centros.size(); i++)
		{
			Centro oCentro = list_centros.get(i);
			latitud = Double.parseDouble(oCentro.getLatitud());
			longitud = Double.parseDouble(oCentro.getLongitud());
			
	        mapa.addMarker(new MarkerOptions()
	        		.position(new LatLng(latitud, longitud))
	        		.title(oCentro.getDescripcion())
	        		.snippet(oCentro.getDescripcion()+"#"+oCentro.getDireccion())
	        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_centro)));
		}
		
		mapa.setInfoWindowAdapter(new AdaptadorMyyInfoWindow(inflater,position));
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

			builder.setMessage(recursos.getString(R.string.dialog_confirmacion_gps))
					.setTitle(recursos.getString(R.string.dialog_confirmacion))
					.setPositiveButton(recursos.getString(R.string.dialog_si),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent callGPSSettingIntent = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(callGPSSettingIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton(recursos.getString(R.string.dialog_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			return builder.create();
		}
	}

}
