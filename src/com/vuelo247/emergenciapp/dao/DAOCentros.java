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
			+ ", distancia REAL)";

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
			insertSQL += "," + list_centros.get(i).getDistancia() + ")";

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
	
	public Centro getCentroMasCercano(double distancia)
	{
		
		String sql = "SELECT latitud,longitud,distancia FROM "+table+" WHERE distancia = "+distancia;

		db = getWritableDatabase();

		Cursor cursor = db.rawQuery(sql, null);

		Centro oCentro = new Centro();
		
		ArrayList<Centro> list = new ArrayList<Centro>();
		
		while (cursor.moveToNext()) {
			
//			oCentro.setCodigo(cursor.getInt(0));
//			oCentro.setDescripcion(cursor.getString(1));
//			oCentro.setUbicacion(cursor.getString(2));
			oCentro.setLatitud(cursor.getString(0));
			oCentro.setLongitud(cursor.getString(1));
			oCentro.setDistancia(cursor.getDouble(2));
		}
		cursor.close();
		db.close();
		
		return oCentro;
	}

}
