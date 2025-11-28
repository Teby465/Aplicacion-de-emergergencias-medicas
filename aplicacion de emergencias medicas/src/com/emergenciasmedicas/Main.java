package com.emergenciasmedicas;

import com.emergenciasmedicas.sistema.CentroEmergencias;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Gestión de Emergencias Médicas ===");

        CentroEmergencias centro = new CentroEmergencias();
        centro.iniciarSimulacion();

        try {
            // Dejamos la simulación corriendo por 30 segundos
            Thread.sleep(30000); // 30 segundos = 30,000 milisegundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                centro.detenerSimulacion();
            } catch (InterruptedException e) {
                System.err.println("Error crítico al intentar detener la simulación: " + e.getMessage());
            }
        }

        System.out.println("=== Fin de la simulación ===");
    }
}
