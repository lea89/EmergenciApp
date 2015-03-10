package com.vuelo247.emergenciapp.entidades;

public class MyMarker
{
    private String ubicacion;
    private String descripcion;
    private Double latitude;
    private Double longitude;

    public MyMarker(String descripcion, String ubicacion, Double latitude, Double longitude)
    {
        this.descripcion = descripcion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ubicacion = ubicacion;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion)
    {
        this.ubicacion = ubicacion;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double Latitude)
    {
        this.latitude = Latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setmLongitude(Double longitude)
    {
        this.longitude = longitude;
    }
}
