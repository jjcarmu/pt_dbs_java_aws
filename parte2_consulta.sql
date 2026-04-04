-- Se desarrollo las consultas de la parte 2 de la prueba tecnica

SELECT DISTINCT c.nombre, c.apellidos
FROM cliente c
INNER JOIN visitan v
    ON c.id = v.idCliente
INNER JOIN sucursal s
    ON s.id = v.idSucursal
INNER JOIN disponibilidad d
    ON s.id = d.idSucursal
INNER JOIN inscripcion i
    ON c.id = i.idCliente AND i.idProducto = d.idProducto;

-- Version Mejorada de la consulta anterior
SELECT DISTINCT c.nombre
FROM cliente c
JOIN inscripcion i
    ON c.id = i.idCliente
JOIN disponibilidad d
    ON i.idProducto = d.idProducto
JOIN visitan v
    ON v.idSucursal = d.idSucursal AND v.idCliente = c.id;
