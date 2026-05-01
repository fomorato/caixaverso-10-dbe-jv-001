package com.ada.repository;

import com.ada.domain.entity.Emprestimo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class EmprestimoRepository implements PanacheRepositoryBase<Emprestimo, UUID> {

}
