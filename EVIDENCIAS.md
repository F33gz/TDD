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

Commit:

```text
a6edff9 test: add reservation creation test
```

### GREEN

Código:

```java
public Reservation createReservation(
        String customerName,
        int partySize,
        LocalDate date,
        LocalTime time) {
    return new Reservation(customerName, partySize, date, time);
}
```

Resultado:

```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
b99360d feat: implement Reservation and ReservationManager classes
```

### REFACTOR

Cambio realizado: `maximumCapacity` ahora se conserva como estado de
`ReservationManager` en lugar de descartarse.

Resultado:

```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
6de7290 feat: refactor ReservationManager to maintain maximumCapacity state
```

## Ciclo 2 - Capacidad insuficiente

### RED

Prueba:

```java
@Test
void shouldRejectReservationWhenCapacityIsInsufficient() {
    ReservationManager manager = new ReservationManager(30);
    LocalDate date = LocalDate.of(2026, 9, 15);
    LocalTime time = LocalTime.of(20, 0);
    manager.createReservation("Ana", 26, date, time);

    assertThrows(
            IllegalStateException.class,
            () -> manager.createReservation("Ben", 6, date, time));
}
```

Resultado:

```text
[ERROR] Tests run: 11, Failures: 1, Errors: 0, Skipped: 0
Expected java.lang.IllegalStateException to be thrown, but nothing was thrown.
[INFO] BUILD FAILURE
```

Commit:

```text
42aa4a5 feat: add test to reject reservation when capacity is insufficient
```

### GREEN

Código:

```java
if (occupiedSeats + partySize > maximumCapacity) {
    throw new IllegalStateException();
}

reservations.add(reservation);
```

Resultado:

```text
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
ee00ace feat: implement capacity check in reservation creation
```

### REFACTOR

Cambio realizado: el cálculo de capacidad se extrajo a `hasAvailability(...)`.

Resultado:

```text
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
91e2a48 feat: refactor capacity check in reservation creation to use hasAvailability method
```

## Ciclo 3 - Cancelar reserva

### RED

Prueba:

```java
@Test
void shouldCancelReservationByCode() {
    ReservationManager manager = new ReservationManager(30);
    Reservation reservation = manager.createReservation(
            "Ana", 4, LocalDate.of(2026, 9, 15), LocalTime.of(20, 0));

    manager.cancelReservation(reservation.getCode());

    assertTrue(reservation.isCancelled());
}
```

Resultado:

```text
[ERROR] cannot find symbol: method cancelReservation(java.lang.String)
[ERROR] cannot find symbol: method isCancelled()
[INFO] BUILD FAILURE
```

Commit:

```text
6d4be72 test: cover reservation cancellation
```

### GREEN

Código:

```java
if (reservation.getCode().equals(code)) {
    reservation.cancel();
    return;
}
```

Resultado:

```text
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
c6f8cfd feat: implement reservation cancellation functionality
```

### REFACTOR

Cambio realizado: la búsqueda por código se extrajo a `findReservation(...)`.

Resultado:

```text
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
6624c6e refactor: implement reservation cancellation and refactor reservation lookup
```

## Ciclo 4 - Recuperar capacidad

### RED

Prueba:

```java
@Test
void shouldRestoreCapacityAfterCancellation() {
    ReservationManager manager = new ReservationManager(30);
    LocalDate date = LocalDate.of(2026, 9, 15);
    LocalTime time = LocalTime.of(20, 0);
    Reservation reservation = manager.createReservation("Ana", 30, date, time);
    assertFalse(manager.hasAvailability(date, time, 1));

    manager.cancelReservation(reservation.getCode());

    assertTrue(manager.hasAvailability(date, time, 30));
}
```

Resultado:

```text
[ERROR] Tests run: 20, Failures: 1, Errors: 0, Skipped: 0
shouldRestoreCapacityAfterCancellation expected: <true> but was: <false>
[INFO] BUILD FAILURE
```

Commit:

```text
5f08b5c test: add test for restoring capacity after reservation cancellation
```

### GREEN

Código:

```java
if (!reservation.isCancelled()
        && reservation.getDate().equals(date)
        && reservation.getTime().equals(time)) {
    occupiedSeats += reservation.getPartySize();
}
```

Resultado:

```text
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
be73140 fix: update availability check to exclude cancelled reservations
```

### REFACTOR

Cambio realizado: la comprobación de reserva activa se movió a `isActiveAt(...)`.

Resultado:

```text
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Commit:

```text
b8a8fc6 refactor: refactor availability check to use isActiveAt method for active reservations
```
