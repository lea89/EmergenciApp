package com.vuelo247.emergenciapp.miscellaneous;




	import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

	/**
	* Clase que chequea las conexiones
	* @author  SAUBIETTE Leandro
	* @since  1.0
	* @see Visitar www.vuelo247.com.ar
	*/
	public class CheckConnection {
		
		/*
		 * Metodo que chequea la conexion al WI-FI
		 */
	    public static Boolean ConnectToWifi(Context context){

	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        boolean isConnected = false;
	        if(activeNetwork != null)
	            isConnected = activeNetwork.isConnectedOrConnecting();

	        boolean isWiFi = false;

	        if(isConnected)
	            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

	        return isWiFi;

	    }

	    /*
		 * Metodo que chequea la conexion a internet movil
		 */
	    public static Boolean ConnectTo3G(Context context){

	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        boolean isConnected = false;
	        if(activeNetwork != null)
	            isConnected = activeNetwork.isConnectedOrConnecting();

	        boolean is3G = false;

	        if(isConnected)
	            is3G = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

	        return is3G;

	    }


}
