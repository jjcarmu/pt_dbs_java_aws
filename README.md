# pt_dbs_java_aws.
# Prueba Técnica: PS 027 - Desarrollador Backend Senior (Java/AWS) - Jhon Javier Cardona

Descripción breve de la solución, arquitectura limpia utilizada (Spring Boot + MongoDB) y patrones aplicados.

## 1. Requisitos Previos
* Java 17 (o la versión que uses)
* Maven
* Docker y Docker Compose instalados.

## 2. Levantar la Base de Datos (Entorno Local)
Para ejecutar la aplicación localmente, primero debemos levantar los contenedores de MongoDB y Mongo Express. En la raíz del proyecto, ejecuta el siguiente comando:

    ```bash
    docker-compose up -d


## 1 - Tecnologías Escogidas

### Core Stack
* ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) **JDK 17**: Elegido por su soporte de registros y mejoras en rendimiento.
* ![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) **Spring Boot 3.x**: Para agilizar el desarrollo de microservicios.

### Infraestructura y DevOps
* ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) **Docker**: Containerización de servicios.
*  **AWS**: Para desplegar los microservicios en la nube.

## 2 - Diseño de datos NoSQL que soportan operaciones

* Coleción cliente
  ```json
    {
    "_id": "uuid-cliente-001",
    "nombre": "Jhon Cardona",
    "saldoDisponible": 500000.0,
    "preferenciaNotificacion": "EMAIL",
    "fondosSuscritos": [
    "1", "3"
    ]
    }


* Coleción fondos
  ```json
  {
  "_id": "1", 
  "nombre": "FPV_BTG_PACTUAL_RECAUDADORA",
  "montoMinimo": 75000.0,
  "categoria": "FPV"
  }

* Coleción transacciones
   ```json
  {
  "_id": "uuid-transaccion-101",
  "clienteId": "uuid-cliente-001",
  "fondoId": "1",
  "tipoTransaccion": "APERTURA", // Puede ser "APERTURA" o "CANCELACION"
  "monto": 75000.0,
  "fecha": "2026-03-17T20:26:54Z"
  }

## 

## Inicialización de Datos (Seed Data)

Para que la aplicación funcione correctamente desde el inicio, es necesario poblar la base de datos con el catálogo de fondos disponibles y crear un usuario de prueba.

Puedes ejecutar los siguientes comandos directamente en la consola de tu cliente MongoDB (como MongoDB Compass, mongosh, o desde Mongo Express en `http://localhost:8081`).

### 1. Seleccionar la base de datos
    use btg_fondos_db;
### 2. Insertar fondos    
    db.fondos.insertMany([
      {
        "_id": "1",
        "nombre": "FPV_BTG_PACTUAL_RECAUDADORA",
        "montoMinimo": 75000.0,
        "categoria": "FPV"
      },
      {
        "_id": "2",
        "nombre": "FPV_BTG_PACTUAL_ECOPETROL",
        "montoMinimo": 125000.0,
        "categoria": "FPV"
      },
      {
        "_id": "3",
        "nombre": "DEUDAPRIVADA",
        "montoMinimo": 50000.0,
        "categoria": "FIC"
      },
      {
        "_id": "4",
        "nombre": "FDO-ACCIONES",
        "montoMinimo": 250000.0,
        "categoria": "FIC"
      },
      {
        "_id": "5",
        "nombre": "FPV_BTG_PACTUAL_DINAMICA",
        "montoMinimo": 100000.0,
        "categoria": "FPV"
      }
    ]);
### 3. Insertar insertar usuario de prueba    
    db.clientes.insertOne({
      "_id": "cliente-prueba-001",
      "nombre": "Usuario BTG",
      "saldoDisponible": 500000.0,
      "preferenciaNotificacion": "EMAIL",
      "fondosSuscritos": []
    });


## Despliegue en AWS (CloudFormation)

[cite_start]La infraestructura de este proyecto está definida como código utilizando AWS CloudFormation, lo que permite un despliegue automatizado, predecible y escalable.

La plantilla `cloudformation-template.yaml` aprovisiona una instancia Amazon EC2 (`t2.micro` por defecto para capa gratuita) configurada con un *Security Group* que expone el puerto 8080 para la API REST. Además, utiliza `UserData` para instalar automáticamente el motor de Docker y Docker Compose en el arranque del servidor.

### Pasos para el despliegue:

1. Inicia sesión en la **Consola de Administración de AWS**.
2. Navega al servicio **CloudFormation**.
3. Haz clic en **Crear pila (Create stack)** y selecciona "Con recursos nuevos (estándar)".
4. Selecciona **Cargar un archivo de plantilla (Upload a template file)** y sube el archivo `cloudformation-template.yaml` ubicado en la raíz de este repositorio.
5. Asigna un nombre a la pila (por ejemplo: `btg-fondos-stack`).
6. En la sección de parámetros, puedes mantener el `InstanceType` por defecto (`t2.micro`).
7. Haz clic en **Siguiente** hasta llegar a la pantalla final y presiona **Enviar (Submit)**.
8. Espera a que el estado de la pila cambie a `CREATE_COMPLETE`.
9. Ve a la pestaña **Salidas (Outputs)** de la pila. Allí encontrarás la clave `ApiUrl` con el enlace público para probar los endpoints de la API desplegada.
10
