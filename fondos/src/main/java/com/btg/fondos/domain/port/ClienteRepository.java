package com.btg.fondos.domain.port;

import com.btg.fondos.domain.model.Cliente;
import java.util.Optional;

public interface ClienteRepository {
    Optional<Cliente> findById(String id);
    Cliente save(Cliente cliente);
}