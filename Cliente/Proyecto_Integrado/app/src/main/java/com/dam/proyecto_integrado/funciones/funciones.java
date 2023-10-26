package com.dam.proyecto_integrado.funciones;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class funciones {
    //Método que calcula el punto más cercano entre un origen y el resto de puntos
    public static LatLng calcularPuntoMasCercano(LatLng origen, List<LatLng> puntos) {
        if (puntos.isEmpty()) {
            return null; // Si la lista de puntos está vacía, no hay un punto más cercano
        }
        LatLng puntoMasCercano = puntos.get(0);
        double distanciaMinima = calcularDistancia(origen, puntoMasCercano);

        for (int i = 1; i < puntos.size(); i++) {
            LatLng puntoActual = puntos.get(i);
            double distanciaActual = calcularDistancia(origen, puntoActual);

            if (distanciaActual < distanciaMinima) {
                distanciaMinima = distanciaActual;
                puntoMasCercano = puntoActual;
            }
        }

        return puntoMasCercano;
    }

    //Método que calcula la distancia entre dos puntos
    private static double calcularDistancia(LatLng punto1, LatLng punto2) {
        double latitud1 = punto1.latitude;
        double longitud1 = punto1.longitude;
        double latitud2 = punto2.latitude;
        double longitud2 = punto2.longitude;

        // Fórmula de la distancia euclidiana
        double distancia = Math.sqrt(Math.pow(latitud2 - latitud1, 2) + Math.pow(longitud2 - longitud1, 2));

        return distancia;
    }
}
