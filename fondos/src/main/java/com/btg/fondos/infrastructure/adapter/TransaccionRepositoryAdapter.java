package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Transaccion;
import com.btg.fondos.domain.port.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransaccionRepositoryAdapter implements TransaccionRepository {

    private final SpringDataTransaccionRepository mongoRepository;

    @Override
    public Transaccion save(Transaccion transaccion) {
        return mongoRepository.save(transaccion);
    }

    @Override
    public Optional<Transaccion> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Transaccion> findByClienteId(String clienteId) {
        return mongoRepository.findByClienteId(clienteId);
    }
}