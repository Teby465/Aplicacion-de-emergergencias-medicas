package com.emergenciasmedicas.util;

public enum TipoEvento {
    NUEVA_EMERGENCIA,
    EMERGENCIA_ASIGNADA,
    AMBULANCIA_EN_CAMINO,
    EMERGENCIA_ATENDIDA,
    RECURSO_LIBERADO,
    FALLO_RECURSO, // Un recurso ha fallado
    SISTEMA_MONITOR
}
