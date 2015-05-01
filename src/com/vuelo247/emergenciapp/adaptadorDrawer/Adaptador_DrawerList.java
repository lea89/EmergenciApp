package com.vuelo247.emergenciapp.adaptadorDrawer;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.entidades.DrawerItem;

@SuppressWarnings("rawtypes")
public class Adaptador_DrawerList extends ArrayAdapter {

	Context context;
	
    @SuppressWarnings("unchecked")
	public Adaptador_DrawerList(Context context, List objects) {
        super(context, 0, objects);
        this.context = context;
    }
    
    class ViewHolder
    {
    	 ImageView icon;
    	 TextView name;
    	 RelativeLayout relative_renglon_drawer;
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	ViewHolder holder = new ViewHolder();
        
    	if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.renglon_drawer, null);
            holder.icon  =(ImageView) convertView.findViewById(R.id.icon);
            holder.name  =(TextView) convertView.findViewById(R.id.txt_nombre_item_drawer);
            holder.relative_renglon_drawer  =(RelativeLayout) convertView.findViewById(R.id.relative_renglon_drawer);
            convertView.setTag(holder);
        }
        else
        {
        	holder = (ViewHolder)convertView.getTag();
        }
       
        DrawerItem item = (DrawerItem) getItem(position);
        
        holder.icon.setImageResource(item.getIconId());
        
        holder. name.setText(item.getName());
        
        //Agregar a metodo estatico para utilizar en toda la app la fuente
        String fontPath = context.getResources().getString(R.string.font_path_regular);
 
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        //
        holder.name.setTypeface(tf);

        return convertView;
    }
}
