package com.ada.service;

import com.ada.domain.entity.Emprestimo;
import com.ada.domain.entity.Parcela;
import com.ada.domain.enums.StatusParcela;
import com.ada.repository.EmprestimoRepository;
import com.ada.repository.ParcelaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ParcelaService {

    @Inject
    ParcelaRepository parcelaRepository;

    @Inject
    EmprestimoRepository emprestimoRepository;

    @Transactional
    public List<Parcela> listarPorEmprestimo(UUID emprestimoId) {
        Emprestimo emp = emprestimoRepository.findById(emprestimoId);
        if (emp == null) return List.of();
        emp.parcelas.size();
        return emp.parcelas;
    }

    public Parcela buscar(UUID parcelaId) {
        return parcelaRepository.findById(parcelaId);
    }

    @Transactional
    public void baixarPagamento(UUID parcelaId) {
        Parcela p = parcelaRepository.findById(parcelaId);

        if (p == null) {
            return;
        }

        if (p.status == StatusParcela.PAGA) {
            throw new IllegalStateException("Parcela já está PAGA");
        }

        p.status = StatusParcela.PAGA;
        parcelaRepository.persist(p);
    }
}
