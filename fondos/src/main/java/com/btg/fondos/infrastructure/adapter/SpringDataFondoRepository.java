package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataFondoRepository extends MongoRepository<Fondo, String> {
}