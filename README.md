# BookHub-EP3

## Conexión con Neon

La aplicación usa PostgreSQL. Configura las variables de entorno que entrega
Neon (`PGHOST`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`, `PGSSLMODE` y
`PGCHANNELBINDING`) antes de iniciar Spring Boot.

En PowerShell, por ejemplo:

```powershell
$env:PGHOST = "<host>"
$env:PGDATABASE = "<base-de-datos>"
$env:PGUSER = "<usuario>"
$env:PGPASSWORD = "<contraseña>"
$env:PGSSLMODE = "require"
$env:PGCHANNELBINDING = "require"
.\mvnw.cmd spring-boot:run
```

Reemplaza los valores entre `< >` con los datos de Neon. No guardes credenciales
reales en este repositorio. `spring.jpa.hibernate.ddl-auto=none` requiere que
las tablas ya existan en Neon; crea o migra allí el esquema antes de usar la API.

## Despliegue en Render con Docker

Render detecta el `Dockerfile` de este repositorio. Crea un servicio **Web
Service** con runtime **Docker** y configura en la sección **Environment** las
variables `PGHOST`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`, `PGSSLMODE` y
`PGCHANNELBINDING` con los datos de Neon. No hace falta configurar `PORT`:
Render la proporciona y Spring Boot la usa automáticamente.