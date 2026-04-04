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

@RestController
@RequestMapping("/api/v1/clientes/")
@RequiredArgsConstructor
@Tag(name = "Fondos", description = "Operaciones de suscripción, cancelación e historial de fondos")
public class ClienteController {

    private final AdministracionFondosService fondosService;

    @GetMapping("/all")
    @Operation(summary = "Clientes", description = "Obtiene la lista de todos los clientes.")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(fondosService.getAllClientes());
    }

    @GetMapping("{clienteId}/id")
    @Operation(summary = "Clientes por ID", description = "Obtiene un cliente por su identificador único.")
    public ResponseEntity<Optional<Cliente>> getById(@PathVariable String clienteId) {
        return ResponseEntity.ok(fondosService.getClienteById(clienteId));
    }
}