# Restaurant Reservation Management

Módulo en memoria para crear, consultar y cancelar reservas de un restaurante.
No utiliza interfaz gráfica, aplicación web ni base de datos.

## Tecnologías

- Java 17
- Maven
- JUnit 5

## Requisitos

- JDK 17 o superior
- Maven instalado

## Ejecutar las pruebas

Desde la raíz del proyecto:

```bash
mvn test
```

## Estructura

- `Reservation`: contiene los datos y el estado de una reserva.
- `ReservationManager`: gestiona creación, disponibilidad, cancelación y consulta.
- `ReservationManagerTest`: contiene las pruebas unitarias del módulo.
- `EVIDENCIAS.md`: registra los cuatro ciclos TDD principales y el resultado final.

## TDD

El módulo se desarrolló progresivamente con el ciclo Red, Green y Refactor. Las
pruebas se escribieron antes de la implementación en los cuatro comportamientos
principales documentados en `EVIDENCIAS.md`.
