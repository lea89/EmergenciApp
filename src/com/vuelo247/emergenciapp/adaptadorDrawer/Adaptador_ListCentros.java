package com.vuelo247.emergenciapp.adaptadorDrawer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.fragments.Fragment_item_centros;

public class Adaptador_ListCentros extends BaseAdapter{

	Context context;
    ArrayList<Centro> centros;

    String url;
    
    Typeface tf;
    
    FragmentManager fragmentManager;

    public Adaptador_ListCentros(Context context, ArrayList<Centro> centros, FragmentManager fragmentManager)
    {
        this.context = context;
        this.centros = centros;
        this.fragmentManager = fragmentManager;
    }
    
	@Override
	public int getCount() {
		return centros.size();
	}

	@Override
	public Centro getItem(int position) {
		return centros.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	private class ViewHolder
	{
		ImageView img_renglon_centro;
		TextView txt_renglon_centro_nombre;
		TextView txt_renglon_centro_direccion;
		RelativeLayout relative_renglon_centros;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		 final Centro centro = getItem(position);

	        final ViewHolder holder;

	        if(convertView == null)
	        {
	            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	            convertView = li.inflate(R.layout.renglon_centro,parent,false);

	            holder = new ViewHolder();

	            holder.img_renglon_centro = (ImageView) convertView.findViewById(R.id.img_renglon_centro);
	            holder.txt_renglon_centro_nombre = (TextView) convertView.findViewById(R.id.txt_renglon_centro_nombre);
	            holder.txt_renglon_centro_direccion = (TextView) convertView.findViewById(R.id.txt_renglon_centro_direccion);
	            holder.relative_renglon_centros = (RelativeLayout) convertView.findViewById(R.id.relative_renglon_centros);
	            
	            String fontPath = context.getResources().getString(R.string.font_path_medium);
				 
		        tf = Typeface.createFromAsset(context.getAssets(), fontPath);
		        
	            holder.txt_renglon_centro_direccion.setTypeface(tf);
		        holder.txt_renglon_centro_nombre.setTypeface(tf);
		        
	            convertView.setTag(holder);
	        }
	        else
	        {
	            holder = (ViewHolder) convertView.getTag();
	        }


	        holder.txt_renglon_centro_nombre.setText(centro.getDescripcion());
	        holder.txt_renglon_centro_direccion.setText(centro.getDireccion());
	        holder.img_renglon_centro.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.marker_centro));
	       
	        holder.relative_renglon_centros.setOnClickListener(new OnClickListener()
	        {

				@Override
				public void onClick(View v) 
				{
					Fragment fragment = new Fragment_item_centros(centro.getCodigo());
					
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();
				}
	        	
	        });
	        
	        holder.txt_renglon_centro_nombre.setOnClickListener(new OnClickListener()
	        {

				@Override
				public void onClick(View v) 
				{
					Fragment fragment = new Fragment_item_centros(centro.getCodigo());
					
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();
				}
	        	
	        });
	        return convertView;
	}
}