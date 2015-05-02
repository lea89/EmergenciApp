package com.vuelo247.emergenciapp.vista;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.Adaptador_DrawerList;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.entidades.DrawerItem;
import com.vuelo247.emergenciapp.fragments.Fragment_centros;
import com.vuelo247.emergenciapp.fragments.Fragment_home;
import com.vuelo247.emergenciapp.fragments.Fragment_mapa;
import com.vuelo247.emergenciapp.miscellaneous.CheckConnection;
import com.vuelo247.emergenciapp.tareas.Tarea_Traer_Centros;

@SuppressWarnings("deprecation")
@SuppressLint("InflateParams")
public class Activity_home extends ActionBarActivity {

	ListView drawerList;
	ListView list_telefonos;

	String[] tagTelefonos;
	String[] tagTitles;

	DrawerLayout drawerLayout;

	ActionBar actionBar;

	boolean openDrawer = false;

	private LocationManager locationManager;
	private LocationListener listenerGPS;
	private Resources recursos;
	public static Location myLocation;
	ActionBarDrawerToggle drawerToggle;
	String sin_conexion;
	
	Fragment fragment;
	
	Typeface tf;
	
	ArrayList<Centro> list_centros;
	DAOCentros dao_centros;
	
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		float density = getResources().getDisplayMetrics().density;
		Log.v("EmergenciApp",density+"");
		dao_centros = new DAOCentros(Activity_home.this,
				"CentrosDB", null, 1);

		list_centros = dao_centros.getCentros();

		recursos = getResources();
		
		sin_conexion = recursos.getString(R.string.sin_conexion);
		
		if (list_centros.size() <= 0) 
		{
			if(CheckConnection.ConnectTo3G(this) || CheckConnection.ConnectToWifi(this))
			{
				new Tarea_Traer_Centros(Activity_home.this).execute();
			}
			else
			{
				Toast.makeText(Activity_home.this,recursos.getString(R.string.sin_conexion),
						Toast.LENGTH_SHORT).show();
			}
		}
		
		Fragment fragment = new Fragment_home(this);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		levantarXML();
		
		setCustomActionBar();
		
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		setupGPS();
		
		tagTitles = recursos.getStringArray(R.array.Tags);

		list_telefonos = (ListView) findViewById(R.id.list_telefonos);

		// Nueva lista de drawer items
		ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();

		items.add(new DrawerItem(tagTitles[0], R.drawable.ic_higa));
		items.add(new DrawerItem(tagTitles[1], R.drawable.ic_hiemi));
		items.add(new DrawerItem(tagTitles[2], R.drawable.ic_mas_cercano));
		items.add(new DrawerItem(tagTitles[3], R.drawable.ic_centros_salud));
		items.add(new DrawerItem(tagTitles[4], R.drawable.actualizar));

		drawerList.setAdapter(new Adaptador_DrawerList(this, items));

		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                  R.drawable.menu, 0, 0
                ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
            
            @Override
        	public void onDrawerSlide(View drawerView, float slideOffset) 
        	{
            	super.onDrawerSlide(drawerView, slideOffset);
            	
            	drawerLayout.bringChildToFront(drawerView);
            	drawerLayout.requestLayout();
            	drawerLayout.setScrimColor(Color.TRANSPARENT);
        	}
        };
        
        drawerLayout.setDrawerListener(drawerToggle);
        
	}
	
	private void setCustomActionBar() {
		
		ActionBar mActionBar = getSupportActionBar();
		
		mActionBar.setDisplayShowHomeEnabled(false);
		
		mActionBar.setDisplayShowTitleEnabled(true);
		
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
		
		mActionBar.setCustomView(mCustomView);
		
		mActionBar.setDisplayShowCustomEnabled(true);		
	}
	
	

	private void selectItem(int position) 
	{
		if(position == 0)
		{
				fragment = new Fragment_mapa(0);

				fragmentManager = getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		}
		
		if(position == 1)
		{
				fragment = new Fragment_mapa(1);

				fragmentManager = getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		}
		
		if(position == 3)
		{
			
			fragment = new Fragment_centros(position);
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}

		if(position == 2)
		{
			dao_centros = new DAOCentros(Activity_home.this,"CentrosDB", null, 1);

			list_centros = dao_centros.getCentros();
			if(list_centros.size() > 0)
			{
				fragment = new Fragment_mapa(position);
				
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}
			else
			{
				Toast.makeText(Activity_home.this,recursos.getString(R.string.actualizar_centros_reintentar),
						Toast.LENGTH_SHORT).show();
			}
				
		}
		
		if(position == 4)
		{
			if(CheckConnection.ConnectTo3G(Activity_home.this) || CheckConnection.ConnectToWifi(Activity_home.this))
			{
				new Tarea_Traer_Centros(Activity_home.this).execute();
			}
			else
			{
				Toast.makeText(Activity_home.this,recursos.getString(R.string.sin_conexion),
						Toast.LENGTH_SHORT).show();
			}
		}

		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
	}
	
	@Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sincronizar el estado del drawer
	        drawerToggle.syncState();
	    }

		@Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Cambiar las configuraciones del drawer si hubo modificaciones
	        drawerToggle.onConfigurationChanged(newConfig);
	    }

	/* La escucha del ListView en el Drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			selectItem(position);
			
		}
	}

	public void levantarXML() 
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerList = (ListView) findViewById(R.id.left_drawer);

	}

	private void setupGPS() {
		String serviceString = Context.LOCATION_SERVICE;

		locationManager = (LocationManager) getSystemService(serviceString);

		listenerGPS = new LocationListener();

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 100, listenerGPS);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 100, listenerGPS);
		} else {
			DialogoConfirmacion dialog = new DialogoConfirmacion();
			dialog.show(getSupportFragmentManager(),
					recursos.getString(R.string.dialog_confirmacion));

		}
	}

	public class LocationListener implements android.location.LocationListener,
			Listener {
		public void onLocationChanged(Location location) {

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         
        if (drawerToggle.onOptionsItemSelected(item)) {
   // Toma los eventos de selección del toggle aquí
            return true;
        }

  
        return super.onOptionsItemSelected(item);
    }
}
