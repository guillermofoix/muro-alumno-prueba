# Muro de notas

Pizarra de post-its compartida: escribes una nota, eliges color, y todos la ven. Se pueden dar likes y borrar.

La funcionalidad es mínima **a propósito**: este repositorio es una plantilla para practicar el **despliegue** (Git, Docker y AWS) en DAW, no para aprender a programar la aplicación.

- **Backend:** Spring Boot 3 + Java 21 (API REST en `/api`)
- **Frontend:** un único `index.html` (en producción lo serviría Nginx con `frontend/nginx.conf`)
- **Base de datos:** PostgreSQL al desplegar; H2 en memoria al ejecutar en tu PC con el perfil `local`

```
navegador ──► Nginx (frontend:80) ──/api/──► Spring Boot (backend:8080) ──► PostgreSQL
```

## Estructura

```
.
├── backend/                  API en Spring Boot
│   ├── src/main/java/...     código (notas, estado, errores)
│   ├── src/test/java/...     tests con H2 y MockMvc
│   ├── pom.xml
│   └── mvnw, mvnw.cmd        Maven Wrapper (no hace falta instalar Maven)
├── frontend/
│   ├── index.html            la web completa (HTML + CSS + JS)
│   └── nginx.conf            sirve la web y reenvía /api/ al backend
└── .env.example              plantilla de variables de entorno
```

## Ejecutarlo en tu PC (perfil `local`)

Solo necesitas **Java 21 o superior**. No hace falta instalar PostgreSQL ni Docker: con el perfil `local` el programa usa una base de datos H2 en memoria (los datos se pierden al parar) y no exige variables de entorno.

Desde la carpeta `backend`:

**Windows (PowerShell o CMD)**

```
cd backend
mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

**Linux / macOS**

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

**Desde el IDE** (IntelliJ, VS Code, Eclipse/STS): abre la clase `MuroNotasApplication` y crea una configuración de ejecución con una de estas opciones:

- Variable de entorno `SPRING_PROFILES_ACTIVE=local`, o
- Argumento del programa `--spring.profiles.active=local`.

Importante: la carpeta de trabajo (*working directory*) debe ser `backend` (o la raíz del repositorio) para que encuentre `frontend/index.html`.

**Qué debes ver:** abre <http://localhost:8080>. Aparece la web del muro de notas con la franja superior en verde (`API: OK`, `BD: OK`), y puedes crear, dar like y borrar notas. También funciona <http://localhost:8080/api/estado>, que devuelve algo como `{"api":"ok","bd":"ok","contenedor":"...","version":"1.0.0"}`.

Para parar el programa: `Ctrl+C`.

### Tests del backend

```bash
cd backend
./mvnw test        # en Windows: mvnw.cmd test
```

## Variables de entorno (despliegue con PostgreSQL)

Sin el perfil `local`, el backend se conecta a PostgreSQL con estas variables. Se definen en un fichero `.env` (cópialo de `.env.example`). El `.env` **nunca** se sube a Git.

| Variable      | Obligatoria | Por defecto | Para qué sirve |
|---------------|-------------|-------------|----------------|
| `DB_HOST`     | Sí          | —           | Servidor de PostgreSQL (en el lab, el endpoint de RDS) |
| `DB_PORT`     | No          | `5432`      | Puerto de PostgreSQL |
| `DB_NAME`     | No          | `muronotas` | Nombre de la base de datos |
| `DB_USER`     | No          | `postgres`  | Usuario de la base de datos |
| `DB_PASSWORD` | Sí          | —           | Contraseña del usuario |
| `DB_SSLMODE`  | No          | `require`   | `require` para RDS, `disable` para un postgres local |

Si falta `DB_HOST` o `DB_PASSWORD`, el backend no arranca y lo dice en el log:
`ERROR: falta la variable de entorno DB_HOST.`

## API

| Método | Ruta                   | Qué hace |
|--------|------------------------|----------|
| GET    | `/api/notas`           | Lista las notas, las más recientes primero |
| POST   | `/api/notas`           | Crea una nota: `{"texto": "...", "color": "amarillo"}` (colores: amarillo, rosa, verde, azul; texto de 1 a 280 caracteres). 400 si no es válida |
| POST   | `/api/notas/{id}/like` | Suma un like. 404 si no existe |
| DELETE | `/api/notas/{id}`      | Borra la nota. 404 si no existe |
| GET    | `/api/estado`          | `{"api":"ok","bd":"ok","contenedor":"<hostname>","version":"1.0.0"}`. Si la BD falla responde igualmente (200) con `"bd":"error"` |

La web consulta `/api/estado` cada 10 segundos y lo muestra en la franja de arriba (verde si todo va bien, rojo si algo falla).

Hibernate crea la tabla `notas` al arrancar (`spring.jpa.hibernate.ddl-auto=update`). Es cómodo para aprender, pero **en un proyecto real se usaría Flyway o Liquibase** para versionar los cambios del esquema.

## Tu tarea

El programa ya funciona, pero **le faltan todos los ficheros de contenedores**. Tu tarea es **dockerizar el proyecto y desplegarlo en el laboratorio** siguiendo la guía **[T03 · Docker: del IDE al laboratorio](docs/T03_Docker_Del_IDE_al_lab.md)**.

Tendrás que crear estos ficheros:

| Fichero                    | Para qué |
|----------------------------|----------|
| `backend/Dockerfile`       | Imagen del backend |
| `backend/.dockerignore`    | Qué no se copia al construir la imagen del backend |
| `frontend/Dockerfile`      | Imagen del frontend (Nginx con `index.html` y `nginx.conf`) |
| `frontend/.dockerignore`   | Qué no se copia al construir la imagen del frontend |
| `docker-compose.yml`       | Entorno de **desarrollo** |
| `docker-compose.lab.yml`   | Despliegue en el **laboratorio** (la base de datos es RDS) |

> La solución de referencia está en la rama [`solucion` del repositorio del profesor](https://github.com/guillermofoix/proyecto-minimo-docker/tree/solucion), no en el tuyo (al crear tu repositorio desde la plantilla solo se copia `main`). Conviene intentarlo por tu cuenta antes de mirarla.
