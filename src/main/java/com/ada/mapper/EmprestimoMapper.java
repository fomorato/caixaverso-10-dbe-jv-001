package com.ada.mapper;

import com.ada.domain.entity.Emprestimo;
import com.ada.domain.entity.Parcela;
import com.ada.dto.response.EmprestimoResponse;
import com.ada.dto.response.ParcelaResponse;

import java.util.ArrayList;

public class EmprestimoMapper {

    public static EmprestimoResponse toResponse(Emprestimo emp) {
        if (emp == null) return null;

        EmprestimoResponse r = new EmprestimoResponse();
        r.id = emp.id;
        r.clienteId = emp.clienteId;
        r.valorTotal = emp.valorTotal;
        r.quantidadeParcelas = emp.quantidadeParcelas;
        r.tipoAmortizacao = emp.tipoAmortizacao;
        r.taxaJurosMensal = emp.taxaJurosMensal;
        r.status = emp.status;
        r.parcelas = new ArrayList<>();

        if (emp.parcelas != null) {
            for (Parcela p : emp.parcelas) {
                r.parcelas.add(toResponse(p));
            }
        }

        return r;
    }

    public static ParcelaResponse toResponse(Parcela p) {
        if (p == null) return null;

        ParcelaResponse r = new ParcelaResponse();
        r.id = p.id;
        r.ordem = p.ordem;
        r.dataVencimento = p.dataVencimento;
        r.valorAmortizacao = p.valorAmortizacao;
        r.valorJuros = p.valorJuros;
        r.valorPrestacao = p.valorPrestacao;
        r.saldoDevedor = p.saldoDevedor;
        r.status = p.status;

        return r;
    }
}
