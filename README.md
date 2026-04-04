# Prueba Técnica - Ingeniero de Desarrollo Back End (BTG Pactual) - Jhon Javier Cardona Muñoz
# BTG Pactual - Sistema de Administración de Fondos

Esta es una API RESTful desarrollada en **Java 17** con **Spring Boot 3.2.x** y **MongoDB**, diseñada siguiendo los principios de **Arquitectura Limpia (Hexagonal)** para gestionar la vinculación y cancelación de fondos de inversión por parte de los clientes.

## 🚀 Tecnologías y Herramientas

*   **Java 17**
*   **Spring Boot 3.2.5**: Framework principal.
*   **Spring Data MongoDB**: Integración con la base de datos NoSQL.
*   **Lombok**: Reducción de código boilerplate (getters, setters, constructores).
*   **SpringDoc OpenAPI (Swagger UI)**: Documentación interactiva de la API.
*   **Docker & Docker Compose**: Contenerización de la base de datos MongoDB y Mongo Express.
*   **JUnit 5 & Spring Boot Test**: Pruebas unitarias y de integración.

---

## 🏗️ Arquitectura Limpia (Clean Architecture)

El proyecto está estructurado para separar las responsabilidades, lo que facilita su mantenimiento y evolución:

*   **`domain` (Dominio):** Contiene la lógica de negocio pura y el núcleo de la aplicación (`Cliente`, `Fondo`, `Transaccion`). Aquí residen los **Puertos** (interfaces) que definen cómo el dominio interactúa con el mundo exterior. No tiene dependencias de infraestructura ni de Spring (framework agnóstico).
*   **`application` (Aplicación):** Contiene los casos de uso (`AdministracionFondosService`). Orquesta la lógica llamando al dominio y usando los puertos inyectados.
*   **`infrastructure` (Infraestructura):** Capa externa que contiene los **Adaptadores**. Aquí se implementan los controladores REST (APIs), la conexión a MongoDB (`SpringDataMongoRepository`), y cualquier configuración propia de Spring Boot o librerías externas.

---

## 🛡️ Prevención de Doble Gasto (Control de Concurrencia)

Uno de los retos principales resueltos en esta API es la **prevención de doble gasto** cuando dos peticiones concurrentes intentan suscribirse a un fondo al mismo tiempo con un saldo justo al límite.

Esto se solucionó implementando **Optimistic Locking (Bloqueo Optimista)** de Spring Data MongoDB mediante la anotación `@Version`. 
Si dos hilos leen el mismo saldo y tratan de actualizarlo simultáneamente, el primer hilo incrementará la versión en la base de datos. El segundo hilo será rechazado instantáneamente con un `OptimisticLockingFailureException`, protegiendo la integridad financiera del cliente.

*(Existe un test de integración dedicado en `AdministracionFondosServiceConcurrencyTest.java` que demuestra esta protección).*

---

## 🐳 Inicialización del Entorno (Base de Datos)

El proyecto incluye un entorno Dockerizado listo para usarse, el cual inserta datos semilla ("sembrados") al arrancar por primera vez.

### 1. Levantar MongoDB
Asegúrate de estar en el directorio del proyecto donde se encuentra `docker-compose.yml` (`/fondos`) y ejecuta:

```bash
docker-compose up -d
```

### 2. Reiniciar los datos semilla (Troubleshooting)
El archivo `docker-config/mongodb/mongo-init.js` contiene datos iniciales (Clientes y Fondos). **Este archivo solo se ejecuta si el volumen de la base de datos está vacío.**
Si hiciste cambios estructurales o quieres limpiar la BD a su estado original, debes destruir el volumen de Docker:

```bash
# 1. Detener los contenedores y borrar el volumen huérfano persistente
docker-compose down -v

# 2. Levantar los contenedores nuevamente (esto leerá el mongo-init.js)
docker-compose up -d
```

**Datos Semilla por defecto:**
*   **Clientes**: Identificadores `1000000001`, `1000000002`, `1000000003` con saldos base de `$500,000`.
*   **Fondos**: FPVs y FICs con IDs `1`, `2`, `3`, `4`, `5` y montos mínimos variados (ej. `$75,000`, `$250,000`).

---

## ⚙️ Configuración y Ejecución de la API

La aplicación está configurada para ejecutarse bajo el puerto `8086` con el context-path `/btg`.

### Compilar y probar:
```bash
./mvnw clean install
```
*(Nota: El comando ejecuta los tests y comprueba la concurrencia. Si prefieres omitir los tests usa `./mvnw clean install -DskipTests`)*

### Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```

---

## 📖 Documentación de la API (Swagger)

Una vez que la aplicación esté corriendo, puedes interactuar con los endpoints sin necesidad de Postman mediante la interfaz de Swagger UI.

*   **Swagger UI:** [http://localhost:8086/btg/swagger-ui/index.html](http://localhost:8086/btg/swagger-ui/index.html)
*   **OpenAPI JSON:** [http://localhost:8086/btg/v3/api-docs](http://localhost:8086/btg/v3/api-docs)

---

## 🛠️ Mejoras Futuras

*   **Seguridad Stateless (JWT):** Implementación de Spring Security con tokens JWT en la capa de infraestructura para proteger los endpoints, excluyendo únicamente el Swagger UI y el path de autenticación.
*   **Mensajería Asíncrona:** Desacoplar el envío real de notificaciones (Email/SMS) mediante una cola de mensajes (ej. AWS SQS o RabbitMQ - Kafka) para evitar bloquear el hilo principal durante la respuesta HTTP.
*   **Monitoreo de los apis:** Mediante el uso de herramientas como Prometheus y Grafana.

---
