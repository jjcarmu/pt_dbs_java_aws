package com.btg.fondos.infrastructure.controller;

import com.btg.fondos.application.service.AdministracionFondosService;
import com.btg.fondos.domain.model.Transaccion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes/{clienteId}/fondos")
@RequiredArgsConstructor
public class FondoController {

    private final AdministracionFondosService fondosService;

    @PostMapping("/suscribir")
    public ResponseEntity<Map<String, String>> suscribir(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.suscribirFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción exitosa al fondo"));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.cancelarFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción cancelada exitosamente"));
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<Transaccion>> obtenerHistorial(@PathVariable String clienteId) {
        return ResponseEntity.ok(fondosService.obtenerHistorial(clienteId));
    }
}