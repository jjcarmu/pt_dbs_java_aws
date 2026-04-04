package com.btg.fondos.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document(collection = "transaccion")
public class Transaccion {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

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
