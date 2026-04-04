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

@Service
@RequiredArgsConstructor
public class AdministracionFondosService {

    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final TransaccionRepository transaccionRepository;
    private final NotificacionFactory notificacionFactory;

    // Se remueve @Transactional para permitir ejecución en MongoDB Standalone (Docker simple)
    // El bloqueo optimista (@Version) igual protege la concurrencia.
    public void suscribirFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new IllegalArgumentException("Fondo no encontrado"));

        //Validar reglas de negocio y restar saldo (Delegado a la entidad)
        cliente.vincular(fondo);

        //Registrar la transacción de Apertura
        Transaccion transaccion = Transaccion.builder()
                .clienteId(cliente.getId())
                .fondoId(fondo.getId())
                .tipo(Transaccion.TipoTransaccion.APERTURA)
                .monto(fondo.getMontoMinimo())
                .build();

        //Guardar cambios
        clienteRepository.save(cliente);
        transaccionRepository.save(transaccion);

        //Enviar Notificación según preferencia
        NotificacionPort notificador = notificacionFactory.obtenerEstrategia(cliente.getPreferenciaNotificacion());
        String mensaje = String.format("Hola %s, te has suscrito exitosamente al fondo %s.", cliente.getNombre(), fondo.getNombre());
        notificador.enviar(cliente.getId(), mensaje);
    }

    // Se remueve @Transactional para permitir ejecución en MongoDB Standalone (Docker simple)
    public void cancelarFondo(String clienteId, String fondoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Fondo fondo = fondoRepository.findById(fondoId)
                .orElseThrow(() -> new IllegalArgumentException("Fondo no encontrado"));

        cliente.getFondosSuscritos().stream()
                .filter(s -> {
                    return s.equals(fondo.getId());
                })
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

    public List<Transaccion> obtenerHistorial(String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    public Optional<Cliente> getClienteById(String clienteId) {
        return clienteRepository.findById(clienteId);
    }
}
