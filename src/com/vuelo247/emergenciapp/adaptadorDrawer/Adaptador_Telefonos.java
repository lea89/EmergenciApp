package com.vuelo247.emergenciapp.adaptadorDrawer;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;

public class Adaptador_Telefonos extends BaseAdapter
{
	Context context;
    ArrayList<String> telefonos;

    Typeface tf;
    
    FragmentManager fragmentManager;

    public Adaptador_Telefonos(Context context, ArrayList<String> telefonos, FragmentManager fragmentManager)
    {
        this.context = context;
        this.telefonos = telefonos;
        this.fragmentManager = fragmentManager;
    }
    
	@Override
	public int getCount() {
		return telefonos.size();
	}

	@Override
	public String getItem(int position) {
		return telefonos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	private class ViewHolder
	{
		TextView txt_renglon_telefono;
		RelativeLayout relative_renglon_telefono;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		 final String telefono = getItem(position);

	        final ViewHolder holder;

	        if(convertView == null)
	        {
	            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	            convertView = li.inflate(R.layout.renglon_telefono,parent,false);

	            holder = new ViewHolder();

	            holder.txt_renglon_telefono = (TextView) convertView.findViewById(R.id.txt_renglon_telefono);
	            holder.relative_renglon_telefono = (RelativeLayout) convertView.findViewById(R.id.relative_renglon_telefono);
	            
	            String fontPath = context.getResources().getString(R.string.font_path_medium);
				 
		        tf = Typeface.createFromAsset(context.getAssets(), fontPath);
		        
	            holder.txt_renglon_telefono.setTypeface(tf);
		        
	            convertView.setTag(holder);
	        }
	        else
	        {
	            holder = (ViewHolder) convertView.getTag();
	        }


	        holder.txt_renglon_telefono.setText(telefono);
	       
	        holder.relative_renglon_telefono.setOnClickListener(new OnClickListener()
	        {

				@Override
				public void onClick(View v) 
				{
					TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					
					String mPhoneNumber = tMgr.getLine1Number();
					
					Intent intent = new Intent(Intent.ACTION_CALL);
					
					if(mPhoneNumber.startsWith("223"))
					{
						intent.setData(Uri.parse("tel:"+telefono));
					}
					else
					{
						intent.setData(Uri.parse("tel:"+telefono));
					}
					
					
					context.startActivity(intent);
				}
	        	
	        });
	        return convertView;
	}
}
