package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Fondo;
import com.btg.fondos.domain.port.FondoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FondoRepositoryAdapter implements FondoRepository {

    private final SpringDataFondoRepository mongoRepository;

    @Override
    public Optional<Fondo> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Fondo> findByClienteId(String id) {
        return Optional.empty();
    }

    @Override
    public Fondo save(Fondo fondo) {
        return null;
    }

    // Si tienes otros métodos en la interfaz FondoRepository, impleméntalos aquí
}