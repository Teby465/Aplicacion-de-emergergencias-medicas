package com.emergenciasmedicas.util;

public enum EstadoEmergencia {
    PENDIENTE,
    ASIGNADA,
    EN_CAMINO,
    ATENDIDA,
    CERRADA,
    FALLIDA // La atención de la emergencia falló y necesita ser re-planificada
}
