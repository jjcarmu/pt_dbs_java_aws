package com.btg.fondos.infrastructure.controller;

import com.btg.fondos.application.service.AdministracionFondosService;
import com.btg.fondos.domain.model.Cliente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operaciones de consulta relacionadas con los Clientes.
 * <p>
 * Facilita la exposición de datos maestros de los clientes, como listados globales 
 * o consultas detalladas por identificador.
 * </p>
 * 
 * @author Jhon Javier Cardona Muñoz <jhonjcm2@gmail.com>
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/clientes/")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones de consulta y visualización de clientes")
public class ClienteController {

    private final AdministracionFondosService fondosService;

    /**
     * Endpoint para obtener un listado de todos los clientes registrados.
     * <p>
     * Retorna la información completa de estado de cada cliente, incluyendo
     * su saldo actual y la lista de fondos a los que está suscrito.
     * </p>
     *
     * @return Una respuesta HTTP 200 OK con la lista completa de {@link Cliente}.
     */
    @GetMapping("/all")
    @Operation(summary = "Clientes", description = "Obtiene la lista de todos los clientes.")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(fondosService.getAllClientes());
    }

    /**
     * Endpoint para buscar a un cliente específico mediante su identificador.
     * <p>
     * Útil para consultar el estado en tiempo real (saldo y fondos) de una
     * cuenta individual.
     * </p>
     *
     * @param clienteId El identificador único del cliente en la base de datos.
     * @return Una respuesta HTTP 200 OK con el objeto {@link Cliente} embebido dentro 
     *         de un {@link Optional}.
     */
    @GetMapping("{clienteId}/id")
    @Operation(summary = "Clientes por ID", description = "Obtiene un cliente por su identificador único.")
    public ResponseEntity<Optional<Cliente>> getById(@PathVariable String clienteId) {
        return ResponseEntity.ok(fondosService.getClienteById(clienteId));
    }
}
