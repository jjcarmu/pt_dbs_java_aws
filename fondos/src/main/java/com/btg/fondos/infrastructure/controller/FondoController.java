package com.btg.fondos.infrastructure.controller;

import com.btg.fondos.application.service.AdministracionFondosService;
import com.btg.fondos.domain.model.Transaccion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes/{clienteId}/fondos")
@RequiredArgsConstructor
@Tag(name = "Fondos", description = "Operaciones de suscripción, cancelación e historial de fondos")
public class FondoController {

    private final AdministracionFondosService fondosService;

    @PostMapping("/suscribir")
    @Operation(summary = "Suscribir a un fondo", description = "Permite a un cliente vincularse a un fondo específico validando su saldo disponible.")
    public ResponseEntity<Map<String, String>> suscribir(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.suscribirFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción exitosa al fondo"));
    }

    @PostMapping("/cancelar")
    @Operation(summary = "Cancelar suscripción", description = "Desvincula a un cliente de un fondo y le retorna el dinero a su saldo disponible.")
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.cancelarFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción cancelada exitosamente"));
    }

    @GetMapping("/transacciones")
    @Operation(summary = "Historial de transacciones", description = "Obtiene la lista de todas las aperturas y cancelaciones realizadas por el cliente.")
    public ResponseEntity<List<Transaccion>> obtenerHistorial(@PathVariable String clienteId) {
        return ResponseEntity.ok(fondosService.obtenerHistorial(clienteId));
    }
}