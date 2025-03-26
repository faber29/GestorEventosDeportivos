# GestorEventosDeportivos

Este proyecto fue desarrollado como parte del taller de la materia *Construcción de Elementos de Software 3* del Politécnico Jaime Isaza Cadavid.

Consiste en una aplicación web en Java que permite gestionar equipos, jugadores y eventos deportivos mediante *Servlets, **JSP* y almacenamiento en memoria con colecciones. Incluye servicios REST para registrar, consultar y modificar la información, así como una interfaz JSP para visualizar los eventos.
Todo el código está desplegado sobre Apache Tomcat y estructurado con Maven.

## ¿Qué se implementó en el proyecto?

- Se desarrolló una aplicación Java Web con Servlets y JSP para la gestión de eventos deportivos.
- Toda la información se almacena en memoria utilizando colecciones (no se usó base de datos).
- Se manejaron tres entidades principales:
    - Equipos
    - Jugadores
    - Eventos deportivos
- Cada entidad fue representada por una clase DAO (Data Access Object).
- Se implementaron servicios REST usando Servlets para:
    - Crear y listar equipos (con paginación y validaciones)
    - Registrar y consultar jugadores
    - Transferir jugadores entre equipos
    - Crear y visualizar eventos deportivos
    - Vender entradas para eventos
    - Cambiar el estado de un evento
- Se validaron reglas como:
    - No permitir equipos con el mismo nombre y deporte
    - No permitir jugadores repetidos con el mismo número en el mismo equipo
    - Solo permitir eventos con mínimo dos equipos del mismo deporte
- Se construyó una página JSP que muestra una tabla con los eventos y permite filtrar por deporte o estado.
- Se creó un endpoint especial para obtener estadísticas generales:
    - Eventos por deporte
    - Promedio de jugadores por equipo
    - Equipos con más eventos programados
    - Porcentaje de ocupación de cada evento
- Se utilizó Maven para gestionar el proyecto, Tomcat como servidor y GSON para el manejo de JSON.

