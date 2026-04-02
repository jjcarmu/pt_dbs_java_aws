package com.btg.fondos.domain.port;

import com.btg.fondos.domain.model.Transaccion;

import java.util.List;
import java.util.Optional;

public interface TransaccionRepository {
    Optional<Transaccion> findById(String id);
    List<Transaccion> findByClienteId(String id);
    Transaccion save(Transaccion transaccion);
}