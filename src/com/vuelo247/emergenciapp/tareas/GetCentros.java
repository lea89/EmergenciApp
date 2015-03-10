package com.vuelo247.emergenciapp.tareas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.miscellaneous.ParseString;

public class GetCentros extends AsyncTask<Void, Void, Void> {

	ProgressDialog pd;
	
	Activity ac;

	Resources recursos; 
	
	StringBuilder stringBuilder;

	HttpPost httpPost;

	String response_string;
	
	Location myLocation;

	public GetCentros(Activity ac) {
		this.ac = ac;
		recursos = ac.getResources();
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(ac);
		pd.setMessage(recursos.getString(R.string.pd_cargando_centros));
		pd.show();
	};

	@Override
	protected Void doInBackground(Void... params) {

		stringBuilder = new StringBuilder();
		stringBuilder
				.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soapenv:Header/>"
						+ "<soapenv:Body>"
						+ "<centros_de_salud soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
						+ "<token xsi:type=\"xsd:string\">"+recursos.getString(R.string.ws_token)+"</token>"
						+ "</centros_de_salud>"
						+ "</soapenv:Body>"
						+ "</soapenv:Envelope>");

		String xml = stringBuilder.toString();
		httpPost = new HttpPost(recursos.getString(R.string.ws_url));
		StringEntity entity;
		response_string = null;

		try {

			entity = new StringEntity(xml, HTTP.UTF_8);

			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

			httpPost.setEntity(entity);

			HttpClient client = new DefaultHttpClient();

			HttpResponse response = client.execute(httpPost);

			response_string = EntityUtils.toString(response.getEntity());
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onPostExecute(Void a) {
		pd.dismiss();
		
		BufferedReader br = new BufferedReader(new StringReader(response_string));
		
		InputSource is = new InputSource(br);

		/************ Parse XML **************/

		ParseString parser = new ParseString("spinner");

		SAXParserFactory factory = SAXParserFactory.newInstance();

		SAXParser sp;
		
		ArrayList<Centro> list_centros = new ArrayList<Centro>();
		
		
		try {
			sp = factory.newSAXParser();
			XMLReader reader = sp.getXMLReader();

			reader.setContentHandler(parser);

			reader.parse(is);

			list_centros = parser.getCentros();

			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (list_centros.size() <= 0) {
			Toast.makeText(ac, recursos.getString(R.string.error_traer_centros),Toast.LENGTH_SHORT).show();
		}
		else
		{
			
		}
		
		DAOCentros dao_centros = new DAOCentros(ac, "CentrosDB", null, 1);
		
		SQLiteDatabase db = dao_centros.getWritableDatabase();

		String insertSQL = "";
		
		if(db != null)
        {
            
			for(int i=0; i<list_centros.size(); i++)
            {
        		
            	insertSQL = "INSERT INTO "+recursos.getString(R.string.db_table_centros)+" (codigo,descripcion,ubicacion,latitud,longitud) VALUES (";
    			insertSQL += +list_centros.get(i).getCodigo();
    			insertSQL += ",'"+list_centros.get(i).getDescripcion()+"'";
    			insertSQL += ",'"+list_centros.get(i).getUbicacion()+"'";
    			insertSQL += ",'"+list_centros.get(i).getLatitud()+"'";
    			insertSQL += ",'"+list_centros.get(i).getLongitud()+"')";
    			
    			db.execSQL(insertSQL);
            }
            db.close();
        }
		else
		{
			Toast.makeText(ac, recursos.getString(R.string.error_traer_centros),
					Toast.LENGTH_SHORT).show();
		}
	}
}
