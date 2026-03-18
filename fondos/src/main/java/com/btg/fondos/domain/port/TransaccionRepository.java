package com.btg.fondos.domain.port;

import com.btg.fondos.domain.model.Transaccion;
import java.util.Optional;

public interface TransaccionRepository {
    Optional<Transaccion> findById(String id);
    Optional<Transaccion> findByIdClienteId(String id);
    Transaccion save(Transaccion transaccion);
}