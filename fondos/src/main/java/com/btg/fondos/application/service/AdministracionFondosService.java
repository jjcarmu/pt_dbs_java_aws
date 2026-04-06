package com.btg.fondos.application.service;

import com.btg.fondos.domain.model.Cliente;
import com.btg.fondos.domain.model.Fondo;
import com.btg.fondos.domain.model.Transaccion;
import com.btg.fondos.domain.port.ClienteRepository;
import com.btg.fondos.domain.port.FondoRepository;
import com.btg.fondos.application.factory.NotificacionFactory;
import com.btg.fondos.domain.port.NotificacionPort;
import com.btg.fondos.domain.port.TransaccionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación principal (Caso de Uso) para la administración de fondos.
 * <p>
 * Esta clase orquesta la lógica de negocio coordinando las llamadas entre el modelo 
 * de dominio (entidades como Cliente y Fondo) y los puertos de infraestructura 
 * (repositorios y notificaciones).
 * </p>
 * 
 * @author Jhon Javier Cardona Muñoz <jhonjcm2@gmail.com>
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AdministracionFondosService {

    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final TransaccionRepository transaccionRepository;
    private final NotificacionFactory notificacionFactory;

    /**
     * Suscribe un cliente a un fondo de inversión específico.
     * <p>
     * El proceso involucra:
     * <ol>
     *   <li>Validar la existencia del cliente y el fondo.</li>
     *   <li>Descontar el saldo del cliente (lógica delegada al dominio).</li>
     *   <li>Registrar la transacción histórica de apertura.</li>
     *   <li>Guardar los cambios en base de datos.</li>
     *   <li>Enviar una notificación al cliente según su preferencia.</li>
     * </ol>
     * Nota: La concurrencia está protegida a nivel de base de datos por Optimistic Locking (@Version).
     * </p>
     *
     * @param clienteId El identificador único del cliente (Ej. número de documento).
     * @param fondoId   El identificador único del fondo al que se desea suscribir.
     * @throws IllegalArgumentException si el cliente o el fondo no existen en el sistema.
     * @throws com.btg.fondos.domain.exception.SaldoInsuficienteException si el cliente no tiene fondos suficientes.
     * @throws org.springframework.dao.OptimisticLockingFailureException si hay colisión por doble gasto recurrente.
     */
    public void suscribirFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new IllegalArgumentException("Fondo no encontrado"));

        // Validar reglas de negocio y restar saldo (Delegado a la entidad)
        cliente.vincular(fondo);

        // Registrar la transacción de Apertura
        Transaccion transaccion = Transaccion.builder()
                .clienteId(cliente.getId())
                .fondoId(fondo.getId())
                .tipo(Transaccion.TipoTransaccion.APERTURA)
                .monto(fondo.getMontoMinimo())
                .build();

        // Guardar cambios
        clienteRepository.save(cliente);
        transaccionRepository.save(transaccion);

        // Enviar Notificación según preferencia
        NotificacionPort notificador = notificacionFactory.obtenerEstrategia(cliente.getPreferenciaNotificacion());
        String mensaje = String.format("Hola %s, te has suscrito exitosamente al fondo %s.", cliente.getNombre(), fondo.getNombre());
        notificador.enviar(cliente.getId(), mensaje);
    }

    /**
     * Cancela la suscripción de un cliente a un fondo de inversión.
     * <p>
     * Restituye el monto del fondo al saldo disponible del cliente y registra la 
     * transacción correspondiente.
     * </p>
     *
     * @param clienteId El identificador único del cliente.
     * @param fondoId   El identificador único del fondo que se desea cancelar.
     * @throws IllegalArgumentException si el cliente no existe, el fondo no existe, 
     *                                  o si el cliente no está suscrito a dicho fondo.
     */
    public void cancelarFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new IllegalArgumentException("Fondo no encontrado"));

        cliente.getFondosSuscritos().stream()
                .filter(s -> s.equals(fondo.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("El cliente %s, no tiene suscrito el fondo %s.", cliente.getNombre(), fondo.getNombre())));

        // 1. Validar reglas de negocio y devolver saldo (Delegado a la entidad)
        cliente.desvincular(fondo);

        // 2. Registrar la transacción de Cancelación
        Transaccion transaccion = Transaccion.builder()
                .clienteId(cliente.getId())
                .fondoId(fondo.getId())
                .tipo(Transaccion.TipoTransaccion.CANCELACION)
                .monto(fondo.getMontoMinimo())
                .build();

        // 3. Guardar cambios
        clienteRepository.save(cliente);
        transaccionRepository.save(transaccion);
    }

    /**
     * Obtiene el historial completo de transacciones (Aperturas y Cancelaciones) de un cliente.
     *
     * @param clienteId El identificador único del cliente.
     * @return Una lista de transacciones. Retorna lista vacía si no hay movimientos.
     */
    public List<Transaccion> obtenerHistorial(String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }

    /**
     * Obtiene el listado de todos los clientes registrados en el sistema.
     * <p>
     * ATENCIÓN: Este método podría traer problemas de rendimiento en un entorno de 
     * producción si la colección es muy grande. Se recomienda implementar paginación en el futuro.
     * </p>
     *
     * @return Una lista con todos los objetos {@link Cliente}.
     */
    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    /**
     * Busca y retorna un cliente por su ID.
     *
     * @param clienteId El identificador único del cliente.
     * @return Un {@link Optional} que contiene el cliente si se encuentra, o vacío en caso contrario.
     */
    public Optional<Cliente> getClienteById(String clienteId) {
        return clienteRepository.findById(clienteId);
    }
}
