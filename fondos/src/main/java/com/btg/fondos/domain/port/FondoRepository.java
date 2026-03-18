package com.btg.fondos.domain.port;

import com.btg.fondos.domain.model.Fondo;
import java.util.Optional;

public interface FondoRepository {
    Optional<Fondo> findById(String id);
    Optional<Fondo> findByIdClienteId(String id);
    Fondo save(Fondo fondo);
}