package com.btg.fondos.domain.port;

import com.btg.fondos.domain.model.Fondo;
import java.util.Optional;

public interface FondoRepository {
    Optional<Fondo> findById(String id);
    Optional<Fondo> findByClienteId(String id);
    Fondo save(Fondo fondo);
    void deleteById(String fondoId);
}