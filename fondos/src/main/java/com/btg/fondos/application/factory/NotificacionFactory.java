package com.btg.fondos.application.factory;

import com.btg.fondos.domain.port.NotificacionPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificacionFactory {

    private final Map<String, NotificacionPort> estrategias;

    // Spring inyecta automáticamente todas las clases que implementen NotificacionPort
    public NotificacionFactory(List<NotificacionPort> notificadores) {
        this.estrategias = notificadores.stream()
                .collect(Collectors.toMap(NotificacionPort::getTipo, Function.identity()));
    }

    public NotificacionPort obtenerEstrategia(String preferencia) {
        NotificacionPort estrategia = estrategias.get(preferencia != null ? preferencia.toUpperCase() : "EMAIL");
        if (estrategia == null) {
            throw new IllegalArgumentException("Tipo de notificación no soportado: " + preferencia);
        }
        return estrategia;
    }
}