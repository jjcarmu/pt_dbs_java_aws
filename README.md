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

## 🛠️ Entrega de los puntos de la Parte 1 - Fondos.

*   **1. Tecnologías sugeridas:** En la parte de Tecnologías y Herramientas se describe.
*   **2. Diseñar un modelo de datos NoSQL que soporte las operaciones:** En el archivo [mongo-init.js](fondos/docker-config/mongodb/mongo-init.js), se encuentra datos semilla para la verificación de la funcionalidad.
    Acontinuación se muestra el modelo de datos NoSQL para las operaciones.

    *    Cliente, Fondo y Transaccion respectivamente.

```json
{
  _id: '1000000001',
  nombre: 'Juan Perez',
  saldoDisponible: 425000,
  preferenciaNotificacion: 'EMAIL',
  fondosSuscritos: [
    '1'
  ],
  version: 1,
  _class: 'com.btg.fondos.domain.model.Cliente'
}
```

```json
{
_id: '1',
nombre: 'FPV_BTG_PACTUAL_RECAUDADORA',
montoMinimo: 75000,
categoria: 'FPV'
}
```

```json
{
  _id: '9e2cadb3-df1d-42af-ada4-80e3942d7647',
  clienteId: '1000000001',
  fondoId: '1',
  tipo: 'APERTURA',
  monto: 75000,
  fecha: ISODate('2026-04-04T15:16:48.309Z'),
  _class: 'com.btg.fondos.domain.model.Transaccion'
}
```

*   **3. Construir una API REST que implemente las funcionalidades descritas:** Se verifica la API, ingresando por la documentación Swagger o importando en Postman el archivo  [pruebas_funcionales.postman_collection.json](pruebas_funcionales.postman_collection.json). 
*   **4. Incluir:**
    *    Manejo de excepciones; Mediante el archivo [GlobalExceptionHandler.java](fondos/src/main/java/com/btg/fondos/infrastructure/config/GlobalExceptionHandler.java) permite manejar los tipos de excepciones que se requieran o si se crea una personalizada.
    *    Código limpio (Clean Code); Ver punto anterior  **Arquitectura Limpia (Clean Architecture)**.
    *    Pruebas unitarias: El el archivo [AdministracionFondosServiceTest.java](fondos/src/test/java/com/btg/fondos/application/service/AdministracionFondosServiceTest.java) se realiza pruebas rápidas y aisladas. No levantan el servidor Tomcat ni se conectan a MongoDB, sino que usan Mockito para simular la base de datos y los servicios de notificación.
         * Test principal: suscribirFondo_FlujoExitoso_GuardaCambiosYEnviaNotificacion().
         * Validaciones (Asserts):
           * Verifica que, al suscribirse a un fondo, el saldo del cliente se descuente matemáticamente bien (ej. 500000 - 75000 = 425000).
           * Comprueba que se genere y guarde una transacción de tipo APERTURA con el monto correcto.
           * Asegura que el servicio llame a la fábrica de notificaciones (NotificacionFactory) y dispare el método enviar(...) (simulando enviar un email o SMS).
         * Prueba de Integración y Concurrencia [AdministracionFondosServiceConcurrencyTest.java](fondos/src/test/java/com/btg/fondos/application/service/AdministracionFondosServiceConcurrencyTest.java). Esta es una prueba mucho más robusta e "integrada". Usa @SpringBootTest para levantar todo el contexto de Spring Data y sí se conecta a tu base de datos MongoDB real (la que corre en Docker). Se control transaccional y la prevención del "Doble Gasto" en un escenario de alta concurrencia.
           * Test principal: suscribirFondo_PeticionesConcurrentes_DebeBloquearDobleGasto().
           * Validaciones (Asserts):
             * Crea un cliente y un fondo semilla temporales en la base de datos MongoDB y asigna un saldo inicial.
             * Simula una "condición de carrera" (Race Condition) donde dos hilos leen exactamente al mismo tiempo el saldo original del cliente.
             * Intenta guardar ambas peticiones de suscripción.
             * Verifica que actúe el Bloqueo Optimista (Optimistic Locking): Comprueba que Spring Data MongoDB lance un OptimisticLockingFailureException en la petición que llega milisegundos tarde.
             * Asegura que, a nivel de base de datos física, el saldo final del cliente solo fue descontado una sola vez, protegiendo la integridad financiera de tu sistema.
    *    Buenas prácticas de seguridad y mantenibilidad.; Con respecto a seguridad y mantenibilidad, el seccion **Mejoras Futuras** se habla de luego implementar JWT, con el fin de tener mayor seguridad en los endpoints para un ambiente productivo, también la implementación del monitoreo que ayudaria a detectar posibles fallos, en general al utiizar una arquitectura hexagonal-based y clean code, ayuda a facilitar el mantenimiento del código.

* **5. Despliegue:** Para el despliegue, se puede realizar en diversos modos, pensando en un ambiente local para un desarrollador, un ambiente completamente dockerized para un equipo de pruebas, y el ambiente en una nube para producción.
    *    Local; Para este despliegue, se requiere tener instalado en el equipo local java, maven, docker y docker compose, y tener un entorno linux. aqui mediante docker se levanta el mongo, y en el equipo se levanta la aplicación.
         * Forma manual. 
         * Mediante Script bash []

    *    Dockerized; .
    *    AWS CloudFormation; .
---


## 🛠️ Entrega de la Parte 2 - SQL.

*   **1. Se crea la BD y las tablas** (Ver archivo [db_postgres.sql](db_postgres.sql)) en una base de datos Postgres con el fin de validar la consulta.
*   **2. SQL con la consulta requerida** (Ver archivo [parte2_consulta.sql](parte2_consulta.sql)), en este archivo he creado 2 consultas, la ultima un poco mejorada para rendimiento.
