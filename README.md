# Gestor de Eventos Deportivos

Este proyecto es una aplicación web desarrollada en Java utilizando Servlets y JSP. Simula un sistema de gestión de eventos deportivos que permite
registrar equipos, jugadores y eventos, además de realizar consultas y modificar el estado de los eventos en tiempo real.
Toda la información se almacena en memoria utilizando colecciones (ArrayList), y se inyecta dinámicamente a través de Postman sin necesidad de
conexión a una base de datos.

## Estructura del Proyecto

- ### dao/
Contiene EquipoDAO, JugadorDAO y EventoDAO, que manejan la lógica de acceso a datos para equipos, jugadores y eventos.

### models/
Los modelos de datos (Equipo, Jugador y Evento) están organizados en una estructura separada dentro del proyecto.

### servlet/
Contiene los servlets EquipoServlet, JugadorServlet y EventoServlet, que controlan la lógica de negocio,
exponen servicios REST y gestionan las operaciones sobre los datos.

### jsp/
Contiene las páginas eventos.jsp, que muestra una tabla con los eventos deportivos registrados y permite filtrarlos por deporte y estado.

## Funcionalidad principal

### EquipoServlet (/equipos)

- POST /equipos: Permite registrar un nuevo equipo en el sistema.

- GET /equipos: Retorna la lista de todos los equipos registrados en formato JSON.

- Paginación: Permite paginar la lista de equipos con ?page=X&size=Y.

- JugadorServlet (/jugadores)

- POST /jugadores: Registra un nuevo jugador en el sistema.

- GET /jugadores: Retorna la lista de todos los jugadores registrados en formato JSON.
Transferencia de jugadores: GET /jugadores/transferir?jugadorId=X&equipoDestino=Y permite cambiar un jugador de equipo.

- EventoServlet (/eventos)

- POST /eventos: Permite registrar un nuevo evento deportivo (con al menos dos equipos participantes).

- GET /eventos: Retorna la lista de todos los eventos registrados.

- PUT /eventos?action=vender-entradas&eventoId=X&cantidad=Y: Permite vender entradas verificando la capacidad.

- PUT /eventos?action=actualizar-estado&eventoId=X&estado=Y: Permite actualizar el estado del evento.

- Filtrado de eventos: GET /eventos?deporte=X&estado=Y permite filtrar eventos por deporte, estado y fechas.

- GET /estadisticas: Retorna información estadística sobre los eventos, como cantidad de eventos por deporte, promedio de jugadores por equipo, 
equipos con más eventos y porcentaje de ocupación de cada evento.