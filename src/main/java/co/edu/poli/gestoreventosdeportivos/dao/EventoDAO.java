package co.edu.poli.gestoreventosdeportivos.dao;

import co.edu.poli.gestoreventosdeportivos.models.Evento;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    private static final List<Evento> eventos = new ArrayList<>(); // Almacenamiento en memoria
    private static int idCounter = 1; // Contador de ID para eventos

    public EventoDAO() {}

    // Metodo para agregar un evento en memoria
    public boolean agregarEvento(Evento evento) {
        if (evento == null || evento.getEquiposParticipantes() == null || evento.getEquiposParticipantes().size() < 2) {
            return false;
        }
        evento.setId(idCounter++);
        eventos.add(evento);
        return true;
    }

    // Obtener todos los eventos almacenados en memoria
    public List<Evento> obtenerEventos() {
        return new ArrayList<>(eventos);
    }

    // Metodo para vender entradas en un evento
    public boolean venderEntradas(int eventoId, int cantidad) {
        for (Evento evento : eventos) {
            if (evento.getId() == eventoId) {
                int disponibles = evento.getCapacidad() - evento.getEntradasVendidas();
                if (cantidad > disponibles) {
                    return false; // No hay suficientes entradas disponibles
                }
                evento.setEntradasVendidas(evento.getEntradasVendidas() + cantidad);
                return true;
            }
        }
        return false; // Evento no encontrado
    }


    // Metodo para actualizar el estado de un evento
    public boolean actualizarEstado(int eventoId, String nuevoEstado) {
        for (Evento evento : eventos) {
            if (evento.getId() == eventoId) {
                evento.setEstado(nuevoEstado);
                return true;
            }
        }
        return false;
    }
}
