package com.ada.service;

import com.ada.domain.entity.Emprestimo;
import com.ada.domain.entity.Parcela;
import com.ada.domain.enums.TipoAmortizacao;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CalculadoraAmortizacaoService {

    public List<Parcela> gerarParcelas(Emprestimo emp) {
        if (emp == null) return List.of();
        if (emp.quantidadeParcelas == null || emp.quantidadeParcelas <= 0) return List.of();
        if (emp.valorTotal == null) return List.of();
        if (emp.taxaJurosMensal == null) return List.of();

        BigDecimal taxa = BigDecimal.valueOf(emp.taxaJurosMensal)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        if (emp.tipoAmortizacao == TipoAmortizacao.SAC) {
            return calcularSAC(emp.valorTotal, emp.quantidadeParcelas, taxa);
        }
        return calcularPRICE(emp.valorTotal, emp.quantidadeParcelas, taxa);
    }

    private List<Parcela> calcularSAC(BigDecimal pv, int n, BigDecimal i) {
        List<Parcela> parcelas = new ArrayList<>();
        BigDecimal amort = pv.divide(BigDecimal.valueOf(n), 10, RoundingMode.HALF_UP);
        BigDecimal saldo = pv;

        for (int k = 1; k <= n; k++) {
            BigDecimal juros = saldo.multiply(i);
            BigDecimal prestacao = amort.add(juros);
            BigDecimal novoSaldo = saldo.subtract(amort);

            parcelas.add(parcela(k, LocalDate.now().plusMonths(k),
                    amort, juros, prestacao, novoSaldo.max(BigDecimal.ZERO)));
            saldo = novoSaldo;
        }
        return parcelas;
    }

    private List<Parcela> calcularPRICE(BigDecimal pv, int n, BigDecimal i) {
        List<Parcela> parcelas = new ArrayList<>();

        // P = PV * i / (1 - (1+i)^-n)
        BigDecimal onePlusI = BigDecimal.ONE.add(i);

        // (1+i)^n
        BigDecimal powN = onePlusI.pow(n, java.math.MathContext.DECIMAL128);
        // (1+i)^-n = 1 / (1+i)^n
        BigDecimal invPowN = BigDecimal.ONE.divide(powN, 20, RoundingMode.HALF_UP);
        BigDecimal denom = BigDecimal.ONE.subtract(invPowN);

        BigDecimal prestacao = pv.multiply(i).divide(denom, 10, RoundingMode.HALF_UP);

        BigDecimal saldo = pv;
        for (int k = 1; k <= n; k++) {
            BigDecimal juros = saldo.multiply(i);
            BigDecimal amort = prestacao.subtract(juros);
            BigDecimal novoSaldo = saldo.subtract(amort);

            parcelas.add(parcela(k, LocalDate.now().plusMonths(k),
                    amort, juros, prestacao, novoSaldo.max(BigDecimal.ZERO)));
            saldo = novoSaldo;
        }
        return parcelas;
    }

    private Parcela parcela(int ordem, LocalDate venc, BigDecimal amort, BigDecimal juros,
                            BigDecimal prest, BigDecimal saldo) {
        Parcela p = new Parcela();
        p.ordem = ordem;
        p.dataVencimento = venc;
        p.valorAmortizacao = amort.setScale(2, RoundingMode.HALF_UP);
        p.valorJuros = juros.setScale(2, RoundingMode.HALF_UP);
        p.valorPrestacao = prest.setScale(2, RoundingMode.HALF_UP);
        p.saldoDevedor = saldo.setScale(2, RoundingMode.HALF_UP);
        return p;
    }
}
