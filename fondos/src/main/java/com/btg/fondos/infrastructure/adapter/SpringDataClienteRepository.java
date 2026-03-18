package com.btg.fondos.infrastructure.adapter;

import com.btg.fondos.domain.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataClienteRepository extends MongoRepository<Cliente, String> {

}