# Evidencias TDD

## Preparación

Resultado:

```text
[INFO] --- surefire:3.5.2:test (default-test) @ gestion-reservas ---
[INFO] BUILD SUCCESS
```

Commit:

```text
410625e Add initial project structure with .gitignore, pom.xml, and placeholder .gitkeep files
```

## Diseño inicial

`Reservation` mantendrá los datos y el estado de cancelación. `ReservationManager`
mantendrá las reservas en memoria y aplicará las reglas de negocio.

## Ciclo 1 - Crear reserva

### RED

Prueba:

```java
@Test
void shouldCreateReservationSuccessfully() {
    ReservationManager manager = new ReservationManager(30);
    LocalDate date = LocalDate.of(2026, 9, 15);
    LocalTime time = LocalTime.of(20, 0);

    Reservation reservation = manager.createReservation("Ana", 4, date, time);

    assertNotNull(reservation);
    assertEquals("Ana", reservation.getCustomerName());
    assertEquals(4, reservation.getPartySize());
    assertEquals(date, reservation.getDate());
    assertEquals(time, reservation.getTime());
}
```

Resultado:

```text
[ERROR] COMPILATION ERROR :
[ERROR]   symbol:   class ReservationManager
[ERROR]   symbol:   class Reservation
[INFO] BUILD FAILURE
```
