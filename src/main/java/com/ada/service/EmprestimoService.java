package com.ada.service;

import com.ada.client.TaxasRestClient;
import com.ada.client.dto.RateResponse;
import com.ada.domain.entity.Emprestimo;
import com.ada.domain.entity.Parcela;
import com.ada.domain.enums.StatusEmprestimo;
import com.ada.dto.request.EmprestimoRequest;
import com.ada.exception.TaxasElegibilidadeException;
import com.ada.repository.EmprestimoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EmprestimoService {

    @Inject
    EmprestimoRepository emprestimoRepository;

    @Inject
    CalculadoraAmortizacaoService calculadora;

    @Inject
    @RestClient
    TaxasRestClient taxasClient;

    @Transactional
    public Emprestimo criar(EmprestimoRequest req) {

        RateResponse rate;
        try {
            rate = taxasClient.checkEligibility(
                    req.clienteId,
                    req.tipoAmortizacao.name()
            );
        } catch (Exception ex) {
            throw new TaxasElegibilidadeException("Falha ao consultar elegibilidade/taxas", ex);
        }

        Emprestimo emp = new Emprestimo();
        emp.id = UUID.randomUUID();
        emp.clienteId = req.clienteId;
        emp.valorTotal = req.valorTotal;
        emp.quantidadeParcelas = req.quantidadeParcelas;
        emp.tipoAmortizacao = req.tipoAmortizacao;
        emp.taxaJurosMensal = rate.taxaJurosMensal;
        emp.status = StatusEmprestimo.PENDENTE;

        List<Parcela> parcelas = calculadora.gerarParcelas(emp);
        for (Parcela p : parcelas) {
            p.id = UUID.randomUUID();
            emp.adicionarParcela(p);
        }

        emprestimoRepository.persist(emp);
        return emp;
    }

    public List<Emprestimo> listar(UUID clienteId) {
        if (clienteId == null) {
            return emprestimoRepository.listAll();
        }
        return emprestimoRepository.list("clienteId", clienteId);
    }

    @Transactional
    public void deletar(UUID id) {
        Emprestimo emp = emprestimoRepository.findById(id);
        if (emp == null) {
            return;
        }
        emprestimoRepository.delete(emp);
    }

    public Emprestimo buscar(UUID id) {
        return emprestimoRepository.findById(id);
    }
}
