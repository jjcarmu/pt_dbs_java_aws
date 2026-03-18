/* Se realiza el ejercicio SQL, sobre PostgreSQL*/
/* Creacion de la base de datos*/
CREATE DATABASE BTG;

/* Creación de tablas maestras*/
CREATE TABLE cliente (
     id integer PRIMARY KEY,
     nombre VARCHAR(100) NOT NULL,
     apellidos VARCHAR(100) NOT NULL,
     ciudad VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
      id integer PRIMARY KEY,
      nombre VARCHAR(100) NOT NULL,
      tipoProducto VARCHAR(100) NOT NULL
);

CREATE TABLE sucursal (
      id integer PRIMARY KEY,
      nombre VARCHAR(100) NOT NULL,
      ciudad VARCHAR(100) NOT NULL
);

-- Creación de entidades intermedias
CREATE TABLE inscripcion (
     idProducto integer,
     idCliente integer,
     PRIMARY KEY (idProducto, idCliente),
     CONSTRAINT fk_producto FOREIGN KEY (idProducto) REFERENCES producto(id),
     CONSTRAINT fk_cliente FOREIGN KEY (idCliente) REFERENCES cliente(id)
);

CREATE TABLE disponibilidad (
    idSucursal integer,
    idProducto integer,
    PRIMARY KEY (idSucursal, idProducto),
    CONSTRAINT fk_sucursal FOREIGN KEY (idSucursal) REFERENCES sucursal(id),
    CONSTRAINT fk_producto_disp FOREIGN KEY (idProducto) REFERENCES producto(id)
);

CREATE TABLE visitan (
     idSucursal integer,
     idCliente integer,
     fechaVisita DATE NOT NULL,
     PRIMARY KEY (idSucursal, idCliente),
     CONSTRAINT fk_sucursal_visita FOREIGN KEY (idSucursal) REFERENCES sucursal(id),
     CONSTRAINT fk_cliente_visita FOREIGN KEY (idCliente) REFERENCES cliente(id)
);


/* SQL con respuesta a Parte 2 – SQL (20%)*/
SELECT c.nombre
FROM cliente c
INNER JOIN visitan v
    ON c.id = v.idCliente
INNER JOIN sucursal s
    ON s.id = v.idSucursal
INNER JOIN disponibilidad d
    ON s.id = d.idSucursal
INNER JOIN inscripcion i
    ON c.id = i.idCliente AND i.idProducto = d.idProducto;

/*Version Mejorada con ayuda de herramientas*/
SELECT DISTINCT c.nombre
FROM cliente c
JOIN inscripcion i
    ON c.id = i.idCliente
JOIN disponibilidad d
    ON i.idProducto = d.idProducto
JOIN visitan v
    ON v.idSucursal = d.idSucursal AND v.idCliente = c.id;
