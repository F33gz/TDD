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

`Reserva` mantendrá los datos y el estado de cancelación. `GestorReservas`
mantendrá las reservas en memoria y aplicará las reglas de negocio.
