package com.vuelo247.emergenciapp.tareas;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vuelo247.emergenciapp.R;

public class Tarea_Dibujar_Ruta extends AsyncTask<Void, Void, PolylineOptions> {

	GoogleMap mapa;
	
	Activity act;
	
	LatLng source;
	LatLng dest;
	
	Resources recursos;
	
	ProgressDialog pd;
	
	int posicion;
	
	public Tarea_Dibujar_Ruta(LatLng source,LatLng dest,GoogleMap mapa,Activity act,int posicion)
	{
		this.mapa = mapa;
		this.source = source;
		this.dest = dest;
		this.act = act;
		this.posicion = posicion;
		pd = new ProgressDialog(act);
		
		recursos = act.getResources();
	}
	
	@Override
	protected void onPreExecute() 
	{
		pd.setMessage("Dibujando trayecto");
		pd.show();
	};
	
	@Override
	protected PolylineOptions doInBackground(Void... params) {

		Document doc = getDocument(source, dest,"driving");
		
		ArrayList<LatLng> directionPoint = getDirection(doc);
		
		PolylineOptions rectLine;
		
		if(posicion < 2)
        {
			rectLine = new PolylineOptions().width(8).color(act.getResources().getColor(R.color.verde_nombre));
        }
		else
		{
			rectLine = new PolylineOptions().width(8).color(act.getResources().getColor(R.color.rojo));
		}
                

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
            //Log.v("EmergenciApp",directionPoint.get(i)+"");
        }
        
        //Polyline polylin = mapa.addPolyline(rectLine);
        return rectLine;
	}

	public Document getDocument(LatLng start, LatLng end, String mode) {
		String url = "http://maps.googleapis.com/maps/api/directions/xml?"
				+ "origin=" + start.latitude + "," + start.longitude
				+ "&destination=" + end.latitude + "," + end.longitude
				+ "&sensor=false&units=metric&mode=driving";
		Log.d("url", url);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			InputStream in = response.getEntity().getContent();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDurationText(Document doc) {
		try {

			NodeList nl1 = doc.getElementsByTagName("duration");
			Node node1 = nl1.item(0);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "text"));
			Log.i("DurationText", node2.getTextContent());
			return node2.getTextContent();
		} catch (Exception e) {
			return "0";
		}
	}

	public int getDurationValue(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("duration");
			Node node1 = nl1.item(0);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.i("DurationValue", node2.getTextContent());
			return Integer.parseInt(node2.getTextContent());
		} catch (Exception e) {
			return -1;
		}
	}

	public String getDistanceText(Document doc) {
		/*
		 * while (en.hasMoreElements()) { type type = (type) en.nextElement();
		 * 
		 * }
		 */

		try {
			NodeList nl1;
			nl1 = doc.getElementsByTagName("distance");

			Node node1 = nl1.item(nl1.getLength() - 1);
			NodeList nl2 = null;
			nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.d("DistanceText", node2.getTextContent());
			return node2.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

		/*
		 * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
		 * doc.getElementsByTagName("distance");
		 * 
		 * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
		 * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
		 * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
		 * node2.getTextContent()); return node2.getTextContent(); } else return
		 * "-1";} else return "-1";
		 */
	}

	public int getDistanceValue(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("distance");
			Node node1 = null;
			node1 = nl1.item(nl1.getLength() - 1);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.i("DistanceValue", node2.getTextContent());
			return Integer.parseInt(node2.getTextContent());
		} catch (Exception e) {
			return -1;
		}
		/*
		 * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
		 * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
		 * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
		 * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
		 * node2.getTextContent()); return
		 * Integer.parseInt(node2.getTextContent()); } else return 0;
		 */
	}

	public String getStartAddress(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("start_address");
			Node node1 = nl1.item(0);
			Log.i("StartAddress", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

	}

	public String getEndAddress(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("end_address");
			Node node1 = nl1.item(0);
			Log.i("StartAddress", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}
	}

	public String getCopyRights(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("copyrights");
			Node node1 = nl1.item(0);
			Log.i("CopyRights", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

	}

	public ArrayList<LatLng> getDirection(Document doc) {
		NodeList nl1, nl2, nl3;
		ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
		nl1 = doc.getElementsByTagName("step");
		if (nl1.getLength() > 0) {
			for (int i = 0; i < nl1.getLength(); i++) {
				Node node1 = nl1.item(i);
				nl2 = node1.getChildNodes();

				Node locationNode = nl2
						.item(getNodeIndex(nl2, "start_location"));
				nl3 = locationNode.getChildNodes();
				Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
				double lat = Double.parseDouble(latNode.getTextContent());
				Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				double lng = Double.parseDouble(lngNode.getTextContent());
				listGeopoints.add(new LatLng(lat, lng));

				locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "points"));
				ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
				for (int j = 0; j < arr.size(); j++) {
					listGeopoints.add(new LatLng(arr.get(j).latitude, arr
							.get(j).longitude));
				}

				locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "lat"));
				lat = Double.parseDouble(latNode.getTextContent());
				lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				lng = Double.parseDouble(lngNode.getTextContent());
				listGeopoints.add(new LatLng(lat, lng));
			}
		}

		return listGeopoints;
	}

	private int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}

	private ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}
	
	@Override
	protected void onPostExecute(PolylineOptions result) {
		super.onPostExecute(result);
		
		pd.dismiss();
		if(result != null)
		{
			mapa.addPolyline(result);
		}
		else
		{
			Toast.makeText(act, recursos.getString(R.string.error_crear_ruta), Toast.LENGTH_SHORT).show();
		}
	}
}
