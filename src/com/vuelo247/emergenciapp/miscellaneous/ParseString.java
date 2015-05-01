package com.vuelo247.emergenciapp.miscellaneous;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.vuelo247.emergenciapp.entidades.Centro;

public class ParseString extends DefaultHandler
{
	String tarea;
	/*
	 * Asigno la tarea que se esta realizando
	 */
	public ParseString(String tarea)
	{
		this.tarea = tarea;
	}
    StringBuilder builder;
    
    /*
     * Objetos donde guardaremos los datos recibidos por el web service
     */
    Centro centro;
    
     
    /*
     * ArrayList de objetos que devolveremos a quien lo solicite
     */
    public ArrayList<Centro> list_centros;
    
     // Initialize the arraylist
     // @throws SAXException
      
    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     * Cuando comienza el documento se instancias los ArrayList para luego poder agregar nuestros objetos
     */
    @Override
    public void startDocument() throws SAXException {
         
        /******* Create ArrayList To Store XmlValuesModel object ******/
    	list_centros = new ArrayList<Centro>();
//        list = new ArrayList<XmlValuesModel>();
    }
 
     
    //Inicializar el objeto XmlValuesModel temp que celebrará la información analizada
    //Y el constructor de cadena que almacenará los caracteres de lectura
     // @param uri
     // @param localName ( Nombre de nodo Analizada vendrá en localName)
     // @param qName
     // @param attributes
     // @throws SAXException
      
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
         
        /****  When New XML Node initiating to parse this function called *****/
         
        // Crear un objeto StringBuilder para almacenar valor de nodo xml
        if(localName.equals("item"))
        {
        	centro = new Centro();
        }
        else 
        {
        	builder = new StringBuilder();
        }
    }
     
     
     
     // Terminó de leer la etiqueta de inicio de sesión, añadir a ArrayList
     // @param uri
     // @param localName
     // @param qName
     // @throws SAXException
      
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

    	/*
    	 * En el caso que la tarea sea login solo quiero que guarde el nodo con la etiqueta ultimoID que nos sirve para identificar al usuario
    	 */
        	/*
        	 * Estas comprobaciones son para el caso de que la tarea sea spinner, segun el valor de la etiqueta es lo que asigno
        	 * si es un atributo lo asigno al objeto mediante la variable builder.toString en cambio si es el objeto lo agrego a arraylist
        	 * por ejemplo idArtefacto lo asigno al objeto artefacto y el objeto artefacto lo agrego al arraylist artefactos
        	 */
		    if(localName.equals("item"))
		    {
		    	list_centros.add(centro);
		    }
		    else if(localName.equals("codigo"))
		    {
		    	centro.setCodigo(Integer.parseInt(builder.toString()));
		    }
		    else if(localName.equals("descripcion"))
		    {
		    	centro.setDescripcion(builder.toString());
		    }
		    else if(localName.equals("ubicacion"))
		    {
		    	centro.setUbicacion(builder.toString());
		    }
		    else if(localName.equals("latitud"))
		    {
		    	centro.setLatitud(builder.toString());
		    }
		    else if(localName.equals("longitud"))
		    {
		    	centro.setLongitud(builder.toString());
		    }
		    else if(localName.equals("telefono"))
		    {
		    	centro.setTelefono(builder.toString());
		    }
		    else if(localName.equals("direccion"))
		    {
		    	centro.setDireccion(builder.toString());
		    }
		    else if(localName.equals("email"))
		    {
		    	centro.setEmail(builder.toString());
		    }
		    else if(localName.equals("colectivos"))
		    {
		    	centro.setColectivos(builder.toString());
		    }
    }
 
    
     // Lea el valor de cada nodo XML
     // @param ch
     // @param start
     // @param length
     // @throws SAXException
      
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
             
        /******  Leer los caracteres y los añade al buffer  ******/
        String tempString=new String(ch, start, length);
        //Log.v("Emergenciapp",tempString);
         builder.append(tempString);
    }
    
    /*
     * Retorno arraylist artefactos
     */
    public ArrayList<Centro> getCentros()
    {
    	return list_centros;
    }
    
    /*
     * Retorno arraylist soportes
     */
}

