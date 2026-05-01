package com.ada.repository;

import com.ada.domain.entity.Parcela;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ParcelaRepository implements PanacheRepositoryBase<Parcela, UUID> {

}
