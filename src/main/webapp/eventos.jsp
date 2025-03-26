<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="co.edu.poli.gestoreventosdeportivos.dao.EventoDAO" %>
<%@ page import="co.edu.poli.gestoreventosdeportivos.models.Evento" %>
<html>
<head>
    <title>Eventos Deportivos</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h2>Eventos Deportivos Registrados</h2>

<!-- 🔹 Formulario para Filtrar Eventos -->
<form action="eventos.jsp" method="GET">
    <label>Deporte:</label>
    <input type="text" name="deporte">

    <label>Estado:</label>
    <select name="estado">
        <option value="">Todos</option>
        <option value="Programado">Programado</option>
        <option value="En curso">En curso</option>
        <option value="Finalizado">Finalizado</option>
        <option value="Cancelado">Cancelado</option>
    </select>

    <label>Fecha Inicio:</label>
    <input type="date" name="fechaInicio">

    <label>Fecha Fin:</label>
    <input type="date" name="fechaFin">

    <button type="submit">Filtrar</button>
</form>

<table>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Fecha</th>
        <th>Lugar</th>
        <th>Deporte</th>
        <th>Capacidad</th>
        <th>Entradas Vendidas</th>
        <th>Estado</th>
        <th>Equipos Participantes</th>
    </tr>

    <%
        String deporte = request.getParameter("deporte");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        EventoDAO eventoDAO = new EventoDAO();
        List<Evento> eventos = eventoDAO.obtenerEventos().stream()
                .filter(evento -> (deporte == null || deporte.isEmpty() || evento.getDeporte().equalsIgnoreCase(deporte)))
                .filter(evento -> (estado == null || estado.isEmpty() || evento.getEstado().equalsIgnoreCase(estado)))
                .filter(evento -> {
                    if (fechaInicio != null && fechaFin != null && !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                        return evento.getFecha().compareTo(fechaInicio) >= 0 && evento.getFecha().compareTo(fechaFin) <= 0;
                    }
                    return true;
                })
                .toList();

        for (Evento evento : eventos) {
    %>
    <tr>
        <td><%= evento.getId() %></td>
        <td><%= evento.getNombre() %></td>
        <td><%= evento.getFecha() %></td>
        <td><%= evento.getLugar() %></td>
        <td><%= evento.getDeporte() %></td>
        <td><%= evento.getCapacidad() %></td>
        <td><%= evento.getEntradasVendidas() %></td>
        <td><%= evento.getEstado() %></td>
        <td><%= evento.getEquiposParticipantes() %></td>
    </tr>
    <% } %>
</table>
</body>
</html>
