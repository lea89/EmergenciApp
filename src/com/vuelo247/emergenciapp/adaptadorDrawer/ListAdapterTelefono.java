package com.vuelo247.emergenciapp.adaptadorDrawer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.entidades.Telefono;

public class ListAdapterTelefono extends ArrayAdapter {

    public ListAdapterTelefono(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.renglon_telefono, null);
        }


        TextView name = (TextView) convertView.findViewById(R.id.txt_nombre_item_telefono);

        Telefono item = (Telefono) getItem(position);
        name.setText(item.getTelefono());

        return convertView;
    }

}
