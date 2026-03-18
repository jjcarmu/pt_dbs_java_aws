package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.port.NotificacionPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificacionAdapter implements NotificacionPort {

    @Override
    public void enviar(String destinatario, String mensaje) {
        log.info("Simulando envío de EMAIL a {}: {}", destinatario, mensaje);
    }

    @Override
    public String getTipo() {
        return "EMAIL";
    }
}