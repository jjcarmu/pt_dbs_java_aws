package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteId(String clienteId);
}