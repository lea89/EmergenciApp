package com.vuelo247.emergenciapp.tareas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vuelo247.emergenciapp.R;
import com.vuelo247.emergenciapp.dao.DAOCentros;
import com.vuelo247.emergenciapp.entidades.Centro;
import com.vuelo247.emergenciapp.miscellaneous.ParseString;

public class Tarea_Traer_Centros extends AsyncTask<Void, Void, String> {

	ProgressDialog pd;

	Activity ac;

	Resources recursos;

	StringBuilder stringBuilder;

	HttpPost httpPost;

	String response_string;

	Location myLocation;

	int statusCode;

	public Tarea_Traer_Centros(Activity ac) {
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
	protected String doInBackground(Void... params) {

		stringBuilder = new StringBuilder();
		stringBuilder
				.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soapenv:Header/>"
						+ "<soapenv:Body>"
						+ "<centros_de_salud soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
						+ "<token xsi:type=\"xsd:string\">"
						+ recursos.getString(R.string.ws_token)
						+ "</token>"
						+ "</centros_de_salud>"
						+ "</soapenv:Body>"
						+ "</soapenv:Envelope>");

		httpPost = new HttpPost(recursos.getString(R.string.ws_url));
		StringEntity entity;
		response_string = "";

		try {

			entity = new StringEntity(HTTP.UTF_8);

			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

			httpPost.setEntity(entity);

			HttpGet httpGet = new HttpGet(recursos.getString(R.string.ws_url));

			HttpClient client = new DefaultHttpClient();

			HttpResponse response = client.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();

			statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				response_string = EntityUtils.toString(response.getEntity());
			} else {
				response_string = recursos
						.getString(R.string.error_traer_centros);
			}
			return response_string;
		} catch (Exception e) {
			return response_string;
		}
	}

	@Override
	public void onPostExecute(String response_string) {
		pd.dismiss();


			try 
			{
				BufferedReader br = new BufferedReader(new StringReader(
						response_string));
				InputSource is = new InputSource(br);

				/************ Parse XML **************/

				ParseString parser = new ParseString("spinner");

				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser sp;

				ArrayList<Centro> list_centros = new ArrayList<Centro>();

				sp = factory.newSAXParser();
				XMLReader reader = sp.getXMLReader();

				reader.setContentHandler(parser);

				reader.parse(is);

				list_centros = parser.getCentros();
				DAOCentros dao_centros = new DAOCentros(ac, "CentrosDB", null, 1);

				dao_centros.InsertCentro(list_centros);

			} catch (ParserConfigurationException e) {
				Log.v("EmergenciApp","Parser");
				e.printStackTrace();
				Toast.makeText(ac,
						recursos.getString(R.string.error_traer_centros),
						Toast.LENGTH_SHORT).show();
			} catch (SAXException e) {
				Log.v("EmergenciApp","Sax");
				e.printStackTrace();
				Toast.makeText(ac,
						recursos.getString(R.string.sin_conexion),
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Log.v("EmergenciApp","IO");
				e.printStackTrace();
				Toast.makeText(ac,
						recursos.getString(R.string.error_traer_centros),
						Toast.LENGTH_SHORT).show();
			}

	}
}
