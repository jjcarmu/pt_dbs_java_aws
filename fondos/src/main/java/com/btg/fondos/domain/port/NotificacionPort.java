package com.btg.fondos.domain.port;

public interface NotificacionPort {
    void enviar(String destinatario, String mensaje);
    String getTipo(); // Retornará "EMAIL" o "SMS"
}