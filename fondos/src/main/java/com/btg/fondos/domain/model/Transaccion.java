package com.btg.fondos.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Transaccion {

    @Builder.Default
    private String id = UUID.randomUUID().toString(); // Identificador único automático

    private String clienteId;
    private String fondoId;
    private TipoTransaccion tipo;
    private Double monto;

    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();

    public enum TipoTransaccion {
        APERTURA, CANCELACION
    }
}