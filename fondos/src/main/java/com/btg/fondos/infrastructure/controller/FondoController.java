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

/**
 * Controlador REST que maneja las operaciones transaccionales relacionadas con los fondos.
 * <p>
 * Expone los endpoints para que los clientes puedan suscribirse, cancelar suscripciones 
 * y consultar su historial de transacciones (aperturas y cancelaciones).
 * </p>
 * 
 * @author Jhon Javier Cardona Muñoz <jhonjcm2@gmail.com>
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/clientes/{clienteId}/fondos")
@RequiredArgsConstructor
@Tag(name = "Fondos", description = "Operaciones de suscripción, cancelación e historial de fondos")
public class FondoController {

    private final AdministracionFondosService fondosService;

    /**
     * Endpoint para procesar la suscripción de un cliente a un fondo de inversión.
     * <p>
     * Recibe el ID del cliente mediante la URL y el ID del fondo a través del cuerpo
     * de la petición (JSON). Delega la ejecución lógica a la capa de Aplicación.
     * </p>
     *
     * @param clienteId El identificador único del cliente en la URL.
     * @param payload   Un mapa JSON que debe contener la clave <code>fondoId</code>.
     * @return Una respuesta HTTP 200 OK con un mensaje de éxito.
     */
    @PostMapping("/suscribir")
    @Operation(summary = "Suscribir a un fondo", description = "Permite a un cliente vincularse a un fondo específico validando su saldo disponible.")
    public ResponseEntity<Map<String, String>> suscribir(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.suscribirFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción exitosa al fondo"));
    }

    /**
     * Endpoint para cancelar la suscripción de un cliente a un fondo de inversión.
     * <p>
     * Recibe el ID del cliente y el fondo, valida la existencia de la suscripción y procede
     * a desvincular al cliente, devolviendo el monto original a su saldo disponible.
     * </p>
     *
     * @param clienteId El identificador único del cliente en la URL.
     * @param payload   Un mapa JSON que debe contener la clave <code>fondoId</code>.
     * @return Una respuesta HTTP 200 OK con un mensaje de confirmación de cancelación.
     */
    @PostMapping("/cancelar")
    @Operation(summary = "Cancelar suscripción", description = "Desvincula a un cliente de un fondo y le retorna el dinero a su saldo disponible.")
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable String clienteId, @RequestBody Map<String, String> payload) {
        String fondoId = payload.get("fondoId");
        fondosService.cancelarFondo(clienteId, fondoId);
        return ResponseEntity.ok(Map.of("mensaje", "Suscripción cancelada exitosamente"));
    }

    /**
     * Endpoint para obtener el historial completo de transacciones de un cliente.
     * <p>
     * Lista todos los movimientos financieros generados por aperturas y cancelaciones 
     * de fondos para el cliente especificado.
     * </p>
     *
     * @param clienteId El identificador único del cliente.
     * @return Una respuesta HTTP 200 OK con la lista de objetos {@link Transaccion}.
     */
    @GetMapping("/transacciones")
    @Operation(summary = "Historial de transacciones", description = "Obtiene la lista de todas las aperturas y cancelaciones realizadas por el cliente.")
    public ResponseEntity<List<Transaccion>> obtenerHistorial(@PathVariable String clienteId) {
        return ResponseEntity.ok(fondosService.obtenerHistorial(clienteId));
    }
}
