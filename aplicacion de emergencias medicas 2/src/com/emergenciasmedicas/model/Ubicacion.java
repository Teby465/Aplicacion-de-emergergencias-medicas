package com.emergenciasmedicas.model;

public class Ubicacion {
    private double latitud;
    private double longitud;

    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**
     * Calcula la distancia euclidiana a otra ubicación.
     * @param otra La otra ubicación.
     * @return La distancia calculada.
     */
    public double distanciaA(Ubicacion otra) {
        double diffLat = this.latitud - otra.latitud;
        double diffLon = this.longitud - otra.longitud;
        return Math.sqrt(diffLat * diffLat + diffLon * diffLon);
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
