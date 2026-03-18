package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Cliente;
import com.btg.fondos.domain.port.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteRepository mongoRepository;

    @Override
    public Optional<Cliente> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return mongoRepository.save(cliente);
    }
}