package com.ada.repository;

import com.ada.domain.entity.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClienteRepository implements PanacheRepositoryBase<Cliente, UUID> {

    public Optional<Cliente> findByDocumento(String documento) {
        return find("documento", documento).firstResultOptional();
    }
}