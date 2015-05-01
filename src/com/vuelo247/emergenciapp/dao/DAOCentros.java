package com.vuelo247.emergenciapp.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.entidades.Centro;

public class DAOCentros extends SQLiteOpenHelper {

	Context context;
	String table;
	
	public DAOCentros(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
		table = context.getResources().getString(R.string.db_table_centros);
	}

	SQLiteDatabase db;
	String sqlCreate = "CREATE TABLE Centros_Salud (" 
			+ "codigo INTEGER"
			+ ", descripcion TEXT" 
			+ ", ubicacion TEXT" 
			+ ", latitud TEXT"
			+ ", longitud TEXT" 
			+ ", telefono TEXT"
			+ ", direccion TEXT"
			+ ", email TEXT"
			+ ", colectivos TEXT)";

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void InsertCentro(ArrayList<Centro> list_centros) {
		db = getWritableDatabase();

		String insertSQL = "";

		
		for (int i = 0; i < list_centros.size(); i++) {
			insertSQL = "INSERT INTO Centros_Salud VALUES (";
			insertSQL += +list_centros.get(i).getCodigo();
			insertSQL += ",'" + list_centros.get(i).getDescripcion() + "'";
			insertSQL += ",'" + list_centros.get(i).getUbicacion() + "'";
			insertSQL += ",'" + list_centros.get(i).getLatitud() + "'";
			insertSQL += ",'" + list_centros.get(i).getLongitud() + "'";
			insertSQL += ",'" + list_centros.get(i).getTelefono()+ "'";
			insertSQL += ",'" + list_centros.get(i).getDireccion() + "'";
			insertSQL += ",'" + list_centros.get(i).getEmail() + "'";
			insertSQL += ",'" + list_centros.get(i).getColectivos() + "')";

			db.execSQL(insertSQL);
		}

		db.close();
	}

	public ArrayList<Centro> getCentros() {
		ArrayList<Centro> list_centros = new ArrayList<Centro>();
		String sql = "SELECT * FROM "
				+ context.getResources().getString(R.string.db_table_centros);

		db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Centro oCentro = new Centro();
			oCentro.setCodigo(cursor.getInt(0));
			oCentro.setDescripcion(cursor.getString(1));
			oCentro.setUbicacion(cursor.getString(2));
			oCentro.setLatitud(cursor.getString(3));
			oCentro.setLongitud(cursor.getString(4));
			oCentro.setTelefono(cursor.getString(5));
			oCentro.setDireccion(cursor.getString(6));
			oCentro.setEmail(cursor.getString(7));
			oCentro.setColectivos(cursor.getString(8));

			list_centros.add(oCentro);
		}
		cursor.close();
		db.close();
		return list_centros;
	}
	
	public void dropTable()
	{
		db = getWritableDatabase();
		
		String sql = "DELETE FROM Centros_Salud";
		
		db.execSQL(sql);
		db.close();
	}

	public Centro getCentro(int codigo) 
	{
		Centro oCentro = new Centro();
		
		String sql = "SELECT * FROM "
				+ context.getResources().getString(R.string.db_table_centros)+" WHERE codigo = "+codigo;

		db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			
			oCentro.setCodigo(cursor.getInt(0));
			oCentro.setDescripcion(cursor.getString(1));
			oCentro.setUbicacion(cursor.getString(2));
			oCentro.setLatitud(cursor.getString(3));
			oCentro.setLongitud(cursor.getString(4));
			oCentro.setTelefono(cursor.getString(5));
			oCentro.setDireccion(cursor.getString(6));
			oCentro.setEmail(cursor.getString(7));
			oCentro.setColectivos(cursor.getString(8));
		}
		cursor.close();
		db.close();
		return oCentro;
	}


}
