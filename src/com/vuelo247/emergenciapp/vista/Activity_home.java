package com.vuelo247.emergenciapp.vista;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnMenuVisibilityListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.adaptadorDrawer.DrawerListAdapter;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.entidades.DrawerItem;
import com.vuelo247.emergenciapp.fragments.Fragment_mapa;
import com.vuelo247.emergenciapp.fragments.Fragment_telefonos;
import com.vuelo247.emergenciapp.tareas.GetCentros;

public class Activity_home extends ActionBarActivity
{
	TextView txt_centros;
	TextView text_2;
	
	ListView drawerList;
	ListView list_telefonos;
	
	String[] tagTelefonos;
	String[] tagTitles;
	
	DrawerLayout drawerLayout;
	
	ActionBar actionBar;
	
	boolean openDrawer = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	
		
		levantarXML();
		//levantarEventos();
		
		tagTitles = getResources().getStringArray(R.array.Tags);

		list_telefonos = (ListView) findViewById(R.id.list_telefonos);

		//Nueva lista de drawer items
		ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
		
		items.add(new DrawerItem(tagTitles[0],R.drawable.ic_html));
		items.add(new DrawerItem(tagTitles[1],R.drawable.ic_css));
		items.add(new DrawerItem(tagTitles[2],R.drawable.ic_javascript));
		items.add(new DrawerItem(tagTitles[3],R.drawable.ic_angular));
		items.add(new DrawerItem(tagTitles[4],R.drawable.ic_python));
		items.add(new DrawerItem(tagTitles[5],R.drawable.ic_ruby));

		// Relacionar el adaptador y la escucha de la lista del drawer
		drawerList.setAdapter(new DrawerListAdapter(this, items));
		
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	private void selectItem(int position) {
		Fragment fragment = null;

		DAOCentros dao_centros = new DAOCentros(Activity_home.this, "CentrosDB", null, 1);
		
		ArrayList<Centro> list_centros = dao_centros.getCentros();
		
		if(list_centros.size() == 0)
		{
			new GetCentros(Activity_home.this).execute();
		}
		
		Log.v("EmergenciApp",list_centros.size()+"");
		
		fragment = new Fragment_mapa(list_centros,position);
        //Mandar como argumento la posición del item
        Bundle args = new Bundle();
        args.putInt(Fragment_telefonos.ARG_ARTICLES_NUMBER, position);
        fragment.setArguments(args);
  
        //Reemplazar contenido
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Se actualiza el item seleccionado y el título, después de cerrar el drawer
        drawerList.setItemChecked(position, true);
        setTitle(tagTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }
	
	/* La escucha del ListView en el Drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
	public void levantarXML()
	{
		txt_centros = (TextView) findViewById(R.id.txt_centros);
		text_2 = (TextView) findViewById(R.id.text_2);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		drawerList = (ListView) findViewById(R.id.left_drawer);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.addOnMenuVisibilityListener(new OnMenuVisibilityListener()
		{
			@Override
			public void onMenuVisibilityChanged(boolean arg0) {
			
				if(openDrawer)
				{
					drawerLayout.closeDrawer(Gravity.LEFT);
					openDrawer = false;
				}
				else
				{
					drawerLayout.openDrawer(Gravity.LEFT);
					openDrawer = true;
				}
				
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
}
