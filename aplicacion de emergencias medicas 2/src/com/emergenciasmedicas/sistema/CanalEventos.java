package com.emergenciasmedicas.sistema;

import com.emergenciasmedicas.model.Evento;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class CanalEventos implements Runnable {
    private static final CanalEventos INSTANCIA = new CanalEventos();
    private final BlockingQueue<Evento> colaEventos = new LinkedBlockingQueue<>();
    private final List<Suscriptor> suscriptores = new CopyOnWriteArrayList<>();

    private CanalEventos() {
        // Constructor privado para el patrón Singleton
    }

    public static CanalEventos getInstancia() {
        return INSTANCIA;
    }

    public void suscribir(Suscriptor s) {
        suscriptores.add(s);
    }

    public void publicar(Evento e) {
        // No bloquea, simplemente añade el evento a la cola
        colaEventos.offer(e);
    }

    @Override
    public void run() {
        System.out.println("Canal de eventos iniciado.");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // take() bloquea hasta que un evento esté disponible
                Evento evento = colaEventos.take();
                // Notifica a todos los suscriptores
                for (Suscriptor s : suscriptores) {
                    s.onEvento(evento);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Canal de eventos interrumpido.");
        }
    }
}
