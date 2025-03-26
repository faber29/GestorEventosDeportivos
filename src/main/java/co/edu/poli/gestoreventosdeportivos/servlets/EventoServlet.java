package co.edu.poli.gestoreventosdeportivos.servlets;

import co.edu.poli.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.gestoreventosdeportivos.models.Evento;
import co.edu.poli.gestoreventosdeportivos.models.Equipo;
import co.edu.poli.gestoreventosdeportivos.models.Jugador;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet("/eventos")
public class EventoServlet extends HttpServlet {
    private final EventoDAO eventoDAO = new EventoDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (BufferedReader reader = request.getReader()) {
            // Leer JSON recibido
            Evento nuevoEvento = gson.fromJson(reader, Evento.class);

            // Validar evento
            if (nuevoEvento == null || nuevoEvento.getEquiposParticipantes() == null || nuevoEvento.getEquiposParticipantes().size() < 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: JSON inválido o faltan equipos participantes.");
                return;
            }

            // Agregar evento
            boolean agregado = eventoDAO.agregarEvento(nuevoEvento);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(agregado ? nuevoEvento : "Debe haber al menos 2 equipos participantes"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("estadisticas".equals(action)) {
            obtenerEstadisticas(response);
            return;
        }

        // Filtro de eventos por deporte, estado y fechas
        String deporte = request.getParameter("deporte");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        List<Evento> eventosFiltrados = eventoDAO.obtenerEventos().stream()
                .filter(evento -> (deporte == null || evento.getDeporte().equalsIgnoreCase(deporte)))
                .filter(evento -> (estado == null || evento.getEstado().equalsIgnoreCase(estado)))
                .filter(evento -> {
                    if (fechaInicio != null && fechaFin != null) {
                        return evento.getFecha().compareTo(fechaInicio) >= 0 && evento.getFecha().compareTo(fechaFin) <= 0;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(eventosFiltrados));
    }

    // ✅ Método para calcular las estadísticas
    private void obtenerEstadisticas(HttpServletResponse response) throws IOException {
        List<Evento> eventos = eventoDAO.obtenerEventos();
        List<Equipo> equipos = equipoDAO.obtenerEquipos();
        List<Jugador> jugadores = jugadorDAO.obtenerJugadores();

        // 1️⃣ Cantidad de eventos por deporte
        Map<String, Long> eventosPorDeporte = eventos.stream()
                .collect(Collectors.groupingBy(Evento::getDeporte, Collectors.counting()));

        // 2️⃣ Promedio de jugadores por equipo
        double promedioJugadores = equipos.isEmpty() ? 0 :
                jugadores.size() / (double) equipos.size();

        // 3️⃣ Equipos con más eventos programados
        Map<Integer, Long> eventosPorEquipo = eventos.stream()
                .flatMap(evento -> evento.getEquiposParticipantes().stream())
                .collect(Collectors.groupingBy(equipoId -> equipoId, Collectors.counting()));

        List<Integer> equiposMasEventos = eventosPorEquipo.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(3) // Obtener los 3 equipos con más eventos
                .collect(Collectors.toList());

        // 4️⃣ Porcentaje de ocupación de cada evento
        Map<Integer, Double> ocupacionEventos = eventos.stream()
                .collect(Collectors.toMap(Evento::getId,
                        evento -> (evento.getCapacidad() > 0) ?
                                (evento.getEntradasVendidas() / (double) evento.getCapacidad()) * 100 : 0));

        // ✅ Construir JSON de respuesta
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("eventosPorDeporte", eventosPorDeporte);
        estadisticas.put("promedioJugadoresPorEquipo", promedioJugadores);
        estadisticas.put("equiposMasEventos", equiposMasEventos);
        estadisticas.put("ocupacionEventos", ocupacionEventos);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(estadisticas));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("vender-entradas".equals(action)) {
            venderEntradas(request, response);
        } else if ("actualizar-estado".equals(action)) {
            actualizarEstado(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Operación no válida");
        }
    }

    private void venderEntradas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int eventoId = Integer.parseInt(request.getParameter("eventoId"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));

            boolean vendido = eventoDAO.venderEntradas(eventoId, cantidad);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(vendido ? "Venta realizada" : "No hay suficientes entradas disponibles o evento no encontrado"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Formato inválido en eventoId o cantidad.");
        }
    }

    private void actualizarEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int eventoId = Integer.parseInt(request.getParameter("eventoId"));
            String nuevoEstado = request.getParameter("estado");

            boolean actualizado = eventoDAO.actualizarEstado(eventoId, nuevoEstado);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(actualizado ? "Estado actualizado" : "Evento no encontrado"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Formato inválido en eventoId.");
        }
    }
}
